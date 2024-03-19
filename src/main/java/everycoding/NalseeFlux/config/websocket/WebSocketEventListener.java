package everycoding.NalseeFlux.config.websocket;

import everycoding.NalseeFlux.chat.UserStateController;
import everycoding.NalseeFlux.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final UserStateController userStateController;

    @EventListener
    public void connect(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // 세션의 attributes에서 인증된 사용자 정보를 가져옵니다.
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        UserInfo userInfo = (UserInfo) sessionAttributes.get("user");

        if (userInfo != null) {
            log.info("입장 사용자={}", userInfo.getName());
            // 사용자 세션 정보를 관리하는 Mapper에 사용자 정보를 추가합니다.
            webSocketRoomUserSessionMapper.registerSession(headerAccessor.getSessionId(), userInfo.getUserId());
        } else {
            log.error("인증되지 않은 사용자의 연결 시도");
        }
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        log.info("퇴장 세션={}", sessionId);

        // 사용자 세션 정보를 관리하는 Mapper에서 사용자 세션을 제거합니다.
        webSocketRoomUserSessionMapper.removeSession(sessionId);

        // 여기에서는 특정 containerId 대신 일반적인 방식으로 상태를 전송합니다.
        // 예를 들면, 전체 채팅 참여자 목록을 업데이트할 수 있습니다.
        userStateController.sendUserStateToChat();
    }
}
