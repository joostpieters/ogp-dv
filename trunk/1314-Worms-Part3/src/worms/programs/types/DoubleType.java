package worms.programs.types;

import java.lang.Math;

import worms.programs.Type;


public class DoubleType extends Type {
	
	private double value;
	
	public DoubleType(double value) {
		this.value = value;
	}
	
	public DoubleType() {
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		this.value = (Double) value;
	}

	@Override
	public Class<DoubleType> getType() {
		return DoubleType.class;
	}
	
	public int intValue() {
		if ( getValue() > Integer.MAX_VALUE )
			return Integer.MAX_VALUE;
		if( getValue() < Integer.MIN_VALUE )
			return Integer.MIN_VALUE;
		return getValue().intValue();
	}
	
	public int roundToNearestInteger() {
		double number = Math.round( getValue() );
		return new DoubleType(number).intValue();
	}
	
	public DoubleType add(DoubleType e2) {
		double e2Value = e2.getValue();
		if ( getValue() + e2Value > Double.POSITIVE_INFINITY )
			return new DoubleType( Double.POSITIVE_INFINITY );
		else if ( getValue() + e2Value < Double.NEGATIVE_INFINITY )
			return new DoubleType( Double.NEGATIVE_INFINITY );
		else 
			return new DoubleType( this.getValue() + e2Value );
		
	}
	
	public DoubleType subtract(DoubleType e2) {
		return new DoubleType( this.value - e2.getValue() );
		
	}
	
	public DoubleType multiplyBy(DoubleType e2) { 
		return new DoubleType( this.value *  e2.getValue() );
		
	}
	
	public DoubleType divideBy(DoubleType e2) {
		return new DoubleType( this.value / e2.getValue() );
		
	}
	
	public DoubleType sqrt() {
		return new DoubleType( Math.sqrt(this.value) );
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		DoubleType otherDoubleType = (DoubleType) other;
		return 
			( this.getValue() == otherDoubleType.getValue() );
	}
}
