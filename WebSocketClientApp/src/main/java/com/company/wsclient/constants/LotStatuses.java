package com.company.wsclient.constants;

import java.awt.*;


public class LotStatuses {

    public static final String[] lotStatuses = {"FREE", "OCCUPIED", "RESERVED", "UNKNOWN"};

    public static final Color[] lotStatusesColors = {
            Color.GREEN,                                 //green
            Color.getHSBColor(0, 67.8f, 100),   //red
            Color.getHSBColor(54, 65.5f, 100),  //yellow
            Color.LIGHT_GRAY                             //gray
    };
}
