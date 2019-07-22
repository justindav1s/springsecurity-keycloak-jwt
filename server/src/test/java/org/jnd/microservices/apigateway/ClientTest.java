package org.jnd.microservices.apigateway;

import org.jnd.microservices.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ClientTest {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MockMvc mvc;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String authserver;
    @Value("${test.jwt.username}")
    String username;
    @Value("${test.jwt.password}")
    String password;
    @Value("${test.jwt.client_id}")
    String client_id;
    @Value("${test.jwt.client_secret}")
    String client_secret;

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Test
    public void canAccessProtectedResource() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/products/all").header("Authorization", "Bearer "+getAccessToken())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void cannotAccessProtectedResource() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/payments").header("Authorization", "Bearer "+getAccessToken())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    public String getAccessToken() {

        log.debug("************ authserver : "+authserver);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", this.username);
        params.add("password", this.password);
        params.add("client_id", this.client_id);
        params.add("client_secret", this.client_secret);
        params.add("scope", "openid");


        HttpEntity<MultiValueMap> request = new HttpEntity<>(params, null);

        ResponseEntity<String> exchange = null;
        try {
            exchange =
                    this.restTemplate.exchange(
                            authserver+"/protocol/openid-connect/token",
                            HttpMethod.POST,
                            request,
                            String.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String response = exchange.getBody();

        log.debug("Login Response : "+response);

        JacksonJsonParser jsonParser = new JacksonJsonParser();

        String accessToken = jsonParser.parseMap(response).get("access_token").toString();

        log.debug("************ obtainAccessToken : "+accessToken);

        return accessToken;
    }

}
