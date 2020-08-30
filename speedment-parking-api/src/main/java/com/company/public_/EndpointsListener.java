package com.company.public_;

import com.speedment.common.logger.Logger;
import com.speedment.common.logger.LoggerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/**
 * View all app endpoints (for testing purposes)
 */
@Component
public class EndpointsListener {

    final Logger logger = LoggerManager.getLogger(EndpointsListener.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods().forEach((key, value) -> logger.info("{" + key + " ," + value + "}"));
    }
}
