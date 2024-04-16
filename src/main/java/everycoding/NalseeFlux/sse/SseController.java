//package everycoding.NalseeFlux.sse;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.UUID;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//public class SseController {
//
//    private final SseEmitterService sseEmitterService;
//
//    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public ResponseEntity<SseEmitter> connect() {
//        String sseId = UUID.randomUUID().toString();
//        SseEmitter sseEmitter = sseEmitterService.subscribe(sseId);
//        return ResponseEntity.ok(sseEmitter);
//    }
//
//    @PostMapping(value = "/broadcast")
//    public ResponseEntity<Void> broadcast(@RequestBody EventPayload eventPayload) {
//        sseEmitterService.broadcast(eventPayload);
//        return ResponseEntity.ok().build();
//    }
//
//}
