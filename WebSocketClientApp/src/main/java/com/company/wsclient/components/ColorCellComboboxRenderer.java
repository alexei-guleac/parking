package com.company.wsclient.components;

import javax.swing.*;
import java.awt.*;

import static com.company.wsclient.constants.LotStatuses.lotStatuses;
import static com.company.wsclient.constants.LotStatuses.lotStatusesColors;


public class ColorCellComboboxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;

            for (int i = 0; i < lotStatuses.length; i++) {
                if (value.equals(lotStatuses[i])) {
                    label.setBackground(lotStatusesColors[i]);
                    if (isSelected) {
                        list.setSelectionForeground(Color.BLACK);
                        list.setSelectionBackground(lotStatusesColors[i]);
                    }
                }
            }
            return label;
        }
        return component;
    }
}
