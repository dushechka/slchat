package client.view.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

import static main.SLChat.SLClient;
import static main.SLChat.IS_CLIENT_RUNNING;

/**
 * Handles chat window operations.
 */
public class ChatController {
    @FXML TextField textField;
    @FXML TextArea textArea;

    @FXML
    protected void handleSendButtonAction(ActionEvent event) {
        try {
            String msg = "";
            if (IS_CLIENT_RUNNING) {
                msg = textField.getText();
                textField.setText("");
                textArea.appendText(msg);
                SLClient.sendMessage(msg);
            }
        } catch (IOException exc) {
            System.out.println("Can't send message!");
            exc.printStackTrace();
        }
    }
}