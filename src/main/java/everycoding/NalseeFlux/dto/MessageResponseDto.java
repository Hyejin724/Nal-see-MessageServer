package everycoding.NalseeFlux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageResponseDto {
    private String id;
    private Long senderId;
    private String name;
    private String msg;
}
