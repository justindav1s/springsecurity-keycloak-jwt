package org.jnd.microservices.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@Slf4j
@EnableZuulProxy
public class WebappApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebappApplication.class, args);
	}

}
