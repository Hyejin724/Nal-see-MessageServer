package everycoding.NalseeFlux.config.websocket;

import everycoding.NalseeFlux.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketRoomUserSessionMapper {
    // 세션 ID와 userId의 매핑을 저장하는 Map
    private final ConcurrentHashMap<String, UserInfo> sessionUserIdMap = new ConcurrentHashMap<>();

    // 새로운 세션을 매핑에 추가하는 메소드
    public void registerSession(String sessionId, UserInfo userInfo) {
        sessionUserIdMap.put(sessionId, userInfo);
    }

    // 세션 종료 시 매핑에서 제거하는 메소드
    public void removeSession(String sessionId) {
        sessionUserIdMap.remove(sessionId);
    }


    // 현재 연결된 모든 사용자의 userId 목록을 반환하는 메소드
    public List<UserInfo> getAllConnectedUserIds() {
        return Collections.list(sessionUserIdMap.elements());
    }

    public UserInfo getUserInfoBySessionId(String sessionId) {
        return sessionUserIdMap.get(sessionId);
    }
}