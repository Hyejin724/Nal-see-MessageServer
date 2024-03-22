package everycoding.NalseeFlux.dto;

import everycoding.NalseeFlux.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchMessageResponseDto {
    private List<MessageResponseDto> prevMessageList;
    private Chat targetMessage;
    private List<MessageResponseDto> nextMessageList;
}
