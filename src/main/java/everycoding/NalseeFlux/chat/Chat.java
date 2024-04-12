package everycoding.NalseeFlux.chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Long receiverId;
    private String receiver;
    private String receiverImg;
    @Setter
    private Integer readCnt;

    private LocalDateTime createAt;

    @Builder

    public Chat(String chatId, String msg, Long senderId, String sender, String senderImg, Long receiverId, String receiver, String receiverImg, Integer readCnt, LocalDateTime createAt) {
        this.id = UUID.randomUUID().toString();
        this.chatId = chatId;
        this.msg = msg;
        this.senderId = senderId;
        this.sender = sender;
        this.senderImg = senderImg;
        this.receiverId = receiverId;
        this.receiver = receiver;
        this.receiverImg = receiverImg;
        this.readCnt = readCnt;
        this.createAt = createAt;
    }
}
