package worms.programs.types;

import worms.model.Food;


public class FoodEntityType extends EntityType{
	
	public FoodEntityType(Food food){
		super(food);
	}
	
	public Food getValue() {
		return (Food) value;
	}
	
	public void setValue(Object value) {
		this.value = (Food) value;
	}
	

	@Override
	public Class<FoodEntityType> getType() {
		return FoodEntityType.class;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		FoodEntityType otherFoodEntityType = (FoodEntityType) other;
		return 
			( this.getValue() == otherFoodEntityType.getValue() );
	}
	
	
}
