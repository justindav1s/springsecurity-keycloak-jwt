package org.jnd.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.*;
import java.util.stream.Collectors;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${jwt.azp}")
    private String azp;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        log.debug("Securing Application : "+azp);

        //to do a HTTP POST, we must turn off Cross-Site Request Forgery protection, which is on by default
        http.csrf().disable();

        // exclude OPTIONS requests from authorization checks
        http.cors();

        //must present a valid access token
        http.cors().and().csrf().disable().authorizeRequests().
                antMatchers("/api/products/all").hasAuthority("ROLE_product").
                antMatchers("/**").denyAll().
                anyRequest().authenticated().
                and().
                oauth2ResourceServer().
                jwt()
                .jwtAuthenticationConverter(authorityExtractor(azp));
    }

    Converter<Jwt, AbstractAuthenticationToken> authorityExtractor(String azp) {
        return new AuthorityExtractor(azp);
    }
}


class AuthorityExtractor extends JwtAuthenticationConverter {

    private static final Logger log = LoggerFactory.getLogger(AuthorityExtractor.class);

    String azp;

    public AuthorityExtractor(String azp)   {
        this.azp = azp;
    }

    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        log.debug("Looking for azp claim for : "+azp);

        String authParty = (String) jwt.getClaims().get("azp");

        Collection<String> authorities = (Collection<String>) new ArrayList<String>();

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
// Method above extracts roles from a JWT Access that looks like this
//        {
//        "jti": "8c14401d-efc0-4bcc-ad0a-5af053a24fb5",
//        "exp": 1562925978,
//        "nbf": 0,
//        "iat": 1562925678,
//        "iss": "http://127.0.0.1:8080/auth/realms/amazin",
//        "aud": "webapp",
//        "sub": "23f21545-19e6-4189-8795-8a303905eaca",
//        "typ": "Bearer",
//        "azp": "webapp",
//        "auth_time": 0,
//        "session_state": "23442335-5c72-4fcc-b6d8-64dfd4076db5",
//        "acr": "1",
//        "allowed-origins": [ "*"],
//        "resource_access": {
//            "webapp": {
//                "roles": [
//                        "ROLE_product",
//                        "ROLE_user",
//                        "ROLE_basket"
//                  ]
//            }
//        },
//        "name": "Justin Davis",
//        "preferred_username": "justin",
//        "given_name": "Justin",
//        "family_name": "Davis",
//        "email": "justinndavis@gmail.com"
//        }






