package org.jnd.microservices.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);


    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<?> getProduct(@PathVariable int id, @RequestHeader HttpHeaders headers) {

        ResponseEntity<?> response = new ResponseEntity<>("[{\"id\":\"1\",\"name\":\"socks\",\"type\":\"CLOTHES\",\"price\":3.39,\"}]", HttpStatus.OK);

        return response;

    }

    @RequestMapping(value = "/products/types", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<?> getProductTypes(@RequestHeader HttpHeaders headers) {

        ResponseEntity<?> response = new ResponseEntity<>("[\"CLOTHES\", \"CARS\", \"GADGETS\"]", HttpStatus.OK);

        return response;
    }

    @RequestMapping(value = "/products/all", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<?> getAllProducts(@RequestHeader HttpHeaders headers) {

        ResponseEntity<?> response = new ResponseEntity<>("[{\"id\":\"11\",\"name\":\"socks\",\"type\":\"CLOTHES\",\"price\":3.39,\"basketIndex\":0}]", HttpStatus.OK);
        return response;
    }


    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String ping() {
        return "OK";
    }


}
