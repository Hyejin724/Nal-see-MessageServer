package everycoding.NalseeFlux.webClientService;

import everycoding.NalseeFlux.dto.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {
    private final WebClient webClient;

    public AuthenticationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://k9314c9500eb3a.user-app.krampoline.com/").build();
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/").build();
    }

    public Mono<UserInfo> getUserInfo(String accessToken) {
        return webClient.get()
                .uri("/user/info") // 메인 서버에서 사용자 정보를 제공하는 엔드포인트
                .header("Authorization", accessToken) // 헤더에 accessToken 포함
                .retrieve()
                .bodyToMono(UserInfo.class); // UserInfo 클래스로 응답을 받음
    }

    public Mono<UserInfo> checkUserExistence(Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/user/exist")
                        .queryParam("userId", userId)
                        .build())
                .retrieve() // 서버로부터의 응답을 검색
                .bodyToMono(UserInfo.class); // 응답 본문을 Boolean 타입으로 변환
    }

//    public Mono<UserInfo> getUserInfoById(Long userId) {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/user/info") // 메인 서버에서 사용자 정보를 제공하는 엔드포인트
//                        .queryParam("userId", userId)
//                        .build())
//                .retrieve()
//                .bodyToMono(UserInfo.class); // UserInfo 클래스로 응답을 받음
//    }
}
