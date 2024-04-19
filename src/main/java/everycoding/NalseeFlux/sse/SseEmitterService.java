//package everycoding.NalseeFlux.sse;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@Slf4j
//public class SseEmitterService {
//
//    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
//    private static final long TIMEOUT = 60 * 1000;
//    private static final long RECONNECTION_TIMEOUT = 1000L;
//
//    public SseEmitter subscribe(String id) {
//        SseEmitter emitter = createEmitter();
//
//        emitter.onTimeout(() -> {
//            log.info("server sent event timed out : id {}", id);
//            emitter.complete();
//        });
//
//        emitter.onError(e -> {
//            log.info("server sent event error occurred : id={}. message={}", id, e.getMessage());
//            emitter.complete();
//        });
//
//        emitter.onCompletion(() -> {
//            if (emitterMap.remove(id) != null) {
//                log.info("server sent event removed in emitter cache: id={}", id);
//            }
//            log.info("disconnected by completed server sent event: id={}", id);
//        });
//
//        emitterMap.put(id, emitter);
//
//        try {
//            SseEmitter.SseEventBuilder event = SseEmitter.event()
//                    .name("event example")
//                    .id(String.valueOf("id-1"))
//                    .data("SSE connected")
//                    .reconnectTime(RECONNECTION_TIMEOUT);
//            emitter.send(event);
//        } catch (IOException e) {
//            log.error("failure sen media position data, id={}, {}", id, e.getMessage());
//        }
//        return emitter;
//    }
//
//    public void broadcast(EventPayload eventPayload) {
//        emitterMap.forEach((id, emitter) -> {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("broadcast event")
//                        .id("broadcast event 1")
//                        .reconnectTime(RECONNECTION_TIMEOUT)
//                        .data(eventPayload, MediaType.APPLICATION_JSON));
//                log.info("sended notification, id={}, payload={}", id, eventPayload);
//            } catch (IOException e) {
//                log.error("fail to send emitter id={}, {}", id, e.getMessage());
//            }
//        });
//    }
//
//    private SseEmitter createEmitter() {
//        return new SseEmitter(TIMEOUT);
//    }
//}
