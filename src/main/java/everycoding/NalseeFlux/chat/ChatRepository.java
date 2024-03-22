package everycoding.NalseeFlux.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Flux<Chat> findByChatIdOrderByCreateAtDesc(String chatId);
    Mono<Chat> findFirstByChatIdOrderByCreateAtDesc(String chatId);
    // 필요한 추가 쿼리 메서드를 정의할 수 있습니다.
}