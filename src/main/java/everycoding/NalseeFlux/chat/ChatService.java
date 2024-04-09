package everycoding.NalseeFlux.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Flux<Chat> getLatestChatsWithUserId(Long userId) {
        // 모든 Chat을 가져온 후, Flux를 활용하여 처리
        return chatRepository.findAll()
                .filter(chat -> chat.getChatId().contains(String.valueOf(userId))) // userId가 포함된 chatId를 가진 Chat만 필터링
                .groupBy(Chat::getChatId) // chatId별로 그룹핑
                .flatMap(Flux::last) // 각 그룹별 마지막 Chat (가장 최근의 Chat)만 선택
                .distinct(Chat::getChatId); // 중복 제거 (이미 groupBy로 그룹핑된 상태라 필요 없을 수도 있음)
    }
}