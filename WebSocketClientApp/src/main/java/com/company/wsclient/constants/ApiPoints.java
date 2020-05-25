package com.company.wsclient.constants;

public class ApiPoints {

    // public static final String wsHost = "localhost";
    public static final String wsHost = "68.183.149.220";       // Digital Ocean parking-app droplet's IP

    public static final String wsPort = "8080";

    public static final String wsEndpoint = "/demo";

    public static final String wsUrl = "ws://" + wsHost + ":" + wsPort + wsEndpoint;
}
