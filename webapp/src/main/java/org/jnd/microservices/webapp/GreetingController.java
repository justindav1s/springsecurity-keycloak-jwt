package org.jnd.microservices.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class GreetingController {

    RestTemplate restTemplate = new RestTemplate();

    String uri = "http://127.0.0.1:1080/api/products/types";

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        log.info("Greetings !");
        model.addAttribute("name", name);

        ResponseEntity<String> products = restTemplate.getForEntity(uri, String.class);

        model.addAttribute("products", products);

        return "view";
    }

}
