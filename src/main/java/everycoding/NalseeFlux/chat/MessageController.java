package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.config.websocket.WebSocketRoomUserSessionMapper;
import everycoding.NalseeFlux.dto.MessageEventDto;
import everycoding.NalseeFlux.dto.MessageRequestDto;
import everycoding.NalseeFlux.dto.MessageResponseDto;
import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.service.AuthenticationService;
import everycoding.NalseeFlux.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final ChatRepository chatRepository;
    private final AuthenticationService authenticationService;
    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final NotificationService notificationService;

    @MessageMapping("/chat")
    @SendToUser("/sub/messages")
    public Mono<MessageResponseDto> message(MessageRequestDto messageRequestDto, Principal principal) {
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
                            .msg(messageRequestDto.getContent())
                            .senderImg(userInfo.getUserImg())
                            .sender(userInfo.getUserName())
                            .receiver(receiverUserInfo.getUserName())
                            .receiverImg(receiverUserInfo.getUserImg())
                            .createAt(LocalDateTime.now()).build();
                    return chatRepository.save(chat)
                            .map(savedChat -> new MessageResponseDto(savedChat.getId(), userInfo.getUserId(), savedChat.getSender(), savedChat.getMsg()));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Receiver does not exist.")));
    }
}
