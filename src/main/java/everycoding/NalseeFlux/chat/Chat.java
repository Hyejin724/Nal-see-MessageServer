package everycoding.NalseeFlux.chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "chat")
public class Chat {
    @Id
    private String id;
    private String chatId;
    private String msg;
    private Long senderId;
    private String sender;
    private String senderImg;
//    private String receiver;
//    private String receiverImg;
    private LocalDateTime createAt;

    @Builder

    public Chat(String chatId, String msg, Long senderId, String sender, String senderImg, LocalDateTime createAt) {
        this.id = UUID.randomUUID().toString();
        this.chatId = chatId;
        this.msg = msg;
        this.senderId = senderId;
        this.sender = sender;
        this.senderImg = senderImg;
//        this.receiver = receiver;
//        this.receiverImg = receiverImg;
        this.createAt = createAt;
    }
}
