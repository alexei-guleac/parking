package com.company.wsclient;

import com.company.wsclient.frames.MainAppFrame;

import static com.company.wsclient.constants.Constants.wsHost;


public class DesktopWebSocketApp {

    public static void main(String[] args) {
        String location;
        if (args.length != 0) {
            location = args[0];
            System.out.println("Default server url specified: '" + location + "'");
        } else {
            location = wsHost;
            System.out.println("Default server url not specified: defaulting to '" + location + "'");
        }

        new MainAppFrame(location);
    }

}
