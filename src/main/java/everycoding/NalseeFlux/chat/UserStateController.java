package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.config.websocket.WebSocketRoomUserSessionMapper;
import everycoding.NalseeFlux.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class UserStateController {

    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @SubscribeMapping("/chat-list")
    public void sendUserStateToChat() {
        // 채팅 서버에 연결되어 있는 유저 ID 목록을 가져오는 메소드
        List<UserInfo> userIds = webSocketRoomUserSessionMapper.getAllConnectedUserIds();

        messagingTemplate.convertAndSend(
                "/topic/chat", // "/chat" 화면에 전송될 주소
                userIds // 연결된 유저 ID 목록
        );
    }
}