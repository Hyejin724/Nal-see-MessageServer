package everycoding.NalseeFlux.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Controller
//@Slf4j
//public class MessageController {
//
//    @MessageMapping("/chat")
//    @SendToUser("/topic/messages")
//    public OutputMessage getPrivatedMassage(final Message message, final Principal principal) throws InterruptedException {
//        log.info("message from: {}({}), text: {}", message.getFrom(), principal.getName(), message.getText());
//
//        String time = new SimpleDateFormat("HH:mm").format(new Date());
//        Message sendMsg = new Message();
//        sendMsg.setFrom("server");
//        sendMsg.setText("Hello, " + message.getFrom() + "!");
//
//        return new OutPutMessage(sendMsg.getFrom(), sendMsg,getText(), time);
//
//    }
//}
