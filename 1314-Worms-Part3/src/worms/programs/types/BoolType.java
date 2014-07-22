package worms.programs.types;

import worms.programs.Type;

public class BoolType extends Type {
	

	public BoolType(boolean value){
		this.value = value;
	}
	
	public BoolType(){
	}
	
	public Boolean getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = (Boolean) value;
	}
	
	private Boolean value;

	@Override
	public Class<BoolType> getType() {
		return BoolType.class;
	}

	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		BoolType otherBooleanType = (BoolType) other;
		return 
			( this.getValue() == otherBooleanType.getValue() );
	}

}