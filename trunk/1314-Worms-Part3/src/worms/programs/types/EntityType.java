package worms.programs.types;

import worms.model.GameObject;
import worms.programs.Type;

public class EntityType extends Type {

	
	public EntityType(GameObject value){
		this.value = value;
	}
	
	public EntityType(){
	}

	@Override
	public GameObject getValue() {
		return this.value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (GameObject) value;
	}

	protected GameObject value;
	
	@Override
	public Class<? extends Type> getType() {
		return EntityType.class;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		EntityType otherEntityType = (EntityType) other;
		return 
			( this.getValue() == otherEntityType.getValue() );
	}
	

	
}
