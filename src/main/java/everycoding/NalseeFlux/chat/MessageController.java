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

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final ChatRepository chatRepository;
    private final AuthenticationService authenticationService;
    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final NotificationService notificationService;

    @MessageMapping("/{chatId}/chat")
    @SendTo("/sub/{chatId}/chat")
    public Mono<MessageResponseDto> message(@DestinationVariable String chatId, MessageRequestDto messageRequestDto, Principal principal) {
        String sessionId = principal.getName(); // WebSocket 연결 시 설정된 세션 ID 또는 사용자 식별자 사용
        log.info("sessionId={}", sessionId);
        UserInfo userInfo = webSocketRoomUserSessionMapper.getUserInfoBySessionId(sessionId);
        Long userId = userInfo.getUserId();
        Long receiverId = messageRequestDto.getReceiverId();
        log.info("userId={}", userId);
        MessageEventDto build = MessageEventDto.builder()
                .receiverName(messageRequestDto.getReceiverName())
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
                    // UserInfo가 성공적으로 반환되면, 여기에서 채팅 메시지 처리를 진행합니다.
                    Chat chat = Chat.builder()
                            .chatId(makeChatId(userInfo.getUserId(), receiverId))
                            .msg(messageRequestDto.getContent())
                            .senderId(userInfo.getUserId())
                            .senderImg(userInfo.getUserImg())
                            .sender(userInfo.getUserName())
                            .createAt(LocalDateTime.now()).build();
                    return chatRepository.save(chat)
                            .map(savedChat -> new MessageResponseDto(savedChat.getId(), userInfo.getUserId(), savedChat.getSender(), savedChat.getMsg()));
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
