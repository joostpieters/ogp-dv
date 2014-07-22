package worms.model;

/**
 * A class of food 
 * 
 * @author Delphine
 */

public class Food extends GameObject {
	
	/**
	 * 
	 */
	public Food(World world, Position position) {
		super(world, position, 0.2);
	}
	
	public double getGrowthEffect() {
		return 0.1;
	}


}
