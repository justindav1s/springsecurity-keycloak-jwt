package org.jnd.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorityExtractor extends JwtAuthenticationConverter {

    private static final Logger log = LoggerFactory.getLogger(AuthorityExtractor.class);

    public AuthorityExtractor() {
    }

    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        Collection<String> authorities = new ArrayList<String>();

        log.debug("Extracting authorities.");

        Collection<String> jwtAuthorities = (Collection<String>) jwt.getClaims().get("authorities");

        if (jwtAuthorities != null) {
            authorities = jwtAuthorities;
            log.debug("Found authorities : " + authorities);
        }
        else
            log.debug("No authorities found.");

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
