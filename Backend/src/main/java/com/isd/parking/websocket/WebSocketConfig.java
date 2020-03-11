package com.isd.parking.websocket;


import com.isd.parking.controller.arduino.ArduinoWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.isd.parking.controller.web.RestApiEndpoints.arduinoWS;

/**
 * Class used to configure a WebSocketHandler
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ArduinoWebSocketHandler handler;

    /**
     * Constructor
     *
     * @param handler - class which provides methods for handling messages from Arduino board
     */
    @Autowired
    public WebSocketConfig(ArduinoWebSocketHandler handler) {
        this.handler = handler;
    }

    /**
     * Method registers handler and specifies api entry point for Arduino messaging
     *
     * @param registry - standard WebSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, arduinoWS);
    }
}