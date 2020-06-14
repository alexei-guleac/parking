package com.isd.parking.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static com.isd.parking.web.rest.ApiEndpoints.frontWSTopic;
import static org.springframework.messaging.simp.SimpMessageType.*;


@Configuration
public class WebSocketAuthorizationSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // You can customize your authorization mapping here.
        messages
            .nullDestMatcher().permitAll()
            .simpTypeMatchers(MESSAGE, SUBSCRIBE, CONNECT, UNSUBSCRIBE, DISCONNECT, HEARTBEAT).permitAll()
            .simpSubscribeDestMatchers(frontWSTopic + "/messages").permitAll()
            .simpDestMatchers("/**").permitAll()
            .anyMessage().permitAll();
        // messages.anyMessage().permitAll();
    }

    // TODO: For test purpose (and simplicity) disabled CSRF, but should re-enable this and provide a CSRF endpoint.
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
