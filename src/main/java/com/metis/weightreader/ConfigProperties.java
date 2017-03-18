package com.metis.weightreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class ConfigProperties {

	private static final Logger LOGGER = LogManager.getLogger(ConfigProperties.class);
	protected static Properties p = new Properties();

	static {
		ClassPathResource resource = new ClassPathResource("config.properties");
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

	private ConfigProperties() {

	}

	public static String getPortName() {
		return p.getProperty("serialport.portname");
	}

	public static int getBaudRate() {
		return Integer.parseInt(p.getProperty("serialport.baudrate"));
	}

	public static int getDataBits() {
		return Integer.parseInt(p.getProperty("serialport.databits"));
	}

	public static int getParity() {
		return Integer.parseInt(p.getProperty("serialport.parity"));
	}

	public static int getStopBits() {
		return Integer.parseInt(p.getProperty("serialport.stopbits"));
	}
	
	public static String getRawValueParserRegex() {
		return p.getProperty("serialport.rawvalue.parser.regex");
	}
	
	public static int getWeightReadInterval() {
		return Integer.parseInt(p.getProperty("weightread.interval"));
	}
	
	public static boolean isMockWeightReader() {
		return Boolean.valueOf(p.getProperty("weightread.mock"));
	}

	public static int getCardReadInterval() {
		return Integer.parseInt(p.getProperty("cardread.interval"));
	}
	
	public static boolean isMockCardReader() {
		return Boolean.valueOf(p.getProperty("cardread.mock"));
	}

}
