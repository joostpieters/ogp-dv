package worms.model;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;

/**
 * A class of moles
 *                   
 * @author   Delphine Vandamme 
 */
public class Mole extends Character {

	/**
	 * Initialize this new  with given name, location, direction and radius.
	 *
	 * @param  world
	 * 		   The world in which to place the created mole  
	 * @param  position 
	 * 		   The position of this new mole (in meter)
	 * @param  direction
	 * 		   The direction of the new mole (in radians)
	 * @param  radius 
	 * 		   The radius of the new mole (in meter)
	 * @param  name
	 * 		   The name of the new character
	 * @param  program
	 *         The program of the new character.
	 * @pre	   The given direction must be a valid direction for a character.
     *       | isValidDirection(direction) 
	 * @effect The position of this new mole is equal to the given position.
     *       | setPosition(position)
     * @effect The radius of this new mole is equal to the given radius.
     * 		 | setRadius(radius) 
     * @effect The direction of this new mole is equal to the given direction.
     *       | setDirection(direction)
     * @effect The name of this new mole is equal to the given name.
     *       | setName(name)
     * @effect If the given program is effective, this mole is the executing 
     *         agent of the program and the program of this mole is equal to the given program.
     * 		 |	if (program != null)
     *       |     then program.setAgent(this) && setProgram(program)
     * @effect If the given world is effective, this mole is added as a game object of the given world.
     * 	     | if (world != null)
	 *       |    then world.addAsGameObject(this)
     * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for a character.
	 *       | ! isValidRadius(radius)         
	 * @throws IllegalNameException
	 * 		   The given name is not a valid name for a character.
	 *       | ! isValidName(name)  
	 * @throws IllegalPositionException
	 * 		   The given position is not a valid position for a character.
	 *       | ! position.canHaveAsPosition(position)                
	 */
	@Raw 
	public Mole(World world, Position position, double direction, double radius, String name, Program program) 
	   throws IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction, name, program);
	}
	
	/**
	 * Returns the minimal radius of this worm.
	 */
	@Override @Basic
	public double getMinimalRadius() {
		return this.minimalRadius;
	}
	
	/**
	 * Variable registering the minimal radius of the mole.
	 */
	public final double minimalRadius = 0.25;
	
	/**
	 * Returns the maximal radius of this mole.
	 */
	@Override @Basic
	public double getMaximalRadius() {
		return this.maximalRadius;
	}
	
	/**
	 * Variable registering the maximal radius of the mole.
	 */
	public final double maximalRadius = 1;
	
	/**
	 * Sets the radius of the mole to the given value.
	 * 
	 * @param  radius
	 *		   The new radius for this mole.
	 * @effect if (radius <= getMaximalRadius()) then super.setRadius(radius)
	 * @effect if (radius > getMaximalRadius()) then this.terminate()
	 * @effect if (radius > getMaximalRadius()) then super.setRadius(getMaximalRadius())
	 */
	@Override
	public void setRadius(double radius) {
		if (radius > getMaximalRadius()) {
			radius = getMaximalRadius();
			terminate();
		}
		super.setRadius(radius);	
	}
	
	/**
	 * Returns the density of moles.
	 */
	@Override @Basic
	public double getDensity() {
		return Mole.DENSITY;
	}
	
	/**
	 * Variable registering the density of a mole that applies to all moles.
	 */
	private static final double DENSITY = 1564.5;
	 
	/**
	 * Check whether the given name is a valid name for a mole.
	 * 
	 * @param  name
	 *         The name to check.
	 * @return result == ( (name.split(" ").length == 2) && 
	 *                     for each word in name.split(" "): (word.length() >= 2) && (word.matches("[A-Z][a-zA-Z]*") )
	 */
	@Override
	public boolean isValidName(String name) {
		String[] words = name.split(" ");    
		if (words.length != 2)
			return false;
		else {
			for (String word: words)
				if (word.length() < 2 || !word.matches("[A-Z][a-zA-Z]*"))
					return false;
			return true;
		}
	}
	
	/**
	 * Returns the position to which the mole can move.
	 * 
	 * @return result == new position(getPosition().addToX(getRadius()/3*Math.cos(getDirection())).getX(), 
	 *  							  getPosition().addToY(getRadius()/3*Math.sin(getDirection())).getY() )
	 */
	@Override
	public Position getMovePosition() {
		double distance = getRadius()/3;
		Position position = new Position( getPosition().addToX(distance*Math.cos(getDirection())).getX(), 
					                      getPosition().addToY(distance*Math.sin(getDirection())).getY() );
		return position;	
	}
	
	/**
	 * Returns the cost in action points of moving the mole to the given position.
	 *  
	 * @param  position
	 * 		   The new position to which the mole wants to move.
	 * @return result = 3
	 */
	@Override
	public int costMove(Position nextPosition) {
		return 3;
	}
	
	/**
	 * Sets the effect of falling down a given distance.
	 *  
	 * @param  distance
	 * 		   The distance along which the character has fallen.
	 */
	@Override
	public void takeFallDamage(double distance) {
	}
	
	/**
	 * Eat all overlapping objects.
	 * 
	 * @effect  super.eat()
	 * @effect  getWorld().startNextTurn()
	 */
	@Override
	public void eat() {
		super.eat();
		getWorld().startNextTurn();
	}
	
	/**
	 * Returns the effect of eating the given object on the current nr of action points of this mole.
	 * 
	 * @param  object
	 *         The game object to be eaten. 
	 * @effect result =  getRadius() * (1 + (object.getRadius()/getRadius()))
	 * @throws IllegalTypeException
	 *         object.getClass() != Worm.class
	 */
	@Override
	public double getEffectOfEating(GameObject object) {
		if ( object.getClass() != Worm.class )
			throw new IllegalTypeException();
		return ( getRadius() * (1 + (object.getRadius()/getRadius())) );
	}
	
	/**
	 * Returns whether or not the given game object can be eaten by a mole.
	 * 
	 * @param  object
	 *         The game object to check.
	 * @return result == ( Worm.class.isInstance(object) && Util.fuzzyLessThanOrEqualTo(object.getRadius(), getRadius()) )
	 */
	@Override
	public boolean canEat(GameObject object) {
		return ( Worm.class.isInstance(object) && Util.fuzzyLessThanOrEqualTo(object.getRadius(), getRadius()));
	}
	
}
