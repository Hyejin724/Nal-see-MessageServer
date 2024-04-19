package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.config.websocket.WebSocketRoomUserSessionMapper;
import everycoding.NalseeFlux.dto.MessageEventDto;
import everycoding.NalseeFlux.dto.MessageRequestDto;
import everycoding.NalseeFlux.dto.MessageResponseDto;
import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.webClientService.AuthenticationService;
import everycoding.NalseeFlux.webClientService.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final ChatRepository chatRepository;
    private final AuthenticationService authenticationService;
    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    @MessageMapping("/{chatId}/chat")
    @SendTo("/sub/{chatId}/chat")
    public Mono<MessageResponseDto> message(@DestinationVariable String chatId, MessageRequestDto messageRequestDto, Principal principal) {
        // 로그인한 사람(내 정보) 가지고 오기
        String sessionId = principal.getName(); // WebSocket 연결 시 설정된 세션 ID 또는 사용자 식별자 사용
        log.info("sessionId={}", sessionId);
        UserInfo userInfo = webSocketRoomUserSessionMapper.getUserInfoBySessionId(sessionId);
        Long userId = userInfo.getUserId();

        // messageRequestDto를 활용하여 받는사람 정보조회
        Long receiverId = messageRequestDto.getReceiverId();
        log.info("userId={}", userId);
        //receiver Id 활용하여 상대방 이름, 이미지 가지고오는 Web Client Logic
        UserInfo receiverInfo = authenticationService.checkUserExistence(receiverId).block();

        // 메인서버에 fcm으로 전송하는 로직
        MessageEventDto build = MessageEventDto.builder()
                .receiverName(Objects.requireNonNull(receiverInfo).getUserName())
                .receiverId(receiverId)
                .senderName(userInfo.getUserName())
                .senderId(userId)
                .message(messageRequestDto.getContent())
                .build();
        notificationService.sendEvent(build)
                .subscribe(
                        null, // onNext는 Void 타입이므로 필요 없음
                        error -> log.error("Error 처리", error),
                        () -> log.info("처리 완료")
                );



        return authenticationService.checkUserExistence(receiverId)
                .flatMap(receiverUserInfo -> {
                    boolean isReceiverSubscribed = subscriptionService.isUserSubscribed(receiverId, "/sub/"+makeChatId(userInfo.getUserId(), receiverId)+"/chat");
                    int readCount = isReceiverSubscribed ? 0 : 1;

                    Chat chat = Chat.builder()
                            .chatId(makeChatId(userInfo.getUserId(), receiverId))
                            .msg(messageRequestDto.getContent())
                            .senderId(userInfo.getUserId())
                            .senderImg(userInfo.getUserImg())
                            .sender(userInfo.getUserName())
                            .receiverId(receiverId)
                            .receiverImg(receiverInfo.getUserImg())
                            .receiver(receiverInfo.getUserName())
                            .readCnt(readCount)
                            .createAt(LocalDateTime.now())
                            .exitUserId1(0L)
                            .exitUserId2(0L)
                            .build();
                    return chatRepository.save(chat)
                            .map(savedChat -> new MessageResponseDto(savedChat.getId(), userInfo.getUserId(), userInfo.getUserImg(), savedChat.getSender(), savedChat.getMsg()));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Receiver does not exist.")));

    }

    public String makeChatId(Long receiverId, Long senderId) {
        return senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" +senderId;
    }

//    // 메시지 조회 요청 처리 메서드
//    @MessageMapping("/chats/{chatId}/history")
//    public void fetchChatHistory(@DestinationVariable String chatId, Principal principal) {
//        if (principal == null) {
//            log.warn("Authentication required to access chat history.");
//            return;
//        }
//
//        String sessionId = principal.getName();
//        UserInfo userInfo = webSocketRoomUserSessionMapper.getUserInfoBySessionId(sessionId);
//        if (userInfo == null) {
//            log.error("User info not found for sessionId: {}", sessionId);
//            return;
//        }
//        String userName = userInfo.getUserName();
//
//        chatRepository.findByChatIdOrderByCreateAtDesc(chatId).collectList().subscribe(
//                messages -> {
//                    // 조회된 메시지들을 요청한 사용자에게만 실시간으로 전송
//                    messagingTemplate.convertAndSendToUser(
//                            userName,
//                            "/queue/chats/history",
//                            messages);
//                },
//                error -> {
//                    log.error("Error fetching chat history", error);
//                }
//        );
//    }

}
