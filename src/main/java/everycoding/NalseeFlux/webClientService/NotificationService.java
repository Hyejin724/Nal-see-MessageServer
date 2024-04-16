package everycoding.NalseeFlux.webClientService;

import everycoding.NalseeFlux.chat.ChatDto;
import everycoding.NalseeFlux.dto.MessageEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NotificationService {

    private final WebClient webClient;

    public NotificationService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("https://nalsee.site:8080/").build();
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/").build();
    }

    public Mono<Void> sendEvent(MessageEventDto message) {
        log.info("NotificationService 진입, 메시지: {}", message);
        return webClient.post()
                .uri("/msg-alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(unused -> log.info("메시지 성공적으로 전송됨"))
                .doOnError(error -> log.error("요청 처리 중 에러 발생", error));
    }

//    public Mono<Boolean> checkOnlineUser(Long userId) {
//        return webClient.post()
//                .uri("/online")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(userId)
//                .retrieve()
//                .bodyToMono(Boolean.class);
//    }

}
