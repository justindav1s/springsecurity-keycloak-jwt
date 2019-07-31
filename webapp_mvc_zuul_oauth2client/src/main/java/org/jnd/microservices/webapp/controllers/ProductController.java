package org.jnd.microservices.webapp.controllers;

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
public class ProductController {

    RestTemplate restTemplate = new RestTemplate();

    String uri_list = "http://127.0.0.1:9080/all";


    @GetMapping("/products")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model, @RequestHeader HttpHeaders headers) {

        for (String key : headers.keySet())        {
            log.info(">>>WEB APP Headers key : "+key+" value : "+headers.get(key));
        }

        ResponseEntity<String> productList =
                this.restTemplate.exchange(
                        uri_list,
                        HttpMethod.GET,
                        new HttpEntity<byte[]>(headers),
                        new ParameterizedTypeReference<String>() {});

        model.addAttribute("productList", productList.getBody());


        return "product_view";
    }

}
