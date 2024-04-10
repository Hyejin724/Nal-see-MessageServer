package everycoding.NalseeFlux.config.websocket;

import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.webClientService.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final AuthenticationService authenticationService;

    // AuthenticationService 주입 (생성자 주입 또는 필드 주입을 사용하세요)
    public CustomHandshakeHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String authToken = extractAuthTokenFromCookies(request);
        if (authToken != null) {
            try {
                UserInfo userInfo = authenticationService.getUserInfo(authToken).block(); // 동기적 블록
                if (userInfo != null) {
                    attributes.put("user", userInfo); // 인증된 사용자 정보를 세션 attributes에 저장
                    String sessionId = UUID.randomUUID().toString();
                    return new Principal() {
                        @Override
                        public String getName() {
                            return sessionId;
                        }
                    };
                }
            } catch (Exception e) {
                log.error("Authentication failed", e);
                return null; // 인증 실패 시 null 반환
            }
        }
        return null; // 토큰이 없거나 인증이 실패한 경우
    }

    private String extractAuthTokenFromCookies(org.springframework.http.server.ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String cookieHeader = headers.getFirst(HttpHeaders.COOKIE);
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                if (cookie.startsWith("AccessToken=")) {
                    return cookie.substring("AccessToken=".length());
                }
            }
        }
        return null;
    }
}
