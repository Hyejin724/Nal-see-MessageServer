package everycoding.NalseeFlux.event;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "event")
public class Event {
    @Id
    private String id;
    private String message;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createAt;
    private String senderImage;

    @Builder
    public Event(String id, String message, Long senderId, Long receiverId, LocalDateTime createAt, String senderImage) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createAt = createAt;
        this.senderImage = senderImage;
    }
}
