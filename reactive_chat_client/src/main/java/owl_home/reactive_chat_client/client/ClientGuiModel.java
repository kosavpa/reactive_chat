package owl_home.reactive_chat_client.client;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;


@Data
public class ClientGuiModel {
    private Set<String> allUserNames = new HashSet<>();
    private String newMessage;

    public Set<String> getAllUserNames() {
        return Collections.unmodifiableSet(allUserNames);
    }

    public void addUser(String newUserName){
        allUserNames.add(newUserName);
    }

    public void deleteUser(String deletedUser){
        allUserNames.remove(deletedUser);
    }
}
