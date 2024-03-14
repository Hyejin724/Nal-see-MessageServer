package everycoding.NalseeFlux.event;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventService {

    // 사용자 ID(Long)를 키로 하고, 해당 사용자의 SSE Emitter를 값으로 하는 맵
    private final Map<Long, Sinks.Many<ServerSentEvent<String>>> userNotificationSinks = new ConcurrentHashMap<>();

    public Flux<ServerSentEvent<String>> getNotificationsForUser(Long userId) {
        return userNotificationSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux();
    }

    public Mono<Void> sendNotificationToUser(Long userId, String message) {
        Sinks.Many<ServerSentEvent<String>> sink = userNotificationSinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(ServerSentEvent.builder(message).build());
        }
        return Mono.empty();
    }
}


