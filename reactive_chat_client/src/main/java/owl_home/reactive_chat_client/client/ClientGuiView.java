package owl_home.reactive_chat_client.client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGuiView {
    private final ClientGuiController controller;

    private JFrame frame = new JFrame("Чат");
    JTextField textField = new JTextField(50);
    JTextArea messageTextArea = new JTextArea(10, 50);
    JTextArea userTextArea = new JTextArea(10, 10);

    public ClientGuiView(ClientGuiController controller){
        this.controller = controller;
        initView();
    }

    private void initView(){
        textField.setEditable(false);
        messageTextArea.setEditable(false);
        userTextArea.setEditable(false);

        frame.getContentPane().add(textField, BorderLayout.NORTH);
        frame.getContentPane().add(messageTextArea, BorderLayout.WEST);
        frame.getContentPane().add(userTextArea, BorderLayout.EAST);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage(textField.getText());
                textField.setText("");
            }
        });
    }

    public void usernameErrorMessageDialog(){
        JOptionPane.showMessageDialog(
            frame,
            "Был введено некорректное имя пользователя. Попробуйте еще раз.",
            "Конфигурация клиента", JOptionPane.ERROR_MESSAGE
        );
    }

    public String getUserName() {
        return JOptionPane.showInputDialog(
            frame,
            "Введите ваше имя:", "Конфигурация клиента",
            JOptionPane.QUESTION_MESSAGE
        );
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        textField.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Соединение с сервером установлено",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Клиент не подключен к серверу",
                    "Чат",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void refreshMessages() {
        messageTextArea.append(controller.getModel().getNewMessage() + "\n");
    }

    public void refreshUsers() {
        ClientGuiModel model = controller.getModel();
        StringBuilder sb = new StringBuilder();

        for (String userName : model.getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        
        userTextArea.setText(sb.toString());
    }
}
