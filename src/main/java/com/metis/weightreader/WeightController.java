package com.metis.weightreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeightController {

	private static final Logger LOGGER = LogManager.getLogger(WeightController.class);

	@RequestMapping("/readWeight")
	public Weight read() {
		Weight weight = null;
		try {
			weight = SerialReader.readWeight();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return weight;
	}
}
