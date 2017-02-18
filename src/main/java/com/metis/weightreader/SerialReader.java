package com.metis.weightreader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialReader {

	private static final Logger LOGGER = LogManager.getLogger(SerialReader.class);
	private static SerialPort serialPort = null;

	public static Weight readWeight() throws Exception {
		Weight weight = null;
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(SerialPortProperties.getPortName());
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
			LOGGER.error("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(SerialReader.class.getName(), 2000);
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(SerialPortProperties.getBaudRate(), SerialPortProperties.getDataBits(),
						SerialPortProperties.getStopBits(), SerialPortProperties.getParity());

				InputStream in = serialPort.getInputStream();
				String value = readValue(in);
				weight = WeightTranslator.translate(value);
				commPort.close();
			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
				LOGGER.error("Error: Only serial ports are handled by this example.");
			}
		}
		return weight;
	}

	public static void init() {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(SerialPortProperties.getPortName());
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
				LOGGER.error("Error: Port is currently in use");
			} else {
				CommPort commPort = portIdentifier.open(SerialReader.class.getName(), 2000);
				if (commPort instanceof SerialPort) {
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(SerialPortProperties.getBaudRate(),
							SerialPortProperties.getDataBits(), SerialPortProperties.getStopBits(),
							SerialPortProperties.getParity());
				} else {
					System.out.println("Error: Only serial ports are handled by this example.");
					LOGGER.error("Error: Only serial ports are handled by this example.");
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static void close() {
		if (serialPort != null) {
			serialPort.close();
		}
	}

	public static Weight read() {
		try {
			InputStream in = serialPort.getInputStream();
			String value = readValue(in);
			return WeightTranslator.translate(value);
		} catch (Exception e) {
			System.out.println("Unable to read weight value.");
			LOGGER.error(e.getMessage(), e);
		}
		return new Weight(0.0);
	}

	private static String readValue(InputStream in) {
		byte[] buffer = new byte[1024];
		int len = -1;
		String value = null;
		try {
			while ((len = in.read(buffer)) > -1) {
				// System.out.println(new String(buffer, 0, len));
				value = new String(buffer, 0, len);

				// If read value is invalid, try read again
				if (value.length() < 10) {
					continue;
				}
				break;
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		return value;
	}

	// /** */
	// public static class SerialInReader implements Runnable {
	// InputStream in;
	//
	// public SerialInReader(InputStream in) {
	// this.in = in;
	// }
	//
	// public void run() {
	// byte[] buffer = new byte[1024];
	// int len = -1;
	// try {
	// while ((len = this.in.read(buffer)) > -1) {
	// System.out.println(new String(buffer, 0, len));
	// return;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

}
