package owl_home.reactive_chat_client.client;


import java.util.HashSet;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import lombok.Data;
import owl_home.reactive_chat_client.entity.Message;
import owl_home.reactive_chat_client.entity.MessageType;


@Data
@Component
public class ClientGuiController {
    private ClientGuiModel model = new ClientGuiModel();
    private ClientGuiView view = new ClientGuiView(this);
    private RSocketRequester tcp;
    private String username;

    @Autowired
    public ClientGuiController(RSocketRequester tcp) {
        this.tcp = tcp;
    }

    public void clientHandshake(){
        tcp
            .route("handshake")
            .data(new Message(MessageType.USER_ADDED, null, view.getUserName()))
            .retrieveMono(Message.class)
            .doOnNext(handshakeResponce -> {
                
                if(handshakeResponce.getType() == MessageType.NAME_ACCEPTED){
                    this.username = handshakeResponce.getUsername();
                    notifyConnectionStatusChanged(true);
                    informAboutAddingNewUser(handshakeResponce);

                } else {
                    view.usernameErrorMessageDialog();
                    clientHandshake();
                }})
            .subscribe();

            requestUsersList();
    }

    private void requestUsersList() {
        List<String> userList = tcp
                                .route("users-list")
                                .retrieveFlux(String.class)
                                .collectList()
                                .block();

        model.setAllUserNames(new HashSet<>(userList));

        view.refreshUsers();
    }

    @RabbitListener(queues = "#{anonQueue.name}")
    public void clientMainLoop(Message message){
        switch(message.getType()){
            case TEXT -> processIncomingMessage(message);
            case USER_ADDED -> informAboutAddingNewUser(message);
            case USER_REMOVED -> informAboutDeletingNewUser(message);
        }
    }

    public void sendTextMessage(String text){
        tcp
            .route("chat")
            .data(new Message(MessageType.TEXT, text, this.username))
            .send()
            .subscribe();
    }

    public void notifyConnectionStatusChanged(boolean clientConnected){
        view.notifyConnectionStatusChanged(clientConnected);
    }

    public void informAboutDeletingNewUser(Message message){
        model.deleteUser(message.getUsername());
        view.refreshUsers();
    }

    public void informAboutAddingNewUser(Message message){
        model.addUser(message.getUsername());
        view.refreshUsers();
    }

    public void processIncomingMessage(Message message){
        StringBuilder builder = new StringBuilder();
        builder
            .append(message.getUsername())
            .append(":")
            .append("\n")
            .append(message.getData());

        model.setNewMessage(builder.toString());
        view.refreshMessages();
    }

    @PreDestroy
    private void disconect(){
        tcp
            .route("chat")
            .data(new Message(MessageType.USER_REMOVED, null, this.username))
            .send()
            .subscribe();
    }    
}