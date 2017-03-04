package com.metis.weightreader;

import java.util.Date;

public class Weight {
	
    private final Double weight;
    private final Unit unit;
    private final Date measureDate;
    private boolean zeroIndicator = false;
    
    public Weight(Double weight) {
    	this.weight = weight;
    	this.unit = Unit.KG;
    	this.measureDate = new Date();
    }
    
    public Weight(Double weight, Unit unit) {
    	this.weight = weight;
    	this.unit = unit;
    	this.measureDate = new Date();
    }
    
	public Double getWeight() {
		return weight;
	}
	
	public String getWeightString() {
		return String.format("%05d", weight.intValue());
	}
	
	public Unit getUnit() {
		return unit;
	}
	public Date getMeasureDate() {
		return measureDate;
	}

	public boolean isZeroIndicator() {
		return zeroIndicator;
	}

	public void setZeroIndicator(boolean zeroIndicator) {
		this.zeroIndicator = zeroIndicator;
	}

	enum Unit { 
		KG("KG"), G("G"), LB("LB");
		
		private final String text;
		
		private Unit(final String text) {
	        this.text = text;
	    }

	    public String toString() {
	        return text;
	    }
	};
}
