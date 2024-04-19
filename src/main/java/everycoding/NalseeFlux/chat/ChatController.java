package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.webClientService.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {

    private final ChatRepository chatRepository;
    private final AuthenticationService authenticationService;
    private final ChatService chatService;

    @GetMapping("/chats/{chatId}")
    public Flux<Chat> getMessagesByChatId(HttpServletRequest request, @PathVariable String chatId) {
        // 인증된 사용자 ID 확인
        String accessToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("AccessToken".equals(cookie.getName())){
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        log.info("AccessToken={}", accessToken);
        Mono<UserInfo> userInfo = authenticationService.getUserInfo(accessToken);
        UserInfo block = userInfo.block();
        Long userId = block.getUserId();

        StringTokenizer st = new StringTokenizer(chatId, "-");
        long userId1 = Long.parseLong(st.nextToken());
        long userId2 = Long.parseLong(st.nextToken());
        long anotherId;

        // 현재 사용자가 채팅방에 속해 있는지 검증
        if (!Objects.equals(userId1, userId) && !Objects.equals(userId2, userId)) {
            return Flux.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied"));
        }
        if (userId1 == userId) {
            anotherId = userId2;
        } else if (userId2 == userId) {
            anotherId = userId1;
        } else {
            anotherId = 0;
        }

        // 메시지 조회 exitUserId에 해당사항 없는 메시지만 조회
        Flux<Chat> chats = chatRepository.findByChatIdOrderByCreateAtAsc(chatId)
                .filter(chat -> !Objects.equals(chat.getExitUserId1(), userId))
                .filter(chat -> !Objects.equals(chat.getExitUserId2(), userId));

        // read_cnt를 0으로 설정하고 데이터베이스에 저장
        Flux<Chat> updatedChats = chats.flatMap(chat -> {
            if (chat.getSenderId().equals(anotherId)) {
                chat.setReadCnt(0);
                return chatRepository.save(chat);
            }
            return Flux.just(chat); // 변경하지 않은 채로 반환
        });

        return updatedChats // 업데이트된 Chat 객체들을 반환
                .sort(Comparator.comparing(Chat::getCreateAt)) // CreateAt 기준으로 오름차순 정렬
                .collectList() // Flux를 List로 변환
                .flatMapMany(Flux::fromIterable); // List를 다시 Flux로 변환

    }

    @GetMapping("/chats")
    public ResponseEntity<Flux<Chat>> getALlChatRoom(HttpServletRequest request) {
        String accessToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("AccessToken".equals(cookie.getName())){
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        log.info("AccessToken={}", accessToken);
        UserInfo userInfo = authenticationService.getUserInfo(accessToken).block();
        Long userId = Objects.requireNonNull(userInfo).getUserId();

        Flux<Chat> latestChatsWithUserId = chatService.getLatestChatsWithUserId(userId);
        return ResponseEntity.ok(latestChatsWithUserId);
    }

    @PostMapping("/delete-user")
    public Mono<Void> deleteUserUpdate(@RequestParam Long userId) {
        log.info("탈퇴한 사용자 처리 완료 = {}", userId);
        return chatService.deleteUserData(userId);
    }

    @PostMapping("/exit-room")
    public ResponseEntity<Mono<Void>> exitUserUpdate(HttpServletRequest request, @RequestParam String chatId) {
        String accessToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("AccessToken".equals(cookie.getName())){
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        log.info("AccessToken={}", accessToken);
        UserInfo userInfo = authenticationService.getUserInfo(accessToken).block();
        Long userId = Objects.requireNonNull(userInfo).getUserId();

        log.info("방 나간 사용자 추가 완료 = {}", chatId);
        return ResponseEntity.ok(chatService.exitUserData(userId, chatId));
    }
}