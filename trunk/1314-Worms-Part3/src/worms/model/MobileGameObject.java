package worms.model;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import worms.exceptions.IllegalPositionException;
import worms.exceptions.IllegalRadiusException;
import worms.util.Util;

/**
 * A class of mobile game objects with a direction and a mass. Mobile game objects can turn.
 * 
 * @invar  The direction of each mobile game object must be a valid direction for a mobile game object.
 *         | isValidDirection(getDirection())       
 *                  
 * @author   Delphine Vandamme 
 */
public abstract class MobileGameObject extends GameObject {

	/**
	 * Initialize this new game object with given position(x,y), given radius, given direction 
	 * and given world to attach to.
	 * 
	 * @param  radius 
	 * 		   The radius of the new mobile game object (in meter)
	 * @param  world
	 * 		   The world in which to place the created mobile game object  
	 * @param  position
	 * 		   The position of this new game object (in meter)
	 * @param  direction
	 *         The direction of this new mobile game object
	 * @pre	   The given direction must be a valid direction for a mobile game object.
     *       | isValidDirection(direction)
     * @effect The direction of this new mobile game object is equal to the given direction.
     *       | setDirection(direction)
	 * @effect if (world != null)
	 *           then world.addAsGameObject(this)
	 * @effect The position of this new mobile game object is equal to the given position.
     *       | setPosition(position)
     * @effect The radius of this new mobile game object is equal to the given radius.
     * 		 | setRadius(radius) 
     * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for a mobile game object.
	 *       | ! isValidRadius(radius) 
	 * @throws IllegalPositionException
	 * 		   The given position is not a valid position for a mobile game object.
	 *       | ! position.canHaveAsPosition(position)
	 */
	public MobileGameObject(World world, Position position, double radius, double direction)
			throws IllegalPositionException, IllegalRadiusException {
		super(world, position, radius);
		
		setDirection(direction);
	}
	
	/**
	 * Returns the current direction of this mobile game object (in radians).
	 */
	@Basic @Raw
	public double getDirection() {
		return this.direction;
	}
	
    /**
	 * Returns the direction restricted to the interval [0, 2pi).
	 * 
	 * @param  direction
	 * 		   The direction to be restricted.
	 * @return If the direction is less than zero, the direction is restricted to [0,2pi).
	 * 		 | result == ( while (Util.fuzzyLessThanOrEqualTo(direction, 0)) {
			             direction += 2 * Math.PI )
	 * @return If the direction is above 2*Pi, the direction is restricted to [0,2pi).
	 * 		 | result == ( while (Util.fuzzyLessThanOrEqualTo(direction, 0)) {
	 *		             direction += 2 * Math.PI )          
	 */
    public static double restrictDirection(double direction) {
    	while (Util.fuzzyLessThanOrEqualTo(direction, 0)) {
			direction += 2 * Math.PI;
		}
		double max = 2 * Math.PI;
		while (Util.fuzzyGreaterThanOrEqualTo(direction, max)) {
			direction -= 2 * Math.PI;
		}
		return direction;
	}
	
	/**
     * Checks whether the given direction is a valid direction for any mobile game object.
     *  
     * @param  direction
     * 		   The direction to check.
     * @return True if and only if the given direction is greater than or equal to
     *         0 and less than or equal to 2*Pi.
     *       | result == (direction >= 0) && (direction <= 2*Math.PI)	
     */
    public static boolean isValidDirection(double direction) {
        return direction >= 0 && Util.fuzzyLessThanOrEqualTo(direction, 2*Math.PI);
    }
    
	/**
	 * Sets the current direction of the mobile game object to the given value.
	 * 
	 * @param  direction
     * 		   The new direction for this mobile game object.
     * @pre    The given direction must be a valid direction for a mobile game object.
     *       | isValidDirection(direction)
     * @post   The direction of this mobile game object is equal to the given
     * 		   direction.
     *       | new.getDirection() == direction
	 */
    @Raw
	public void setDirection(double direction) {
    	assert isValidDirection(direction);
		this.direction = direction;
	}
    
    /**
     * Returns whether or not the angle is accepted for a turn of this mobile game object.
     * 
     * @param  angle
     *         The angle used to determine if it's accepted for a turn.
     * @return True if and only if the angle is not below -2*Pi and does not exceed 2*Pi, 
     *         and if the sum of the current direction and the given angle is a valid direction.
     *       |  result ==
     *       |     ( (Util.fuzzyGreaterThanOrEqualTo(angle, -2*Math.PI))
    	     |      && (Util.fuzzyLessThanOrEqualTo(angle, 2*Math.PI))
    		 |      && (isValidDirection(restrictDirection(getDirection() + angle))) )
     */
    public boolean canTurn(double angle) {
    	return ( (Util.fuzzyGreaterThanOrEqualTo(angle, -2*Math.PI))
    	      && (Util.fuzzyLessThanOrEqualTo(angle, 2*Math.PI))
    		  && (isValidDirection(restrictDirection(getDirection() + angle))) );
    }
	
	/**
	 * Turns the mobile game object by the given angle.
	 * 
	 * @param  angle
	 *  	   The angle by which the mobile game object has to turn.
	 * @pre    The direction must be a valid direction for a mobile game object.
     *       | isValidDirection(getDirection() + angle)
	 * @effect If the given angle is a valid angle, the direction of the mobile game object is set by adding 
	 *  	   the given angle to the current direction of the mobile game object.
	 *  	 | setDirection(restrictDirection(getDirection() + angle))
	 */
	public void turn(double angle) {
		if (canTurn(angle)){
			setDirection(restrictDirection(getDirection() + angle));
		}
	}
	
	/**
	 * Variable registering the current direction of this mobile game object (in radians).
	 */
	private double direction = 0;
	
	/**
	 * Returns the mass of the mobile game object.
	 * 
	 * @return The mass of the mobile game object 
	 *       | result == ( p*4/3*Math.PI*Math.pow(getRadius(), 3) )
	 */
	public double getMass() {
		return getDensity()*4/3*Math.PI*Math.pow(getRadius(), 3);
	}
	
	/**
	 * Check whether the given mass is a valid mass for a mobile game object.
	 * 
	 * @param  mass
	 *         The mass to check.
	 * @return True if and only if the given mass is not below 
	 * 		   the Integer.MIN_VALUE and not above Integer.MAX_VALUE.
	 *       | result == (mass >= Integer.MIN_VALUE) && (mass <= Integer.MAX_VALUE)
	 */
	public static boolean isValidMass(double mass) {
		return (Util.fuzzyGreaterThanOrEqualTo(mass, Integer.MIN_VALUE)) 
			   && (Util.fuzzyLessThanOrEqualTo(mass, Integer.MAX_VALUE));
	}
	
	/**
	 * Returns the density of this mobile game object.
	 */
	@Basic
	public abstract double getDensity();
	

}