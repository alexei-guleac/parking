package com.company.wsclient.components.listeners;

import com.company.wsclient.ArduinoWebSocketClient;
import com.company.wsclient.constants.Messages;
import com.company.wsclient.frames.MainAppFrame;
import de.craften.ui.swingmaterial.MaterialButton;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URISyntaxException;

import static com.company.wsclient.resources.ResourceBundleProvider.getString;


public class AppActionListener {

    private final MaterialButton connectButton;

    private final MaterialButton sendButton;

    private final JTextField uriField;

    private final JComboBox<? extends Draft> draft;

    private final MaterialButton closeButton;

    private MainAppFrame mainAppFrame;

    private JComboBox<? extends Integer> lotNumber;

    private JComboBox<?> lotStatus;

    private WebSocketClient webSocketClient;

    private JTextField messageField;


    public AppActionListener(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        this.webSocketClient = mainAppFrame.getWebSocketClient();
        this.connectButton = mainAppFrame.getConnectButton();
        this.sendButton = mainAppFrame.getSendButton();
        this.uriField = mainAppFrame.getUriField();
        this.draft = mainAppFrame.getDraft();
        this.closeButton = mainAppFrame.getCloseButton();
        this.lotNumber = mainAppFrame.getLotNumber();
        this.lotStatus = mainAppFrame.getLotStatus();
        this.messageField = mainAppFrame.getMessageField();
    }

    public void connectButtonActionPerformed(ActionEvent e) {
        try {
            webSocketClient = new ArduinoWebSocketClient(mainAppFrame);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        connectButton.setEnabled(false);
        closeButton.setEnabled(true);
        sendButton.setEnabled(true);
        uriField.setEditable(false);
        draft.setEditable(false);
        webSocketClient.connect();
    }

    public void closeButtonActionPerformed(ActionEvent e) {
        webSocketClient.close();
        messageField.setText(getString("MainFrame.messageField.text"));
    }

    public void sendButtonActionPerformed(ActionEvent e) {
        if (webSocketClient != null) {
            webSocketClient.send(Messages.wsMessagePart1 + lotNumber.getSelectedItem()
                    + Messages.wsMessagePart2 + lotStatus.getSelectedItem() + Messages.wsMessagePart3);
            messageField.setText("");
            messageField.requestFocus();
        }
    }

    public void messageFieldActionPerformed(ActionEvent actionEvent) {
        if (webSocketClient != null) {
            webSocketClient.send(messageField.getText());
            messageField.setText("");
            messageField.requestFocus();
        }
    }
}
