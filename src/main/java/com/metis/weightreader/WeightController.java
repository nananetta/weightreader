package com.metis.weightreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeightController {

	@Autowired
	private SimpMessagingTemplate template;
	
	private static WeightReaderRunner runner;

	private static final Logger LOGGER = LogManager.getLogger(WeightController.class);

	@MessageMapping("/read")
	@SendTo("/topic/weight")
	public Weight startWeightReader() throws Exception {
		runner = new WeightReaderRunner(this);
		runner.run();
		return new Weight(0.0);
	}

	@SendTo("/topic/weight")
	public void update(Weight weight) {
		this.template.convertAndSend("/topic/weight", weight);
	}

	@RequestMapping("/disconnect")
	public void disconnect() {
		System.out.println("Disconnect to SerialReader");
		runner.stop();
		SerialReader.close();
	}
	
	/**
	 ** Only for Testing
	 **/
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
