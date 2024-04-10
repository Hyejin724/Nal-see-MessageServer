package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.webClientService.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        // chatId로부터 사용자 ID 파싱
        List<Long> ids = Arrays.stream(chatId.split("-"))
                .map(Long::parseLong)
                .toList();

        // 현재 사용자가 채팅방에 속해 있는지 검증
        if (!ids.contains(userId)) {
            return Flux.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied"));
        }

        // 검증 후 메시지 조회 및 반환
        return chatRepository.findByChatIdOrderByCreateAtDesc(chatId);
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
}