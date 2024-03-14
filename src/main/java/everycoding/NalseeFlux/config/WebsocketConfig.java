//package everycoding.NalseeFlux.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
//import reactor.core.publisher.Sinks;
//
//import java.util.Map;
//
//@Configuration
//public class WebsocketConfig {
//
//    @Bean
//    public SimpleUrlHandlerMapping handlerMapping(WebSocketHandler wsh) {
//        return new SimpleUrlHandlerMapping(Map.of("/ws-chat", wsh), 1);
//    }
//
//    @Bean
//    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
//        return new WebSocketHandlerAdapter();
//    }
//
//    @Bean
//    public Sinks.Many<String> sink() {
//        return Sinks.many().multicast().directBestEffort();
//    }
//}
