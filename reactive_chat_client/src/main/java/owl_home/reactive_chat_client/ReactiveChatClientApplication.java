package owl_home.reactive_chat_client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import owl_home.reactive_chat_client.client.ClientGuiController;


@SpringBootApplication
public class ReactiveChatClientApplication {

	static {
		System.setProperty("java.awt.headless", "false");
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ReactiveChatClientApplication.class, args);

		context.getBean("clientGuiController", ClientGuiController.class).clientHandshake();
	}

}
