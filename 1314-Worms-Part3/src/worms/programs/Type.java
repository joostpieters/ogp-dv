package worms.programs;


public abstract class Type {
	
	public abstract Object getValue();

	public abstract void setValue(Object value);

	public abstract Class<? extends Type> getType();
	
	@Override
	public abstract boolean equals(Object other);



}
