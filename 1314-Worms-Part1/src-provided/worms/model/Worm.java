package worms.model;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;

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
 *         | isValidPosition(position)  
 * @author   Delphine Vandamme & Pieter Noyens
 */
public class Worm {

	/**
	 * Initialize this new worm with given name, location, direction and radius.
	 *
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
     *       | isValidDirection(direction)
     * @post   The direction of this new worm is equal to the given
     * 		   direction.
     *       | new.getDirection() == direction  
     * @post   The position of this new worm is equal to a new Position(x,y), given x and y.
     *       | new.getPosition() = new Position(x,y)
     * @post   The name of this new worm is equal to the given name.
     *       | new.getName() == name
     * @post   The radius of this new worm is equal to the given radius.
     * 		 | new.getRadius() == radius
     * @post   The number of action points of this new worm are set to 
     * 		   the maximum number of action points of this new worm.
     *       | new.getActionPoints() == getMaxActionPoints()
     * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for a worm.
	 *       | ! isValidRadius(radius)         
	 * @throws IllegalNameException
	 * 		   The given name is not a valid name for a worm.
	 *       | ! isValidName(name)  
	 * @throws IllegalPositionException
	 * 		   The given position is not a valid position for a worm.
	 *       | ! position.isValidPosition(position)  
	 * @throws IllegalDirectionException
	 * 		   The given direction is not a valid direction for a worm.
	 *       | ! isValidDirection(direction)               
	 */
	@Raw 
	public Worm(double x, double y, double direction, double radius,
		String name) 
	   throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalNameException {
		if (! isValidDirection(direction))
			throw new IllegalDirectionException();
		setPosition(new Position(x,y));
		setDirection(direction);
		setRadius(radius);
		setName(name);
		setActionPoints(getMaxActionPoints());
	}
	
	/**
	 * Returns the position of this worm.
	 */
	@Basic @Raw
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the position of this worm to the given position.
	 * 
	 * @param position
	 * 	      The new position for this worm.
	 */
	@Raw
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Returns whether the given position is a valid position for this worm.
	 * 
	 * @param position
	 * 		  The position to be checked.
	 * @return True if and only if the given position references an effective position.
     *       | result == (getPosition != null)	
	 */
	@Raw
	public boolean isValidPosition(Position position) {
		return (getPosition() != null);
	}
	/**
	 * Variable registering the position of this worm.
	 */
	private Position position;
		
	/**
	 * Returns whether or not the worm can move a given number of steps.
	 * 
	 * @param  nbSteps
	 * 		   The number of steps used to calculate the cost of moving.
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than or equal to the
	 *         cost of moving the worm a given number of steps.
	 *       |  result == ( getActionPoints() >= costMove(nbSteps) )
	 */
	public boolean canMove(int nbSteps) {
		return (getActionPoints() >= costMove(nbSteps));
	}
	
