package everycoding.NalseeFlux.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSession {
    private Long userId;
    private String name;
    private String email;
    private String roomId;
}
