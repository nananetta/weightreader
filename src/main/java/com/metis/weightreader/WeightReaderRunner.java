package com.metis.weightreader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeightReaderRunner implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(WeightReaderRunner.class);

	private WeightController listener;
	private volatile boolean running = true;
	private static int weightReadInterval;
	private static boolean isMockWeightReader;
	private int zeroCounter = 0;

	static {
		weightReadInterval = ConfigProperties.getWeightReadInterval();
		isMockWeightReader = ConfigProperties.isMockWeightReader();
	}

	public WeightReaderRunner(WeightController listener) {
		LOGGER.info("Start WeightReader Thread");
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			if (!isMockWeightReader) {
				SerialReader.init();
			}
			while (running) {
				Weight weight = null;
				if (!isMockWeightReader) {
					weight = SerialReader.read();
				} else {
					weight = mockWeightRead();
				}
				LOGGER.info("weight: " + weight.getWeightString());
				// Check if weight is zero for a number of times, then mark zeroIndicator flag.
				checkZeroIndicator(weight);
				listener.updateWeight(weight);
				Thread.sleep(weightReadInterval);
			}
			LOGGER.info("stopped");
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (!isMockWeightReader) {
				SerialReader.close();
			}
		}
	}

	private void checkZeroIndicator(Weight weight) {
		if(listener.isResetWeight()) {
			weight.setZeroIndicator(false);
			zeroCounter = 0;
			listener.setResetWeight(false);
			return;
		}
		if (weight.getWeight() == 0) {
//			System.out.println("weight.getWeight(): "+weight.getWeight());
			zeroCounter++;
//		} else {
//			zeroCounter = 0;
//			weight.setZeroIndicator(false);
		}
//		System.out.println("zeroCounter: "+zeroCounter);
		if (zeroCounter >= 1) {
			weight.setZeroIndicator(true);
		}
	}
	
	private Weight mockWeightRead() {
		int rand = (int) (Math.random() * 10);
		return new Weight((double) rand);
	}

	public void stop() {
		this.running = false;
		this.zeroCounter = 0;
		LOGGER.info("Runner Stopped");
	}

}
