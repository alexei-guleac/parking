package com.company.wsclient.components.listeners;

import com.company.wsclient.frames.MainAppFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.company.wsclient.resources.ResourceBundleProvider.getString;


public class AppWindowAdapter extends WindowAdapter {

    private final MainAppFrame mainAppFrame;

    public AppWindowAdapter(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
    }

    @Override
    public void windowClosing(WindowEvent e) {

        Object[] choices = {getString("exit.program.message.ok"), getString("exit.program.message.cancel")};
        Object defaultChoice = choices[0];
        mainAppFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int confirmed = JOptionPane.showOptionDialog(
                null,
                getString("exit.the.program.confirm"),
                getString("exit.program.message.box"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                defaultChoice);
        if (confirmed == JOptionPane.YES_OPTION) {
            if (mainAppFrame.getWebSocketClient() != null) {
                mainAppFrame.getWebSocketClient().close();
            }
            mainAppFrame.dispose();
        } else {
            mainAppFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
}
