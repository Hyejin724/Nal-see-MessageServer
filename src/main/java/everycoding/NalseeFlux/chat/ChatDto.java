package everycoding.NalseeFlux.chat;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatDto {

    private Chat chat;
    private Boolean isOnline;

    @Builder
    public ChatDto(Chat chat, Boolean isOnline) {
        this.chat = chat;
        this.isOnline = isOnline;
    }
}
