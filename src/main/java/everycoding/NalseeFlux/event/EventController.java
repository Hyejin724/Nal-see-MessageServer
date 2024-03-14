package everycoding.NalseeFlux.event;

import everycoding.NalseeFlux.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;

    @PostMapping("/events")
    public Mono<ResponseEntity<Void>> handleEvent(@RequestBody EventDto eventDto, ServerWebExchange exchange) {
        UserInfo userInfo = exchange.getAttribute("userInfo");
        return eventRepository.save(Event.builder()
                        .receiverId(eventDto.getReceiverId())
                        .message(eventDto.getMessage())
                        .senderId(eventDto.getSenderId())
                        .senderImage(eventDto.getSenderImage())
                        .createAt(eventDto.getCreateAt())
                        .build())
                .flatMap(event ->
                        // 저장된 이벤트를 수신자에게 전송
                        eventService.sendNotificationToUser(event.getReceiverId(), event.getMessage())
                )
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED)));
    }

    @GetMapping("/events/{receiverId}")
    public Flux<Event> getUserEvents(@PathVariable Long receiverId,
                                     @RequestParam(value = "afterId", required = false) String afterId,
                                     Pageable pageable) {
        return eventRepository.findWithInfiniteScroll(receiverId, afterId, pageable);
    }
}