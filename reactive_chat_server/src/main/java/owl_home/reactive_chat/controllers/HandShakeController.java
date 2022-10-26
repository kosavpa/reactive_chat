package owl_home.reactive_chat.controllers;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import owl_home.reactive_chat.entity.Message;
import owl_home.reactive_chat.entity.MessageType;

import reactor.core.publisher.Mono;


@Controller
public class HandShakeController {
    private RabbitTemplate template;

    @Autowired
    public HandShakeController(RabbitTemplate template) {
        this.template = template;
    }

    @MessageMapping("handshake")    
    public Mono<Message> handShakeWithUser(Mono<Message> usernameFlow){
        return usernameFlow
                    .map(message -> {
                        if(message != null & message.getUsername() != null & !message.getUsername().isEmpty() & UsersController.users.add(message.getUsername())){
                            message = new Message(MessageType.NAME_ACCEPTED, null, message.getUsername());
                        } else {
                            message = new Message(MessageType.USER_NAME_ERROR, null, null);

                            return message;
                        }

                        template.convertAndSend("r_chat", "", new Message(MessageType.USER_ADDED, null, message.getUsername()));

                        return message;
                    });
    }
}