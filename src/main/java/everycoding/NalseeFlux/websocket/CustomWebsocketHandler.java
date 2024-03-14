//package everycoding.NalseeFlux.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.bson.json.JsonObject;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.publisher.Mono;
//import reactor.core.publisher.Sinks;
//
//@Component
//@Slf4j
//public class CustomWebsocketHandler implements WebSocketHandler {
//
//    private final Sinks.Many<String> sink;
//
//    public CustomWebsocketHandler(Sinks.Many<String> sink) {
//        this.sink = sink;
//    }
//
//
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
////        var output = session.receive()
////                .map(e -> e.getPayloadAsText())
////                .map(e -> {
////                    try {
////                        JsonObject json = new JsonObject(e);
////                        String username = json.getString("username");
////                        if (username.equals("")) username = "익명";
////                        String message = json.getString("message");
////                        return username + ": " + message;
////                    } catch (JSONExcepton ex) {
////                        ex.printStackTrace();
////                        return "메시지 처리 중 오류 발생";
////                    }
////                });
//
////        output.subscribe(s -> sink.emitNext(s, Sinks.EmitFailureHandler.FAIL_FAST));
//
//        return session.send(sink.asFlux().map(session::textMessage));
//    }
//}
