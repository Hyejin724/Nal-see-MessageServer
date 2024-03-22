package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.chat.Chat;
import everycoding.NalseeFlux.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Mono<Chat> saveMessage(Chat chat) {
        // sender와 receiver의 아이디를 알파벳 순으로 정렬하여 chatId 생성
        String[] ids = {chat.getSender(), chat.getReceiver()};
        Arrays.sort(ids);
        String chatId = String.join("", ids);
        chat.setChatId(chatId);

        // 현재 시간을 메시지 생성 시간으로 설정
        chat.setCreateAt(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    public Flux<Chat> getMessages(String chatId) {
        return chatRepository.findByChatIdOrderByCreateAtDesc(chatId);
    }
}
