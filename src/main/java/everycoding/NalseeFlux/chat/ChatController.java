package everycoding.NalseeFlux.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 메시지 전송
    @MessageMapping("/send")
    public void sendMessage(@Payload Chat chat) {
        chatService.saveMessage(chat).subscribe(savedChat -> {
            messagingTemplate.convertAndSend("/topic/chat/" + savedChat.getChatId(), savedChat);
        });
    }

    // 채팅방의 메시지 목록 조회
    @GetMapping("/{chatId}")
    public ResponseEntity<Flux<Chat>> getMessages(@PathVariable String chatId) {
        return ResponseEntity.ok(chatService.getMessages(chatId));
    }
}
