package com.metis.smartcardreader;

import java.util.Date;

public class SmartCard {
	
    private final String identifier;
    private final Date readDate;
    
    public SmartCard(String identifier) {
    	this.identifier = identifier;
    	this.readDate = new Date();
    }

	public String getIdentifier() {
		return identifier;
	}

	public Date getReadDate() {
		return readDate;
	}
    
	
}
