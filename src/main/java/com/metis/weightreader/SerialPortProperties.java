package com.metis.weightreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class SerialPortProperties {

	private static final Logger LOGGER = LogManager.getLogger(SerialPortProperties.class);
	protected static Properties p = new Properties();

	static {
		ClassPathResource resource = new ClassPathResource("serialport.properties");
		InputStream inputStream = null;
		try {
			inputStream = resource.getInputStream();
			p.load(inputStream);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private SerialPortProperties() {

	}

	public static String getPortName() {
		return p.getProperty("portname");
	}

	public static int getBaudRate() {
		return Integer.parseInt(p.getProperty("baudrate"));
	}

	public static int getDataBits() {
		return Integer.parseInt(p.getProperty("databits"));
	}

	public static int getParity() {
		return Integer.parseInt(p.getProperty("parity"));
	}

	public static int getStopBits() {
		return Integer.parseInt(p.getProperty("stopbits"));
	}
	
	public static String getRawValueParserRegex() {
		return p.getProperty("rawvalue.parser.regex");
	}

}
