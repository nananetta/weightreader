package com.metis.smartcardreader;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.metis.weightreader.ConfigProperties;
import com.metis.weightreader.SerialReader;
import com.metis.weightreader.Weight;
import com.metis.weightreader.WeightController;
import com.metis.weightreader.WeightReaderRunner;

public class SmartCardReaderRunner implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(SmartCardReaderRunner.class);

	private WeightController listener;
	private volatile boolean running = true;
	private static int cardReadInterval;
	private static boolean isMockCardReader;
	private CardTerminal terminal;

	static {
		cardReadInterval = ConfigProperties.getCardReadInterval();
		isMockCardReader = ConfigProperties.isMockCardReader();
	}

	public SmartCardReaderRunner(WeightController listener) {
		this.listener = listener;
	}
	
	@Override
	public void run() {
		try {
			SmartCardReader.init();
			while (running) {
				SmartCard card = null;
				if (!isMockCardReader) {
					card = SmartCardReader.read();
				} else {
					card = mockCardRead();
				}
				if (card != null) {
					listener.updateCard(card);
				}
				Thread.sleep(cardReadInterval);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SmartCard mockCardRead() {
		int rand = (int) (Math.random() * 100);
		if (rand > 80) {
			int randomCard = (int) (Math.random() * 100);
			String cardUid = "XC-" + randomCard;
			LOGGER.info(cardUid);
			return new SmartCard(cardUid);
		} else {
			LOGGER.info("NO CARD.");
			return null;
		}
	}

	public void stop() {
		this.running = false;
		LOGGER.info("Runner Stopped");
	}

}
