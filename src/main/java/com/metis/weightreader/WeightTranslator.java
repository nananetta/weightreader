package com.metis.weightreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeightTranslator {

	public static Weight translate(String rawValue) {
		Double value = 0.0;
		Pattern p = Pattern.compile(SerialPortProperties.getRawValueParserRegex());
	    Matcher m = p.matcher(rawValue);
	    m.find();
	    String actualValue = m.group();
	    value = Double.parseDouble(actualValue);
//	    System.out.println(m.find());
//	    System.out.println(m.group());
		return new Weight(value, Weight.Unit.G);
	}
}
