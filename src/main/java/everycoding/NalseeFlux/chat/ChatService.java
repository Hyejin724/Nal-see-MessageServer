package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.webClientService.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final NotificationService notificationService;

    public Flux<Chat> getLatestChatsWithUserId(Long userId) {
        // 모든 Chat을 가져온 후, Flux를 활용하여 처리
        return chatRepository.findAll()
                .filter(chat -> chat.getChatId().contains(String.valueOf(userId))) // userId가 포함된 chatId를 가진 Chat만 필터링
                .groupBy(Chat::getChatId) // chatId별로 그룹핑
                .flatMap(Flux::last); // 각 그룹별 마지막 Chat (가장 최근의 Chat)만 선택
//                .map(chat -> ChatDto.builder()
//                        .chat(chat)
//                        .isOnline(notificationService.checkOnlineUser(getUserId(chat, userId)).block())
//                        .build());

    }
    public Long getUserId(Chat chat, Long id) {
        String chatId = chat.getChatId();
        StringTokenizer st = new StringTokenizer(chatId, "-");
        long userId = 0;
        while (st.hasMoreTokens()) {
            userId = Long.parseLong(st.nextToken());
            if (userId != id) {
                log.info(String.valueOf(userId));
                return userId;
            }
        }
        return userId;
    }

//    private Mono<ChatDto> processChatGroup(GroupedFlux<String, Chat> chatGroup, Long userId) {
//        return chatGroup.reduce((chat1, chat2) -> chat2)  // Always get the last chat
//                .flatMap(chat -> notificationService.checkOnlineUser(getUserId(chat, userId))
//                        .map(isOnline -> ChatDto.builder()
//                                .chat(chat)
//                                .isOnline(isOnline)
//                                .build())
//                );
//    }
//
//
//    public Flux<ChatDto> getLatestChatsWithUserIds(Long userId) {
//        return chatRepository.findWithTailableCursorBy()
//                .filter(chat -> chat.getChatId().contains(String.valueOf(userId)))
//                .map(chat -> Pair.of(chat.getChatId(), chat))
//                .scan(new HashMap<String, Chat>(), (state, update) -> {
//                    state.put(update.getKey(), update.getValue());
//                    return new HashMap<>(state); // 새로운 상태를 반환하여 변경 사항을 방출
//                })
//                .flatMap(map -> Flux.fromIterable(map.values())) // 최신 상태의 모든 채팅을 방출
//                .map(chat -> ChatDto.builder()
//                        .chat(chat)
//                        .isOnline(notificationService.checkOnlineUser(getUserId(chat, userId)).block())
//                        .build());
//    }
}