	/**
	 * Moves the worm by the given number of steps 
	 * along the current direction of the worm.
	 * 
	 * @param  nbSteps
	 * 		   The number of steps this worm has to move.
	 * @effect The x-coordinate of the worm is set by incrementing the current x-coordinate with 
	 * 		   the product of the number of steps, the radius and the x-component of the direction.
	 * 		 | getPosition().addToX(nbSteps*getRadius()*Math.cos(getDirection()))
	 * @effect The y-coordinate of the worm is set by incrementing the current y-coordinate with 
	 * 		   the product of the number of steps, the radius and the y-component of the direction.
	 * 		 | getPosition().addToY(nbSteps*getRadius()*Math.sin(getDirection()))
	 * @effect The number of action points of the worm is decreased
	 * 		   by the cost of moving the worm a number of steps.
	 * 		 | decreaseActionPoints(costMove(nbSteps))
	 * @throws IllegalPositionException
	 * 	     | The sum of the current x-coordinate and the given value is not a valid position.
	 *       | ! isValidPosition(getX() + nbSteps*getRadius()*Math.cos(getDirection()))
	 * @throws IllegalPositionException
	 * 	     | The sum of the current y-coordinate and the given value is not a valid position.
	 *       | ! isValidPosition(getY() + nbSteps*getRadius()*Math.sin(getDirection()))
	 */
	public void move(int nbSteps)
	   throws IllegalPositionException {
		setPosition(getPosition().addToX(nbSteps*getRadius()*Math.cos(getDirection())));
		setPosition(getPosition().addToY(nbSteps*getRadius()*Math.sin(getDirection())));
		decreaseActionPoints(costMove(nbSteps));
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
	public int costMove(int nbSteps) {
	    return (int) Math.ceil((Math.abs(Math.cos(getDirection())) + 
	    		4 * Math.abs(Math.sin(getDirection()))) * nbSteps);
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
    public boolean canAcceptForTurn(double angle) {
    	return ( (Util.fuzzyGreaterThanOrEqualTo(angle, -2*Math.PI))
    	      && (Util.fuzzyLessThanOrEqualTo(angle, 2*Math.PI))
    		  && (isValidDirection(restrictDirection(getDirection() + angle))) );
    }

	/**
	 * Returns whether or not the worm can turn by the given angle.
	 * 
	 * @param  angle
	 * 		   The angle used to compute the cost of turning.
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than or equal to the
	 *         cost of turning the worm by a given angle.
	 *       |  result ==
	 *       |          ( getActionPoints() >= costTurn(angle) )
	 */
	public boolean canTurn(double angle) {
		return (getActionPoints() >= costTurn(angle));
	}
	
	/**
	 * Turns the worm by the given angle.
	 * 
	 * @param  angle
	 *  	   The angle by which the worm has to turn.
	 * @effect If the given angle is a valid angle, the direction of the worm is set by adding 
	 *  	   the given angle to the current direction of the worm.
	 *  	 | setDirection(restrictDirection(getDirection() + angle))
	 * @effect If the given angle is a valid angle, the number of action points of the 
	 * 		   worm is decreased by the cost of turning the worm by the given angle.
	 * 		 | decreaseActionPoints(costTurn(angle))
	 */
	public void turn(double angle) {
		if (canAcceptForTurn(angle)){
			setDirection(restrictDirection(getDirection() + angle));
			decreaseActionPoints(costTurn(angle));
		}
	}
	
	/**
	 * Returns the cost in action points of turning the worm by a given angle.
	 * 
	 * @param  angle
	 		   The angle of which the cost of turning is to be computed.
	 * @return The cost of turning the worm by the given angle rounded up to the next integer.	   
	 * 		 | result == ( (int) Math.ceil(Math.abs(60/(2*Math.PI/angle))) )
	 */
	public int costTurn(double angle) {
		return (int) Math.ceil(Math.abs(60/(2*Math.PI/angle)));
	}
	
	/**
	 * Variable registering the current direction of this worm (in radians).
	 */
	private double direction = 0;
	
	/**
	 * Returns the radius of the worm.
	 */
	@Basic @Raw
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Check whether the given radius is a valid radius for a worm.
	 * 
	 * @param  radius
	 *         The radius to check.
	 * @return True if and only if the given radius is not below 
	 * 		   the minimal radius and not above Double.POSITIVE_INFINITY .
	 *       | result == (radius >= getMinimalRadius()) 
	 *                   && (radius <= Double.POSITIVE_INFINITY)
	 */
	public static boolean isValidRadius(double radius) {
		return (Util.fuzzyGreaterThanOrEqualTo(radius, minimalRadius)) 
				&& (Util.fuzzyLessThanOrEqualTo(radius, Double.POSITIVE_INFINITY));
	}
	
	/**
	 * Sets the radius of the worm to the given value.
	 * 
	 * @param  radius
	 *		   The new radius for this worm.
	 * @post   If the given radius is a valid radius for this worm, 
	 *         the radius of this worm is equal to the given radius.
     *       | new.getRadius() == radius
     * @throws IllegalRadiusException
     * 		   The given radius is not a valid radius for any worm.  
     * 		 | ! isValidRadius(radius)    
	 */
	@Raw
	public void setRadius(double radius)
	   throws IllegalRadiusException {
		if (! isValidRadius(radius))
			throw new IllegalRadiusException(radius,this);
		this.radius = radius;
	}
	
	/**
	 * Variable registering the radius of the worm.
	 */
	private double radius;

	/**
	 * Variable registering the minimal radius of the worm.
	 */
	public static double minimalRadius = 0.25;
	
	/**
	 * Returns the mass of the worm.
	 * 
	 * @return The mass of the worm 
	 *       | result == ( p*4/3*Math.PI*Math.pow(getRadius(), 3) )
	 */
	public Double getMass() {
		return p*4/3*Math.PI*Math.pow(getRadius(), 3);
	}

	/**
	 * Variable registering the density of a worm that applies to all worms.
	 */
	private static final int p = 1062;
	
	/**
	 * Returns the current number of action points of the worm.
	 * 
	 * @post   If the nr of action points is above the maximum nr
	 *         of action points, the nr of action points is set 
	 *         to the maximum nr of action points.
	 *       | new.getActionPoints() == getMaxActionPoints()
	 */
	public int getActionPoints() {
		if ( actionPoints > getMaxActionPoints() )
			setActionPoints(getMaxActionPoints());
		return this.actionPoints;
	}
	
	/**
	 * Constant reflecting the lowest possible value for 
	 * the action points of a worm.
	 * 
	 * @return The lowest possible value for the action points of all
	 *         worms is 0.
	 *       | result == 0
	 */
	private static final int minActionPoints = 0;
	
	/**
	 * Return the highest possible value for the action points 
	 * of this worm.
	 * 
	 * @return The highest possible value for the action points
	 *         of this worm is equal to the mass of this worm 
	 *         rounded to the nearest integer.
	 *       | result == ( getMass() + 0.5 )
	 */ 
	public int getMaxActionPoints() {
		return (int) (getMass() + 0.5);
	}
	
	/**
	 * Returns the current number of action points of the worm.
	 * 
	 * @param  actionPoints
	 *		   The new number of action points for this worm.
	 * @post    If the given number of action points are in the range established by the minimum
	 *          and maximum number of action points for this worm, the number of action points
	 *          of this worm are equal to the given number of action points.
	 *        | if ( (action points >= getMinActionPoints()) && (actionPoints <= getMaxActionPoints()) )
	 *        |   then new.getActionPoints() == actionPoints
	 * @post   If the given number of action points exceeds the maximum number of action points for	
	 * 		   this worm, the number of action points of this worm
	 * 		   are equal to the the highest possible value for the number of action points of this worm.
	 *       | if (actionPoints> getMaxActionPoints())
	 *       |   then new.getActionPoints() == getMaxActionPoints() 
	 * @post   If the given number of action points are below the minimum number of 
	 * 		   action points for this worm, the number of action points of this worm 
	 * 		   are equal to the lowest possible value for the number of action points of this worm.
	 *        | if (actionPoints < getMinActionPoints())
	 *        |     then new.getActionPoints() = this.getMinActionPoints()     
	 */
	public void setActionPoints(int actionPoints) {
		if (actionPoints > getMaxActionPoints())
			this.actionPoints = getMaxActionPoints();
		else if (actionPoints < minActionPoints)
			this.actionPoints = minActionPoints;
		else 
			this.actionPoints = actionPoints;
	}
	
	/**
	 * Decreases the current number of action points of the worm with 
	 * the given number of points.
	 * 
	 * @param  actionPoints
	 * 		   The number of action points to be subtracted from the 
	 * 		   current number of action points.
	 * @post   If the given nr of action points is positive and if 
	 * 		   the old nr of action points of this worm is not below 
	 * 		   the minimum nr of action points incremented with the given 
	 * 		   nr of action points, the new nr of action points is equal to
	 * 		   the old nr of action points decremented with the given nr of action points.
	 *       | setActionPoints(getActionPoints() - actionPoints)
	 * @post   If the given nr of action points is positive and if 
	 * 		   the old nr of action points of this worm is below 
	 * 		   the minimum nr of action points incremented with the given 
	 * 		   nr of action points, the new nr of action points is equal to
	 * 		   the minimum action points.
	 *       | setActionPoints(minActionPoints)
	 */
	public void decreaseActionPoints(int actionPoints) {
		if ( (actionPoints > 0) 
		   && (getActionPoints() >= minActionPoints + actionPoints ) )
			setActionPoints(getActionPoints() - actionPoints);
		else if ( ( (actionPoints > 0) 
		   && (getActionPoints() <= minActionPoints + actionPoints ) ))
			setActionPoints(minActionPoints);
	}
	
	/**
	 * Variable registering the current number of action points of the worm.
	 */
	private int actionPoints;
	
	/**
	 * Returns the name the worm.
	 */
	@Basic @Raw
	public String getName() {
		return this.name;
	}
	
	/**
	 * Check whether the given name is a valid name for a worm.
	 * 
	 * @param  name
	 *         The name to check.
	 * @return True if and only if the given name only contains characters a-z, A-Z, ', " 
	 * 		   and spaces and if the first letter is in upperscore.
	 *       | result == 
	 *       |        (name.length() >= 2) && (name.matches("[A-Z][a-zA-Z\'\" ]*"))
	 */
	public static boolean isValidName(String name) {
		return (name.length() >= 2) && (name.matches("[A-Z][a-zA-Z\'\" ]*"));
	}
	
	/**
	 * Sets the name of the worm to the given string.
	 * 
	 * @param  name
	 *		   The new name for this worm.
	 * @throws IllegalNameException
	 *         The name is against the predefined rules.
	 *        | ! isValidName(name)
	 */
	@Raw
	public void setName(String name)
	   throws IllegalNameException {
		if (! isValidName(name))
			throw new IllegalNameException(name,this);
		this.name = name;
	}
	
	/**
	 * Variable registering the name of the worm.
	 */
	private String name;
	
	/**
	 * Variable registering the Earth’s standard acceleration that applies to all worms.
	 */
	private static final double G = 9.80665;

	/**
	 * Returns whether or not the worm can jump.
	 * 
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than the minimum number 
	 * 		   of action points, the direction does not exceed Pi and is not less than zero.
	 *       |  result == (getActionPoints() >= minActionPoints &&  getDirection() >= 0 
	 *		 |		       && Util.fuzzyLessThanOrEqualTo(getDirection(), Math.PI)      
	 */
	public boolean canJump() {
		return ( (getActionPoints() > minActionPoints) 
				&& (getDirection() >= 0) 
				&& (Util.fuzzyLessThanOrEqualTo(getDirection(), Math.PI)) );
	}
	
	/**
	 * Changes the position of the worm as the result of a jump 
	 * from the current position (x,y) and with respect to the 
	 * worm’s direction and the number of remaining action points.
	 * 
	 * @effect If the worm can jump, the jump distance is added to the
	 * 		   current x-coordinate of the position of this worm.
	 *  	 | getPosition().addToX(distanceJump())
	 * @effect The number of action points of the worm is set to the minimum value.
	 * 		 | setActionPoints(minActionPoints)
	 * @throws IllegalJumpException
	 *         The worm is unable to jump.
	 *       | ! canJump() 
	 */
	public void jump()
	   throws IllegalJumpException {
		if (! canJump())
			throw new IllegalJumpException();
		setPosition(getPosition().addToX(distanceJump()));
		setActionPoints(minActionPoints);
	}
	
	/**
	 * Returns the above time for a potential jump from the current position.
	 * 
	 * @return The jump time equals the jump distance divided by the initial velocity.
	 * 		 | result == distanceJump() / initialVelocityX()
	 * @throws ArithmeticException
	 *         Division by zero.
	 *       | initialVelocityX() == 0
	 */
	public double jumpTime()
	   throws ArithmeticException {
		if (initialVelocityX() == 0)
			throw new ArithmeticException();
		return distanceJump() / initialVelocityX();
	}
	
	/**
	 * Returns in-flight positions of a jumping worm at any dt seconds after launch.
	 * 
	 * @param  dt
	 * 		   Number of seconds after launch.
	 * @return If the worm can jump, an array of the in-flight positions (xdt, ydt) is returned. 
	 * 		   The x-position equals the sum of the current x-coordinate of the position, 
	 *         and the product of the initial velocity and time dt. 
	 *         The y-position equals the sum of the current y-coordinate of the position, 
	 *         and the product of the initial velocity and time dt, diminished with (0.5*G*dt^2).
	 *       | return == { (getPosition().getX() + (initialVelocityX()*dt)), 
	 *                     (getPosition().getY() + (initialVelocityY()*dt) - 0.5*G*Math.pow(dt, 2)) }
	 * @throws IllegalJumpException
	 *         The action cannot be performed because the worm cannot jump.
	 *       | ! canJump()
	 */
	public double[] jumpStep(double dt)
	   throws IllegalJumpException {
		if (! canJump())
			throw new IllegalJumpException();	
		double xdt = getPosition().getX() + (initialVelocityX()*dt);
		double ydt = getPosition().getY() + (initialVelocityY()*dt) - 0.5*G*Math.pow(dt, 2);
		double[] dposition = {xdt, ydt};
		return dposition;
	}
	
	/**
	 * Returns the force exerted on the worm.
	 * 
	 * @return The force equals the sum of five times 
	 * 		   the action points of this worm and the product 
	 *         of the mass of the worm and G.
	 *       | return == (5*getActionPoints() + getMass()*G)
	 */
	public double force() {
		return 5*getActionPoints() + getMass()*G;
	}
	
	/**
	 * Returns the initial velocity of the worm. 
	 * 
	 * @return The initial velocity equals 0.5 times the force 
	 *         on the worm divided by the mass of the worm.
	 *       | result == ( force() * 0.5 / getMass() )
	 * @throws ArithmeticException
	 *         Division by zero.
	 *       | getMass() == 0
	 */
	public double initialVelocity()
	   throws ArithmeticException {
		if (getMass() == 0)
			throw new ArithmeticException();
		return force() * 0.5 / getMass();
	}
	
	/**
	 * Returns the x-component of the initial velocity of the worm
	 * 
	 * @return The x-component of the initial velocity equals the 
	 * 	       product of the initial velocity of the worm and the 
	 *         cosine of the direction of the worm.
	 *  	 | result == ( initialVelocity() * Math.cos(getDirection()) )
	 */
	public double initialVelocityX() {
		return initialVelocity() * Math.cos(getDirection());
	}
	
	/**
	 * Returns the y-component of the initial velocity of the worm
	 * 
	 * @return The y-component of the initial velocity equals the 
	 * 	       product of the initial velocity of the worm and the 
	 *         sine of the direction of the worm.
	 *       | result == ( initialVelocity() * Math.sin(getDirection()) )
	 */
	public double initialVelocityY() {
		return initialVelocity() * Math.sin(getDirection());
	}
	
	/**
	 * Returns the distance the worm can jump.
	 * 
	 * @return The distance the worm can jump is equal to the square 
	 *         of the initial velocity multiplied by the sine of 2 
	 *         times the direction, divided by G.
	 *       | result == ( Math.pow(initialVelocity(), 2) *
				Math.sin(2*getDirection())/G )
	 */
	public double distanceJump() {
		return Math.pow(initialVelocity(), 2) *
				Math.sin(2*getDirection())/G;
	}
}
