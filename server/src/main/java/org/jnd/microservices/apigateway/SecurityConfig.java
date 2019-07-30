package org.jnd.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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

        //to do a HTTP POST, we must turn off Cross-Site Request Forgery protection, which is on by default
        http.csrf().disable();

        // exclude OPTIONS requests from authorization checks
        http.cors();

        //must present a valid access token
        http
                .authorizeRequests()
                .antMatchers("/api/products/**").anonymous()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authorityExtractor());

    }

    Converter<Jwt, AbstractAuthenticationToken> authorityExtractor() {
        return new AuthorityExtractor();
    }

}


// Example Access Token
//{
//        "jti": "ce4e8113-34dd-49fc-9a9d-85a7dad96dee",
//        "exp": 1564394303,
//        "nbf": 0,
//        "iat": 1564394003,
//        "iss": "https://kc.services.theosmo.com/auth/realms/VRM-DEV",
//        "aud": [
//        "realm-management",
//        "account"
//        ],
//        "sub": "Justin",
//        "typ": "Bearer",
//        "azp": "vrm-shell-oidc",
//        "auth_time": 0,
//        "session_state": "5a127642-ba81-498b-8f01-2cafa62a572f",
//        "acr": "1",
//        "allowed-origins": [
//        "https://vrm-shell-oidc-vrm-dev.nonprod.theosmo.com",
//        "https://vrm-shell-vrm-dev.nonprod.theosmo.com",
//        "http://localhost:8080"
//        ],
//        "realm_access": {
//        "roles": [
//        "offline_access",
//        "uma_authorization"
//        ]
//        },
//        "resource_access": {
//        "realm-management": {
//        "roles": [
//        "view-identity-providers",
//        "view-realm",
//        "manage-identity-providers",
//        "impersonation",
//        "realm-admin",
//        "create-client",
//        "manage-users",
//        "query-realms",
//        "view-authorization",
//        "query-clients",
//        "query-users",
//        "manage-events",
//        "manage-realm",
//        "view-events",
//        "view-users",
//        "view-clients",
//        "manage-authorization",
//        "manage-clients",
//        "query-groups"
//        ]
//        },
//        "vrm-shell-oidc": {
//            "roles": [
//                  "vrmuser"
//            ]
//        },
//        "account": {
//        "roles": [
//                "manage-account",
//                "manage-account-links",
//                "view-profile"
//        ]
//        }
//        },
//        "scope": "email profile",
//        "email_verified": true,
//        "name": "Justin Davis",
//        "preferred_username": "justind",
//        "given_name": "Justin",
//        "family_name": "Davis",
//        "email": "jusdavis@redhat.com",
//        "authorities": [
//              "vrmuser"
//        ]
//        }







