package org.jnd.microservices.webapp;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Component
@Slf4j
public class TokenInjectingFilter extends ZuulFilter {

    @Autowired
    OAuth2AuthorizedClientService clientService;

    TokenInjectingFilter(OAuth2AuthorizedClientService clientService){
        this.clientService = clientService;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        log.info("TokenInjectingFilter : run");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        SecurityContextImpl context = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");

        //log.info("TokenInjectingFilter : credentials : "+context.getAuthentication().getCredentials());

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) context.getAuthentication();

        //log.info("TokenInjectingFilter : oauthToken : "+oauthToken);
        //log.info("TokenInjectingFilter : oauthToken reg id : "+oauthToken.getAuthorizedClientRegistrationId());
        //log.info("TokenInjectingFilter : oauthToken principal : "+oauthToken.getPrincipal());

        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName());

        String accessToken;
        if ( (client != null) && (client.getAccessToken() != null) )   {
            accessToken = client.getAccessToken().getTokenValue();

            Instant expires = client.getAccessToken().getExpiresAt();
            Instant now = Instant.now();

            log.info("AccessToken time to expire : "+ (expires.getEpochSecond() - now.getEpochSecond()));
            //log.info("AccessToken : " + accessToken);
            ctx.addZuulRequestHeader("Authorization", "Bearer " + accessToken);
        }


        return null;
    }
}
