package owl_home.reactive_chat.controllers;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import owl_home.reactive_chat.entity.Message;
import owl_home.reactive_chat.entity.MessageType;
import reactor.core.publisher.Mono;


@Controller
public class MessageController {
    private RabbitTemplate template;

    @Autowired
    public MessageController(RabbitTemplate template){
        this.template = template;
    }

    @MessageMapping("chat")
    public Mono<Void> receiveMessage(Mono<Message> messageFlow){        
        return messageFlow
                    .doOnNext(message -> {

                        if(message.getType() == MessageType.USER_REMOVED)
                            UsersController.users.remove(message.getUsername());

                        template.convertAndSend("r_chat", "", message);
                        
                    })
                    .thenEmpty(Mono.empty());
    }
}
