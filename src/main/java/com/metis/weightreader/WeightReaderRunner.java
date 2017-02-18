package com.metis.weightreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeightReaderRunner implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(WeightReaderRunner.class);

	private WeightController listener;
	private volatile boolean running = true;

	public WeightReaderRunner(WeightController listener) {
		LOGGER.info("Start WeightReader Thread");
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
//			SerialReader.init();
			while (running) {
				Thread.sleep(1000);
//				Weight weight = SerialReader.read();
//				listener.update(weight);
				listener.update(mockWeightRead());
			}
			System.out.println("stopped");
		} catch (InterruptedException e) {
			LOGGER.info(e.getMessage(), e);
		} finally {
//			SerialReader.close();
		}
	}

	private Weight mockWeightRead() {
		int rand = (int) (Math.random() * 100);
		return new Weight((double) rand);
	}
	
	public void stop() {
		this.running = false;
		LOGGER.info("Runner Stopped");
	}

}
