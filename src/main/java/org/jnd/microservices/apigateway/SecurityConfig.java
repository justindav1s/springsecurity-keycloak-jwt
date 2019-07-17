package org.jnd.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.jwt.Jwt;
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

        log.debug("Securing Application : " + azp);

        //to do a HTTP POST, we must turn off Cross-Site Request Forgery protection, which is on by default
        http.csrf().disable();

        // exclude OPTIONS requests from authorization checks
        http.cors();

        //must present a valid access token
        http
                .authorizeRequests()
                .antMatchers("/api/products/all").hasRole("product")
                .antMatchers("/**").denyAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authorityExtractor(azp));

    }

    Converter<Jwt, AbstractAuthenticationToken> authorityExtractor(String azp) {
        return new AuthorityExtractor(azp);
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






