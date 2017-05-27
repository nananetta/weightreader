package com.metis.weightreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger LOGGER = LogManager.getLogger(Application.class);
	
    public static void main(String[] args) {
    	
		if(ConfigProperties.isMockWeightReader()) {
			LOGGER.info("Using mock weight reader!");
		}
		if(ConfigProperties.isMockCardReader()) {
			LOGGER.info("Using mock card reader!");
		}

        SpringApplication.run(Application.class, args);
    }
}
