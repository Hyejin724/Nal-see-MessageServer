package everycoding.NalseeFlux.chat;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    Flux<Chat> findByChatIdOrderByCreateAtAsc(String chatId);
//    @Tailable
//    Flux<Chat> findWithTailableCursorBy(); // Change stream을 위한 메서드
    Flux<Chat> findBySenderId(Long senderId);
    Flux<Chat> findByReceiverId(Long senderId);
    Flux<Chat> findByChatId(String chatId);
}