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

    String azp;

    public AuthorityExtractor(String azp) {
        this.azp = azp;
    }

    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        log.debug("Looking for azp claim for : " + azp);

        String authParty = (String) jwt.getClaims().get("azp");

        Collection<String> authorities = new ArrayList<String>();

        if (azp.equals(authParty)) {
            log.debug("Token was issued by matching Authorizing Party  : " + authParty);
            log.debug("Extracting Roles for Authorizing Party : " + authParty);

            Map<String, Object> resourceacc = (Map<String, Object>) jwt.getClaims().get("resource_access");
            Map<String, Object> scopedapp = (Map<String, Object>) resourceacc.get(authParty);
            authorities = (Collection<String>) scopedapp.get("roles");

            log.debug("Found authorities : " + authorities);
        }

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
