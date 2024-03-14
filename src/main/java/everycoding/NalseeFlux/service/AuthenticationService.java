package everycoding.NalseeFlux.service;

import everycoding.NalseeFlux.dto.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {
    private final WebClient webClient;

    public AuthenticationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Mono<Void> sendEvent(String message) {
        return webClient.post()
                .uri("/auth/info")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<UserInfo> getUserInfo(String accessToken) {
        return webClient.get()
                .uri("/userInfo") // 메인 서버에서 사용자 정보를 제공하는 엔드포인트
                .header(accessToken) // 헤더에 accessToken 포함
                .retrieve()
                .bodyToMono(UserInfo.class); // UserInfo 클래스로 응답을 받음
    }
}
