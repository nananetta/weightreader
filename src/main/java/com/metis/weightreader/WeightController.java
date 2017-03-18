package com.metis.weightreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metis.smartcardreader.SmartCardReaderRunner;
import com.metis.smartcardreader.SmartCard;

@RestController
public class WeightController {

	@Autowired
	private SimpMessagingTemplate template;

	// private static WeightReaderRunner runner;

	// private static ReaderRunner readerRunner;

	private static Thread weightRunnerThread;
	private static Thread cardRunnerThread;

	private static final Logger LOGGER = LogManager.getLogger(WeightController.class);

	private Weight currWeight;
	private SmartCard currCard;
	private boolean isResetWeight = false;

	@MessageMapping("/read")
	@SendTo("/topic/weight")
	public Weight startWeightReader() throws Exception {
		weightRunnerThread = new Thread(new WeightReaderRunner(this));
		weightRunnerThread.start();
		LOGGER.info("WeightReader started.");

		cardRunnerThread = new Thread(new SmartCardReaderRunner(this));
		cardRunnerThread.start();
		LOGGER.info("SmartCardReader started.");
		return new Weight(0.0);
	}

	@SendTo("/topic/weight")
	public void updateWeight(Weight weight) {
		this.currWeight = weight;
		LOGGER.info("zero indicator: " + weight.isZeroIndicator());
		this.template.convertAndSend("/topic/weight", weight);
	}

	@SendTo("/topic/driverRecord")
	public void updateDriverRecord(SmartCard card) {
		this.template.convertAndSend("/topic/driverRecord", card);
	}

	public void updateCard(SmartCard card) {
		this.currCard = card;
		if (isValidCurrWeight() && isValidCurrCard()) {
			LOGGER.info("UPDATE WEIGHT && CARD");
			updateDriverRecord(currCard);
			currWeight = null;
			currCard = null;
			isResetWeight = true;
		}
	}

	private boolean isValidCurrWeight() {
		return (currWeight != null && currWeight.isZeroIndicator());
	}

	private boolean isValidCurrCard() {
		return (currCard != null && currCard.getIdentifier() != null && currCard.getIdentifier().length() > 0);
	}

	@RequestMapping("/disconnect")
	public void disconnect() {
		LOGGER.info("Disconnect to SerialReader");
		weightRunnerThread.stop();
		cardRunnerThread.stop();
		SerialReader.close();
	}

	public boolean isResetWeight() {
		return isResetWeight;
	}

	public void setResetWeight(boolean isResetWeight) {
		this.isResetWeight = isResetWeight;
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
