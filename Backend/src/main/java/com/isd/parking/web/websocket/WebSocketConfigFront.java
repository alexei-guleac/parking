package com.isd.parking.web.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.isd.parking.web.rest.ApiEndpoints.frontWS;
import static com.isd.parking.web.rest.ApiEndpoints.frontWSTopic;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigFront implements WebSocketMessageBrokerConfigurer {

    @Value("${front.url}")
    private String frontUrl;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // These are endpoints the client can subscribes to.
        config.enableSimpleBroker(frontWSTopic);
        // Message received with one of those below destinationPrefixes will be automatically router to controllers @MessageMapping
        config.setApplicationDestinationPrefixes("/api");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Handshake endpoint
        registry.addEndpoint(frontWS).setAllowedOrigins(frontUrl).withSockJS();
    }
}
