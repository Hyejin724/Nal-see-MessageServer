//package everycoding.NalseeFlux.notification;
//
//import everycoding.NalseeFlux.event.EventService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.codec.ServerSentEvent;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
//@RestController
//@RequiredArgsConstructor
//public class NotificationController {
//
//    private final EventService eventService;
//
//    @GetMapping("/notifications/{userId}")
//    public Flux<ServerSentEvent<String>> subscribeToNotifications(@PathVariable Long userId) {
//        return eventService.getNotificationsForUser(userId);
//    }
//}
