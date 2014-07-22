package worms.model;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;

/**
 * A class of worms involving a name, position, direction, radius, 
 * mass and action points.
 * 
 * @invar  The direction of each worm must be a valid direction for a worm.
 *         | isValidDirection(getDirection())
 * @invar  The name of each worm must be a valid name for a worm.
 *         | isValidName(getName())         
 * @invar  The radius of each worm must be a valid radius for a worm.
 *         | isValidRadius(getRadius())
 * @invar  The position of each worm must be a valid position for a worm.
 *         | canHaveAsPosition(getPosition())  
 * @invar  The current number of action points of each worm must be a valid number of
 *         action points for a worm.
 *         | canHaveAsPoints(getActionPoints())   
 * @invar  The current number of hit points of each worm must be a valid number of
 *         hit points for a worm.
 *         | canHaveAsPoints(getHitPoints())           
 * @author   Delphine Vandamme 
 */
public class Mole extends Character {

	/**
	 * Initialize this new worm with given name, location, direction and radius.
	 *
	 * @param  world
	 * 		   The world in which to place the created worm  
	 * @param  x
	 * 		   The x-coordinate of the position of this new worm (in meter)
	 * @param  y
	 * 	       The y-coordinate of the position of this new worm (in meter)
	 * @param  direction
	 * 		   The direction of the new worm (in radians)
	 * @param  radius 
	 * 		   The radius of the new worm (in meter)
	 * @param  name
	 * 		   The name of the new worm
	 * @pre	   The given direction must be a valid direction for a worm.
     *       | isValidDirection(direction) µ
     * @post   No weapons are attached to this new worm.
	 * 	     | new.getNbWeapons() == 0 
     * @effect The direction of this new worm is equal to the given
     * 		   direction.
     *       | setDirection(direction)
     * @effect The name of this new worm is equal to the given name.
     *       | setName(name)
     * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for a worm.
	 *       | ! isValidRadius(radius)         
	 * @throws IllegalNameException
	 * 		   The given name is not a valid name for a worm.
	 *       | ! isValidName(name)  
	 * @throws IllegalPositionException
	 * 		   The given position is not a valid position for a worm.
	 *       | ! position.canHaveAsPosition(position)  
	 * @throws IllegalDirectionException
	 * 		   The given direction is not a valid direction for a worm.
	 *       | ! isValidDirection(direction)               
	 */
	@Raw 
	public Mole(World world, Position position, double direction, double radius, String name, Program program) 
	   throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction, name, program);
	}
	
	/**
	 * 
	 */
	@Override
	public double getMinimalRadius() {
		return 0.25;
	}
	
	/**
	 * 
	 */
	@Override
	public double getMaximalRadius() {
		return 1.0;
	}
	
	@Override
	public void setRadius(double radius) {
		if (radius > getMaximalRadius()) 
			radius = getMaximalRadius();
		super.setRadius(radius);
		terminate();
	}
	
	/**
	 * Returns the mass of the worm.
	 * 
	 * @return The mass of the worm 
	 *       | result == ( p*4/3*Math.PI*Math.pow(getRadius(), 3) )
	 */
	public double getDensity() {
		return Mole.DENSITY;
	}
	
	/**
	 * Variable registering the density of a worm that applies to all worms.
	 */
	private static final double DENSITY = 1564.5;
	
	/**
	 * Check whether the given name is a valid name for a worm.
	 * 
	 * @param  name
	 *         The name to check.
	 * @return True if and only if the given name only contains characters a-z, A-Z, ', ", 
	 *         spaces and numbers 0-9 and if the first letter is in upper score.
	 *       | result == 
	 *       |        (name.length() >= 2) && (name.matches("[A-Z][a-zA-Z0-9\'\" ]*"))
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
	 * 
	 * @return
	 */
	@Override
	public Position getMovePosition() {
		double distance = getRadius()/3;
		Position position = new Position( getPosition().addToX(distance*Math.cos(getDirection())).getX(), 
					                         getPosition().addToY(distance*Math.sin(getDirection())).getY() );
		return position;	
	}
	
	/**
	 * Returns the cost in action points of moving the worm a number of steps.
	 *  
	 * @param  nbSteps
	 * 		   The number of steps of which the cost is to be computed.
	 * @return The cost of moving the worm a number of steps rounded up to the next integer.
	 * 	     | result == ( (int) Math.ceil((Math.abs(Math.cos(getDirection())) + 
	    		4 * Math.abs(Math.sin(getDirection())))) ) 
	 */
	@Override
	public int costMove(Position nextPosition) {
		return 3;
	}
	
	@Override
	public void takeFallDamage(double distance) {
	}
	
	/**
	 * 
	 */
	@Override
	public void eat() {
		super.eat();
		getWorld().startNextTurn();
	}
	
	/**
	 * 
	 */
	@Override
	public double getEffectOfEating(GameObject object) {
		return ( getRadius() * (1 + (object.getRadius()/getRadius())) );
	}
	
	/**
	 * 
	 */
	@Override
	public boolean canEat(GameObject object) {
		return ( Worm.class.isInstance(object) && Util.fuzzyLessThanOrEqualTo(object.getRadius(), getRadius()));
	}
	
}
