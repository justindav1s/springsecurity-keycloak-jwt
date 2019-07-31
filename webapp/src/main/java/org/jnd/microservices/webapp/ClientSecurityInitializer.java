package org.jnd.microservices.webapp;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class ClientSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    public ClientSecurityInitializer() {
        super(SecurityConfig.class);
    }
}
