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
import java.util.Enumeration;
import java.util.Map;

@Component
@Slf4j
public class PostRoutingHeaderCheckerFilter extends ZuulFilter {

    @Autowired
    OAuth2AuthorizedClientService clientService;

    PostRoutingHeaderCheckerFilter(OAuth2AuthorizedClientService clientService){
        this.clientService = clientService;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {

        log.info("PostRoutingHeaderCheckerFilter  : run");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        Enumeration<String> header_names = request.getHeaderNames();

        while (header_names.hasMoreElements())    {
            String name = header_names.nextElement();
            String value = request.getHeader(name);
            log.info("Request Header name : "+name+" value : "+value);

        }

        Map<String, String> headers = ctx.getZuulRequestHeaders();
        for (String key : headers.keySet())        {
            log.info("Zuul Request Header key : "+key+" value : "+headers.get(key));
        }


        return null;
    }
}
