package worms.model;

import worms.exceptions.IllegalPositionException;
import worms.exceptions.IllegalRadiusException;

/**
 * A class of food
 * 
 * @author Delphine
 */
public class Food extends GameObject {
	
	/**
	 *  Initialize this new food with given world, position and radius = 0.2.
	 *  
	 * @param  world
	 * 		   The world in which to place the created food 
	 * @param  position 
	 * 		   The position of this new food (in meter)
	 * @effect If the given world is effective, this food is added as a game object of the given world.
     * 	     | if (world != null) then world.addAsGameObject(this)
	 * @effect The position of this new food is equal to the given position.
     *       | setPosition(position)
     * @effect The radius of this new food is equal to the 0.2.
     * 		 | setRadius(0.2) 
     * @throws IllegalPositionException
	 * 		   The given position is not a valid position for food.
	 *       | ! position.canHaveAsPosition(position)
	 * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for food.
	 */
	public Food(World world, Position position) throws IllegalPositionException, IllegalRadiusException {
		super(world, position, 0.2);
	}
	
	/**
	 * Returns the effect of eating this food ration on the radius of the game object that eats this ration.
	 * @return result == 0.1
	 */
	public double getGrowthEffect() {
		return 0.1;
	}


}
