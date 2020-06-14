package com.isd.parking.web.websocket;


import com.isd.parking.web.websocket.arduino.ArduinoWebSocketHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.isd.parking.web.rest.ApiEndpoints.arduinoWS;

/**
 * Class used to configure a WebSocketHandler
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
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
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, arduinoWS);
    }
}
