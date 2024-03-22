package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.config.websocket.WebSocketRoomUserSessionMapper;
import everycoding.NalseeFlux.dto.MessageRequestDto;
import everycoding.NalseeFlux.dto.MessageResponseDto;
import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.service.AuthenticationService;
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
import java.util.Date;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final ChatRepository chatRepository;
    private final AuthenticationService authenticationService;
    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;

    @MessageMapping("/chat")
    @SendToUser("/sub/messages")
    public Mono<MessageResponseDto> message(MessageRequestDto messageRequestDto, Principal principal) {
        String sessionId = principal.getName(); // WebSocket 연결 시 설정된 세션 ID 또는 사용자 식별자 사용
        log.info("sessionId={}", sessionId);
        UserInfo userInfo = webSocketRoomUserSessionMapper.getUserInfoBySessionId(sessionId);
        Long userId = 1L;
        Long receiverId = 2L;
        log.info("userId={}", userId);

        return authenticationService.checkUserExistence(receiverId)
                .flatMap(isReceiverExists -> {
                    if (isReceiverExists) {
                        Chat chat = new Chat();
                        chat.setChatId(UUID.randomUUID().toString());
                        chat.setMsg(messageRequestDto.getContent());
                        chat.setSender(userInfo.getUserName());
                        chat.setReceiver(messageRequestDto.getReceiverName());
                        return chatRepository.save(chat)
                                .map(savedChat -> new MessageResponseDto(savedChat.getId(), userId, savedChat.getReceiver(), savedChat.getMsg()));
                    } else {
                        return Mono.error(new RuntimeException("Receiver does not exist."));
                    }
                });
    }
}
