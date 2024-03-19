package everycoding.NalseeFlux.config.websocket;

import everycoding.NalseeFlux.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            // 송신 메시지 처리 시 인증된 사용자 정보 확인
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            UserInfo userInfo = (UserInfo) sessionAttributes.get("user");
            if (userInfo == null) {
                log.error("User is not authenticated");
                // 인증되지 않은 경우 메시지를 차단하거나 에러 처리
                return null; // 메시지 전송 차단
            }
            // 인증된 사용자에 대한 추가 처리가 필요한 경우 여기에 로직 추가
        }
        return message; // 인증된 사용자인 경우 메시지 전송 허용
    }
}