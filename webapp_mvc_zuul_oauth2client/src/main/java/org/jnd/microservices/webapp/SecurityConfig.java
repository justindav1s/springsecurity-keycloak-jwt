package org.jnd.microservices.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/index.html").hasAuthority("products")
                .mvcMatchers("/products").hasAuthority("products")
                .mvcMatchers("/test.html").hasAuthority("products")
                .mvcMatchers("/all").hasAuthority("products")
                .mvcMatchers("/").hasAuthority("products")
                .mvcMatchers("/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userAuthoritiesMapper(this.userAuthoritiesMapper());
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Map<String, Object> userAttributes = new HashMap<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority) {
                    log.info("OidcUserAuthority  Attributes : "+((OidcUserAuthority) authority).getAttributes());
                    Map<String, Object> attributes = ((OidcUserAuthority) authority).getAttributes();
                    userAttributes.putAll(attributes);
                } else if (authority instanceof OAuth2UserAuthority) {
                    log.info("OAuth2UserAuthority : "+authority);
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;
                    userAttributes.putAll(oauth2UserAuthority.getAttributes());
                }
            });

            Collection<String> customAuth = new ArrayList<String>();
            if (userAttributes.get("authorities") != null) {
                customAuth = (Collection<String>) userAttributes.get("authorities");
            }

            log.info("Found User Authorities : "+ customAuth);

            List<SimpleGrantedAuthority> collect = customAuth.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return collect;
        };
    }


//    @Bean
//    WebClient webClient(ClientRegistrationRepository clientRegistrations,
//                        OAuth2AuthorizedClientRepository authorizedClients) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth =
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
//        // (optional) explicitly opt into using the oauth2Login to provide an access token implicitly
//         oauth.setDefaultOAuth2AuthorizedClient(true);
//        // (optional) set a default ClientRegistration.registrationId
//         oauth.setDefaultClientRegistrationId("keycloak");
//        return WebClient.builder()
//                .apply(oauth.oauth2Configuration())
//                .build();
//    }

}










