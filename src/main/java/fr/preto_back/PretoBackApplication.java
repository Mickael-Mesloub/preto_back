package fr.preto_back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PretoBackApplication {
	private final static Logger logger = LoggerFactory.getLogger(PretoBackApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PretoBackApplication.class, args);
		logger.info("Application started successfully!");
	}

}
