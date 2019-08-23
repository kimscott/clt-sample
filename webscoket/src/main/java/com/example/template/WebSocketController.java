package com.example.template;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Gson gson = new Gson();

    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public String processMessageFromClient(
            @Payload String message,
            Principal principal) throws Exception {

        System.out.println("==========================11111");
        System.out.println(message);
        System.out.println(principal.getName());

        return gson
                .fromJson(message, Map.class)
                .get("name").toString();
    }

    @MessageMapping("/message2")
    @SendTo("/topic/reply")
    public String processMessageFromClient2(
            @Payload String message,
            Principal principal) throws Exception {

        System.out.println("==========================2222");
        System.out.println(message);
        System.out.println(principal.getName());

        return gson
                .fromJson(message, Map.class)
                .get("name").toString();
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }


    @KafkaListener(topics = "${topic.topicName}")
    public void onMessage(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

        System.out.println(message);
        messagingTemplate.convertAndSendToUser("kook","/queue/reply",message);
    }


}