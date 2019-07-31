package org.jnd.microservices.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@Slf4j
public class GreetingController {

    RestTemplate restTemplate = new RestTemplate();

    String uri_types = "http://127.0.0.1:1080/api/products/types";
    String uri_list = "http://127.0.0.1:9080/all";


    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model, @RequestHeader HttpHeaders headers) {
        log.info("Greetings !");

        log.info(">>>>>>>>>>>>> "+headers.toString());

        for (String key : headers.keySet())        {
            log.info(">>> key :"+key+" value : "+headers.get(key));
        }


        model.addAttribute("name", name);

        ResponseEntity<String> productTypes = restTemplate.getForEntity(uri_types, String.class);
        model.addAttribute("productTypes", productTypes.getBody());

        ResponseEntity<String> productList =
                this.restTemplate.exchange(
                        uri_list,
                        HttpMethod.GET,
                        new HttpEntity<byte[]>(headers),
                        new ParameterizedTypeReference<String>() {});

        model.addAttribute("productList", productList.getBody());


        return "view";
    }

}
