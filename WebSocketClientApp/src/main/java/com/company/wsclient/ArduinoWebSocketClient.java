package com.company.wsclient;

import com.company.wsclient.frames.MainAppFrame;
import de.craften.ui.swingmaterial.MaterialButton;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


public class ArduinoWebSocketClient extends WebSocketClient {

    private final JTextArea logArea;

    private final MaterialButton connectButton;

    private final MaterialButton sendButton;

    private final JTextField uriField;

    private final JComboBox<? extends Draft> draft;

    private final MaterialButton closeButton;

    public ArduinoWebSocketClient(MainAppFrame mainAppFrame) throws URISyntaxException {
        super(new URI(mainAppFrame.getUriField().getText()),
                (Draft) Objects.requireNonNull(mainAppFrame.getDraft().getSelectedItem()));
        this.logArea = mainAppFrame.getLogArea();
        this.connectButton = mainAppFrame.getConnectButton();
        this.sendButton = mainAppFrame.getSendButton();
        this.uriField = mainAppFrame.getUriField();
        this.draft = mainAppFrame.getDraft();
        this.closeButton = mainAppFrame.getCloseButton();
    }

    @Override
    public void onMessage(String message) {
        logArea.append("got: " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        logArea.append("You are connected to Parking App Server: " + getURI() + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logArea.append("You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
        connectButton.setEnabled(true);
        sendButton.setEnabled(false);
        uriField.setEditable(true);
        draft.setEditable(true);
        closeButton.setEnabled(false);
    }

    @Override
    public void onError(Exception ex) {
        logArea.append("Exception occurred ...\n" + ex + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
        ex.printStackTrace();
        connectButton.setEnabled(true);
        uriField.setEditable(true);
        draft.setEditable(true);
        closeButton.setEnabled(false);
    }
}
