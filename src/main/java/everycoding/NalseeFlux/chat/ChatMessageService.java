//package everycoding.NalseeFlux.chat;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ChatMessageService {
//    private final ChatRepository chatRepository;
//
//    public Chat saveMessage(Chat chat) {
//        chat.setCreateAt(LocalDateTime.now());
//        return chatRepository.save(chat);
//    }
//
//    public List<Chat> getMessages(String chatId) {
//        // 특정 chatId에 해당하는 메시지 조회 로직 구현
//        return chatRepository.findAll(); // 예시, 실제 구현 필요
//    }
//}
