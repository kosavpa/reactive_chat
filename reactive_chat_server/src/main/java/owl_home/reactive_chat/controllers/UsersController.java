package owl_home.reactive_chat.controllers;


import java.util.HashSet;
import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;


@Controller
public class UsersController {
    public static Set<String> users = new HashSet<>();

    @MessageMapping("users-list")
    public Flux<String> usersList(){        
        return Flux.fromIterable(users);
    }
}
