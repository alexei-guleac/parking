/*
 * Created on Thu May 21 13:48:12 EEST 2020
 */

package com.company.wsclient.frames;

import com.company.wsclient.components.ColorCellComboboxRenderer;
import com.company.wsclient.components.RoundedBorder;
import com.company.wsclient.components.listeners.AppActionListener;
import com.company.wsclient.components.listeners.AppWindowAdapter;
import com.company.wsclient.constants.Locales;
import com.company.wsclient.resources.ResourceBundleProvider;
import de.craften.ui.swingmaterial.MaterialButton;
import de.craften.ui.swingmaterial.MaterialColor;
import de.craften.ui.swingmaterial.MaterialFrame;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.company.wsclient.constants.Constants.totalParkingLotsNumber;
import static com.company.wsclient.constants.LotStatuses.lotStatuses;
import static com.company.wsclient.resources.ResourceBundleProvider.getString;


@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class MainAppFrame extends MaterialFrame {

    private static final long serialVersionUID = -6056260699202978657L;

    private static final ArrayList<Integer> lotNumbers = new ArrayList<>();

    // Variables declaration //GEN-BEGIN:variables
    private JPanel pane;

    private JLabel numberLabel;

    private JLabel statusLabel;

    private JComboBox<String> languageList;

    private JComboBox<? extends Integer> lotNumber;

    private JComboBox<?> lotStatus;

    private MaterialButton connectButton;

    private MaterialButton sendButton;

    private MaterialButton closeButton;

    private JLabel connectionTypeLabel;

    private JScrollPane scrollPane;

    private JComboBox<? extends Draft> draft;

    private JLabel hostLabel;

    private JTextField uriField;

    private JLabel logLabel;

    private JScrollPane scrollPane1;

    private JTextArea logArea;
    // End of variables declaration  //GEN-END:variables

    private JLabel messageLabel;

    private JTextField messageField;

    private WebSocketClient webSocketClient;

    private AppActionListener app;

    public MainAppFrame(String defaultLocation) {
        super();
        fillLotNumbers();
        initUI(defaultLocation);
    }

    private void initUI(String defaultLocation) {
        // Component initialization //GEN-BEGIN:initUI
        initComponents();

        final int padding = 8;
        final Insets commonInsets = new Insets(padding, padding, padding, padding);
        final Insets commonZeroInsets = new Insets(0, padding, 0, padding);
        final double commonWeightX = 0.5;
        final double commonWeightY = 0.0;

        app = new AppActionListener(this);

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        pane = new JPanel(new GridBagLayout());

        //---- numberLabel ----
        numberLabel.setText(getString("MainFrame.numberLabel.text"));
        pane.add(numberLabel, new GridBagConstraints(0, 0, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                commonInsets, 0, 0));

        //---- statusLabel ----
        statusLabel.setText(getString("MainFrame.statusLabel.text"));
        pane.add(statusLabel, new GridBagConstraints(1, 0, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                commonInsets, 0, 0));

        //---- languageList ----
        languageList.addActionListener(this::languageChanged);
        for (String s : Locales.languages.keySet()) {
            languageList.addItem(s);
        }
        languageList.setMaximumSize(new Dimension(100, 20));
        languageList.setPreferredSize(new Dimension(80, 20));
        languageList.setMinimumSize(new Dimension(50, 20));
        final GridBagConstraints constraintsLang = new GridBagConstraints(2, 0, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                commonInsets, 0, 0);
        constraintsLang.anchor = GridBagConstraints.LINE_END;
        pane.add(languageList, constraintsLang);

        //---- lotNumber ----
        // add lots dropdown lists
        lotNumber.setToolTipText(getString("lot.number.tooltip"));
        pane.add(lotNumber, new GridBagConstraints(0, 1, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- lotStatus ----
        lotStatus.setToolTipText(getString("lot.status.tooltip"));
        lotStatus.setRenderer(new ColorCellComboboxRenderer());
        lotStatus.setSelectedItem(0);
        pane.add(lotStatus, new GridBagConstraints(1, 1, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- connectButton ----
        setupConnectButton();
        connectButton.setText(getString("MainFrame.connectButton.text"));
        connectButton.setToolTipText(getString("connect.button.tooltip"));
        pane.add(connectButton, new GridBagConstraints(2, 1, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- sendButton ----
        setupSendButton();
        sendButton.setText(getString("MainFrame.sendButton.text"));
        sendButton.setToolTipText(getString("send.button.tooltip"));
        pane.add(sendButton, new GridBagConstraints(0, 2, 2, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- closeButton ----
        setupCloseButton();
        closeButton.setText(getString("MainFrame.closeButton.text"));
        closeButton.setToolTipText(getString("close.button.tooltip"));
        pane.add(closeButton, new GridBagConstraints(2, 2, 1, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- connectionTypeLabel ----
        connectionTypeLabel.setText(getString("MainFrame.connectionTypeLabel.text"));
        pane.add(connectionTypeLabel, new GridBagConstraints(0, 3, 3, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //======== draft ========
        draft.setToolTipText(getString("draft.tooltip"));
        pane.add(draft, new GridBagConstraints(0, 4, 3, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- hostLabel ----
        hostLabel.setText(getString("MainFrame.hostLabel.text"));
        pane.add(hostLabel, new GridBagConstraints(0, 5, 3, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- uriField ----
        uriField.setText(defaultLocation);
        uriField.setToolTipText(getString("host.tooltip"));
        pane.add(uriField, new GridBagConstraints(0, 6, 3, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //---- logLabel ----
        logLabel.setText(getString("MainFrame.logLabel.text"));
        pane.add(logLabel, new GridBagConstraints(0, 7, 3, 1, commonWeightX, commonWeightY,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 0));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(logArea);
        }
        //======== logArea ========
        pane.add(scrollPane1, new GridBagConstraints(0, 8, 3, 1, commonWeightX, 2.8,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 250));

        //---- messageLabel ----
        messageLabel.setText(getString("MainFrame.messageLabel.text"));
        pane.add(messageLabel, new GridBagConstraints(0, 9, 3, 1, commonWeightX, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonZeroInsets, 0, 20));

        //---- messageField ----
        messageField.setText(getString("MainFrame.messageField.text"));
        messageField.addActionListener(app::messageFieldActionPerformed);
        messageField.setToolTipText(getString("message.field.tooltip"));
        pane.add(messageField, new GridBagConstraints(0, 10, 3, 1, commonWeightX, 1.5,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                commonInsets, 0, 70));

        sendButton.setEnabled(false);

        //---- bind pane to window ----
        final GridBagConstraints constraints = new GridBagConstraints(0, 0, 3, 1, commonWeightX, 1.5,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                commonInsets, 0, 70);
        constraints.anchor = GridBagConstraints.PAGE_END; //bottom of space
        contentPane.add(pane, constraints);

        initFrame();
    }

    private void initFrame() {
        setFrameDimensions();
        addWindowListener(new AppWindowAdapter(this));
        setupCursor();
        setupFont("Arial", Font.PLAIN, 14);
        setTitle(getString("app.name"));

        ImageIcon imageIcon = loadIcon("/favicon-light.png");
        // log.info(String.valueOf(imageIcon));
        if(imageIcon != null)
            setIconImage(imageIcon.getImage());
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    private ImageIcon loadIcon(String strPath)
    {
        URL imgURL = getClass().getResource(strPath);
        if(imgURL != null)
            return new ImageIcon(imgURL);
        else
            return null;
    }

    private void initComponents() {
        numberLabel = new JLabel();
        statusLabel = new JLabel();
        languageList = new JComboBox<>();
        lotNumber = new JComboBox<>(lotNumbers.toArray(new Integer[0]));
        lotStatus = new JComboBox<>(lotStatuses);
        connectButton = new MaterialButton();
        sendButton = new MaterialButton();
        closeButton = new MaterialButton();
        connectionTypeLabel = new JLabel();
        scrollPane = new JScrollPane();
        Draft[] drafts = {new Draft_6455()};
        draft = new JComboBox<>(drafts);
        hostLabel = new JLabel();
        uriField = new JTextField();
        logLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        logArea = new JTextArea();
        messageLabel = new JLabel();
        messageField = new JTextField();
    }

    private void setFrameDimensions() {
        Dimension d = new Dimension(700, 800);
        setPreferredSize(d);
        Dimension dMin = new Dimension(500, 720);
        setMinimumSize(dMin);
        setSize(d);
    }

    private void setupCursor() {
        setHandCursor(lotNumber, lotStatus, connectButton, draft, languageList, sendButton, closeButton);
    }

    private void setupFont(String fontFamilyName, int type, int fontSize) {
        final Font f = new Font(fontFamilyName, type, fontSize);
        final Component[] components = pane.getComponents();
        for (Component component : components) {
            component.setFont(f);
        }
        setFont(f);
    }

    private void setHandCursor(Component... component) {
        for (Component com : component) {
            com.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private void setDefaultCursor(Component... component) {
        for (Component com : component) {
            com.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void fillLotNumbers() {
        for (int i = 1; i <= totalParkingLotsNumber; i++) {
            lotNumbers.add(i);
        }
    }

    private void setupConnectButton() {
        connectButton.addActionListener(app::connectButtonActionPerformed);
        connectButton.setBorder(new RoundedBorder(50));       //10 is the radius
        connectButton.setForeground(MaterialColor.BLACK);
        connectButton.setBackground(MaterialColor.LIGHTGREENA_400);
    }

    private void setupSendButton() {
        sendButton.addActionListener(app::sendButtonActionPerformed);
        sendButton.setBorder(new RoundedBorder(30));       //10 is the radius
        sendButton.setForeground(MaterialColor.BLACK);
        sendButton.setBackground(MaterialColor.LIGHTBLUE_200);
    }

    private void setupCloseButton() {
        closeButton.addActionListener(app::closeButtonActionPerformed);
        closeButton.setBorder(new RoundedBorder(30));       //10 is the radius
        closeButton.setEnabled(false);
        closeButton.setForeground(MaterialColor.BLACK);
        closeButton.setBackground(MaterialColor.RED_400);
    }

    private void languageChanged(ActionEvent actionEvent) {
        final Locale locale = Locales.languages.get(
                Objects.requireNonNull(languageList.getSelectedItem()).toString());
        updateLanguage(locale);
    }

    private void updateLanguage(Locale locale) {
        // bundle = ResourceBundle.getBundle("strings", locale);
        ResourceBundleProvider.setLocale(locale);
        numberLabel.setText(getString("MainFrame.numberLabel.text"));
        statusLabel.setText(getString("MainFrame.statusLabel.text"));
        lotNumber.setToolTipText(getString("lot.number.tooltip"));
        lotStatus.setToolTipText(getString("lot.status.tooltip"));
        connectButton.setText(getString("MainFrame.connectButton.text"));
        connectButton.setToolTipText(getString("connect.button.tooltip"));
        sendButton.setText(getString("MainFrame.sendButton.text"));
        sendButton.setToolTipText(getString("send.button.tooltip"));
        closeButton.setText(getString("MainFrame.closeButton.text"));
        closeButton.setToolTipText(getString("close.button.tooltip"));
        connectionTypeLabel.setText(getString("MainFrame.connectionTypeLabel.text"));
        draft.setToolTipText(getString("draft.tooltip"));
        hostLabel.setText(getString("MainFrame.hostLabel.text"));
        uriField.setToolTipText(getString("host.tooltip"));
        logLabel.setText(getString("MainFrame.logLabel.text"));
        messageLabel.setText(getString("MainFrame.messageLabel.text"));
        messageField.setText(getString("MainFrame.messageField.text"));
        messageField.setToolTipText(getString("message.field.tooltip"));
        pack();
        setTitle(getString("app.name"));
    }
}
