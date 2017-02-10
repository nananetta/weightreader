package com.metis.weightreader;

import java.util.Date;

public class Weight {

    private final Double weight;
    private final Unit unit;
    private final Date measureDate;
    
    public Weight(Double weight, Unit unit) {
    	this.weight = weight;
    	this.unit = unit;
    	this.measureDate = new Date();
    }
    
	public Double getWeight() {
		return weight;
	}
	public Unit getUnit() {
		return unit;
	}
	public Date getMeasureDate() {
		return measureDate;
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
