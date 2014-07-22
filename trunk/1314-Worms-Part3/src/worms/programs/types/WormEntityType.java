package worms.programs.types;
import worms.model.Worm;

public class WormEntityType extends EntityType{
	
	public WormEntityType(Worm worm){
		super(worm);
	}

	
	public Worm getValue() {
		return (Worm) this.value;
	}
	
	public void setValue(Object value) {
		this.value = (Worm) value;
	}
		

	@Override
	public Class<WormEntityType> getType() {
		return WormEntityType.class;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		WormEntityType otherWormEntityType = (WormEntityType) other;
		return 
			( this.getValue() == otherWormEntityType.getValue() );
	}
	
	
	//public GameObject searchNearestObjectInGivenDirection(DoubleType theta){
	//	return this.getValue().searchNearestObjectInGivenDirection(theta.getValue());
	//}

	
}
