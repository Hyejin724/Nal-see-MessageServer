package everycoding.NalseeFlux.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.StringTokenizer;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

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

    public Mono<Void> deleteUserData(Long userId) {
        Mono<Void> senderMono = deleteSender(userId);
        Mono<Void> receiverMono = deleteReceiver(userId);

        return Mono.when(senderMono, receiverMono)
                .doOnSuccess(aVoid -> log.info("Both sender and receiver updates completed for userId: {}", userId));
    }

    private Mono<Void> deleteSender(Long userId) {
        return chatRepository.findBySenderId(userId)
                .doOnNext(chat -> {
                    chat.setSender("탈퇴한 사용자");
                    chat.setSenderImg("https://avatars.githubusercontent.com/u/161725279?s=400&u=eed6bcb03606ab9253332be66ec36ae17cbcabd9&v=4");
                })
                .flatMap(chatRepository::save)
                .then();
    }

    private Mono<Void> deleteReceiver(Long userId) {
        return chatRepository.findByReceiverId(userId)
                .doOnNext(chat -> {
                    chat.setReceiver("탈퇴한 사용자");
                    chat.setReceiverImg("https://avatars.githubusercontent.com/u/161725279?s=400&u=eed6bcb03606ab9253332be66ec36ae17cbcabd9&v=4");
                })
                .flatMap(chatRepository::save)
                .then();
    }
}