package org.jnd.microservices.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WebappApplication {

	public static void main(String[] args) {
		log.info("Running");
		SpringApplication.run(WebappApplication.class, args);
	}

}
