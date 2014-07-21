package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import worms.exceptions.IllegalDirectionException;
import worms.exceptions.IllegalPositionException;
import worms.exceptions.IllegalRadiusException;
import worms.util.Util;

public abstract class MoveableGameObject extends GameObject {

	protected MoveableGameObject(World world, Position position, double radius, double direction)
			throws  IllegalDirectionException, IllegalPositionException, IllegalRadiusException {
		super(world, position, radius);
		if (! isValidDirection(direction))
			throw new IllegalDirectionException();
		setDirection(direction);
	}
	
	/**
	 * Returns the current direction of this worm (in radians).
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
     * Checks whether the given direction is a valid direction for any worm.
     *  
     * @param  direction
     * 		   The direction to check.
     * @return True if and only if the given direction is greater than or equal to
     *         0 and less than or equal to 2*Pi.
     *       | result ==
     *       |   (direction >= 0) && (direction <= 2*Math.PI)	
     */
    public static boolean isValidDirection(double direction) {
        return direction >= 0 && 
        	   Util.fuzzyLessThanOrEqualTo(direction, 2*Math.PI);
    }
    
	/**
	 * Sets the current direction of the worm to the given value.
	 * 
	 * @param  direction
     * 		   The new direction for this worm.
     * @pre    The given direction must be a valid direction for a worm.
     *       | isValidDirection(direction)
     * @post   The direction of this worm is equal to the given
     * 		   direction.
     *       | new.getDirection() == direction
	 */
    @Raw
	public void setDirection(double direction) {
    	assert isValidDirection(direction);
		this.direction = direction;
	}
    
    /**
     * Returns whether or not the angle is accepted for a turn of this worm.
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
	 * Turns the worm by the given angle.
	 * 
	 * @param  angle
	 *  	   The angle by which the worm has to turn.
	 * @pre    The direction must be a valid direction for a worm.
     *       | isValidDirection(getDirection() + angle)
	 * @effect If the given angle is a valid angle, the direction of the worm is set by adding 
	 *  	   the given angle to the current direction of the worm.
	 *  	 | setDirection(restrictDirection(getDirection() + angle))
	 */
	public void turn(double angle) {
		if (canTurn(angle)){
			setDirection(restrictDirection(getDirection() + angle));
		}
	}
	
	/**
	 * Variable registering the current direction of this worm (in radians).
	 */
	private double direction = 0;
	
	/**
	 * Returns the mass of the worm.
	 * 
	 * @return The mass of the worm 
	 *       | result == ( p*4/3*Math.PI*Math.pow(getRadius(), 3) )
	 */
	public double getMass() {
		return getDensity()*4/3*Math.PI*Math.pow(getRadius(), 3);
	}
	
	public abstract double getDensity();
	

}
