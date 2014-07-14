package worms.model;
import java.util.*;

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
public class Worm extends MoveableGameObject {

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
     *       | isValidDirection(direction) �
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
	public Worm(World world, Position position, double direction, double radius,
		String name) 
	   throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction);
		setName(name);
		setHitPoints(getMaxPoints());
		setActionPoints(getMaxPoints());
		setWeapons();
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
	 * @pre    The direction must be a valid direction for a worm.
     *       | isValidDirection(getDirection() + angle)
	 * @effect If the given angle is a valid angle, the direction of the worm is set by adding 
	 *  	   the given angle to the current direction of the worm.
	 *  	 | setDirection(restrictDirection(getDirection() + angle))
	 * @effect If the given angle is a valid angle, the number of action points of the 
	 * 		   worm is decreased by the cost of turning the worm by the given angle.
	 * 		 | decreaseActionPoints(costTurn(angle))
	 */
	public void turn(double angle) {
		super.turn(angle);
		decreaseActionPoints(costTurn(angle));
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
	 * 
	 */
	@Override
	public double getMinimalRadius() {
		return this.minimalRadius;
	}
	
	/**
	 * Variable registering the minimal radius of the worm.
	 */
	public final double minimalRadius = 0.25;
	
	/**
	 * Returns the mass of the worm.
	 * 
	 * @return The mass of the worm 
	 *       | result == ( p*4/3*Math.PI*Math.pow(getRadius(), 3) )
	 */
	public double getMass() {
		return p*4/3*Math.PI*Math.pow(getRadius(), 3);
	}
	
	/**
	 * Variable registering the density of a worm that applies to all worms.
	 */
	private final int p = 1062;
	
	/**
	 * Returns The current number of action points of the worm.
	 * 
	 * @return The current number of action points of the worm.
	 *  	 | result == this.actionPoints
	 * @effect If the nr of action points is above the maximum nr
	 *         of action points, the nr of action points is set 
	 *         to the maximum nr of action points.
	 *       | setMaxActionPoints(getMaxPoints())
	 */
	public int getActionPoints() {
		if ( actionPoints > getMaxPoints() )
			setActionPoints(getMaxPoints());
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
	private static final int minPoints = 0;
	
	/**
	 * Return the highest possible value for the action points 
	 * of this worm.
	 * 
	 * @return The highest possible value for the action points
	 *         of this worm is equal to the mass of this worm 
	 *         rounded to the nearest integer.
	 *       | result == (int) Math.round(getMass())
	 */ 
	public int getMaxPoints() 
	   throws IllegalMassException {
		if (! isValidMass(getMass()))
			throw new IllegalMassException();			
		return (int) Math.round(getMass());
	}
	
	/**
     * Checks whether the given direction is a valid direction for any worm.
     *  
     * @param  actionPoints
     * 		   The number of action points to check.
     * @return True if and only if the given nr of action points is greater than or equal to
     *         the minimum nr of action points and less than or equal to the maximum
     *         nr of action points.
     *       | result ==
     *       |   (actionPoints >= minActionPoints) && (actionPoints <= getMaxPoints())	
     */
	@Raw
    public boolean canHaveAsPoints(int points) {
        return points >= minPoints && 
        	   points <= getMaxPoints();
    }
	
	/**
	 * Returns the current number of action points of the worm.
	 * 
	 * @param  actionPoints
	 *		   The new number of action points for this worm.
	 * @post    If the given number of action points are in the range established by the minimum
	 *          and maximum number of action points for this worm, the number of action points
	 *          of this worm are equal to the given number of action points.
	 *        | if ( (action points >= getMinPoints()) && (actionPoints <= getMaxPoints()) )
	 *        |   then new.getActionPoints() == actionPoints
	 * @post   If the given number of action points exceeds the maximum number of action points for	
	 * 		   this worm, the number of action points of this worm
	 * 		   are equal to the the highest possible value for the number of action points of this worm.
	 *       | if (actionPoints> getMaxPoints())
	 *       |   then new.getActionPoints() == getMaxPoints() 
	 * @post   If the given number of action points are below the minimum number of 
	 * 		   action points for this worm, the number of action points of this worm 
	 * 		   are equal to the lowest possible value for the number of action points of this worm.
	 *        | if (actionPoints < getMinPoints())
	 *        |     then new.getActionPoints() = this.getMinPoints()     
	 */
	public void setActionPoints(int actionPoints) {
		if (actionPoints > getMaxPoints())
			this.actionPoints = getMaxPoints();
		else if (actionPoints <= minPoints) {
			this.actionPoints = minPoints;
			getWorld().startNextTurn();
		}
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
	 * @effect If the given nr of action points is positive and if 
	 * 		   the old nr of action points of this worm is not below 
	 * 		   the minimum nr of action points incremented with the given 
	 * 		   nr of action points, the new nr of action points is equal to
	 * 		   the old nr of action points decremented with the given nr of action points.
	 *       | setActionPoints(getActionPoints() - actionPoints)
	 * @effect If the given nr of action points is positive and if 
	 * 		   the old nr of action points of this worm is below 
	 * 		   the minimum nr of action points incremented with the given 
	 * 		   nr of action points, the new nr of action points is equal to
	 * 		   the minimum action points.
	 *       | setActionPoints(minActionPoints)
	 */
	public void decreaseActionPoints(int actionPoints) {
		if ( (actionPoints > 0) 
		   && (getActionPoints() >= minPoints + actionPoints ) )
			setActionPoints(getActionPoints() - actionPoints);
		else if ( ( (actionPoints > 0) 
		   && (getActionPoints() <= minPoints + actionPoints ) ))
			setActionPoints(minPoints);
	}
	
	/**
	 * Variable registering the current number of action points of the worm.
	 */
	private int actionPoints;
	
	/**
	 * Returns The current number of hit points of the worm.
	 * 
	 * @return The current number of hit points of the worm.
	 *  	 | result == this.hitPoints
	 * @effect If the nr of hit points is above the maximum nr
	 *         of hit points, the nr of hit points is set 
	 *         to the maximum nr of hit points.
	 *       | setHitPoints(getMaxPoints())
	 */
	public int getHitPoints() {
		if ( hitPoints > getMaxPoints() )
			setHitPoints(getMaxPoints());
		//if ( (hitPoints == minPoints) )
			
		return this.hitPoints;
	}
	
	/**
	 * Returns the current number of hit points of the worm.
	 * 
	 * @param  hitPoints
	 *		   The new number of hit points for this worm.
	 * @post    If the given number of hit points are in the range established by the minimum
	 *          and maximum number of hit points for this worm, the number of hit points
	 *          of this worm are equal to the given number of hit points.
	 *        | if ( (hit points >= getMinPoints()) && (hitPoints <= getMaxPoints()) )
	 *        |   then new.getHitPoints() == hitPoints
	 * @post   If the given number of hit points exceeds the maximum number of hit points for	
	 * 		   this worm, the number of hit points of this worm
	 * 		   are equal to the the highest possible value for the number of hit points of this worm.
	 *       | if (hitPoints> getMaxPoints())
	 *       |   then new.getHitPoints() == getMaxPoints() 
	 * @post   If the given number of hit points are below the minimum number of 
	 * 		   hit points for this worm, the number of hit points of this worm 
	 * 		   are equal to the lowest possible value for the number of hit points of this worm.
	 *        | if (hitPoints < getMinPoints())
	 *        |     then new.getHitPoints() = this.getMinPoints()     
	 */
	public void setHitPoints(int hitPoints) {
		if (hitPoints > getMaxPoints())
			this.hitPoints = getMaxPoints();
		else if (hitPoints <= minPoints) {
			this.hitPoints = minPoints;
			terminate();
		}
		else 
			this.hitPoints = hitPoints;
	}
	
	/**
	 * Decreases the current number of hit points of the worm with 
	 * the given number of points.
	 * 
	 * @param  hitPoints
	 * 		   The number of hit points to be subtracted from the 
	 * 		   current number of hit points.
	 * @effect If the given nr of hit points is positive and if 
	 * 		   the old nr of hit points of this worm is not below 
	 * 		   the minimum nr of hit points incremented with the given 
	 * 		   nr of hit points, the new nr of hit points is equal to
	 * 		   the old nr of hit points decremented with the given nr of hit points.
	 *       | setHitPoints(getHitPoints() - hitPoints)
	 * @effect If the given nr of hit points is positive and if 
	 * 		   the old nr of hit points of this worm is below 
	 * 		   the minimum nr of hit points incremented with the given 
	 * 		   nr of hit points, the new nr of hit points is equal to
	 * 		   the minimum hit points.
	 *       | setActionPoints(minActionPoints)
	 */
	public void decreaseHitPoints(int hitPoints) {
		if ( (hitPoints > 0) 
		   && (getHitPoints() >= minPoints + hitPoints ) )
			setHitPoints(getHitPoints() - hitPoints);
		else if ( ( (hitPoints > 0) 
		   && (getHitPoints() <= minPoints + hitPoints ) ))
			setHitPoints(minPoints);
	}
	
	/**
	 * Variable registering the current number of hit points of the worm.
	 */
	private int hitPoints;
	
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
	 * @return True if and only if the given name only contains characters a-z, A-Z, ', ", 
	 *         spaces and numbers 0-9 and if the first letter is in upper score.
	 *       | result == 
	 *       |        (name.length() >= 2) && (name.matches("[A-Z][a-zA-Z0-9\'\" ]*"))
	 */
	public static boolean isValidName(String name) {
		return (name.length() >= 2) && (name.matches("[A-Z][a-zA-Z0-9\'\" ]*"));
	}
	
	/**
	 * Sets the name of the worm to the given string.
	 * 
	 * @param  name
	 *		   The new name for this worm.
	 * @post   The name of this new worm is equal to the given name.
     *       | new.getName() == name
	 * @throws IllegalNameException
	 *         The name is against the predefined rules.
	 *        | ! isValidName(name)
	 */
	@Raw
	public void setName(String name)
	   throws IllegalNameException {
		if (! isValidName(name))
			throw new IllegalNameException(name);
		this.name = name;
	}
	
	/**
	 * Variable registering the name of the worm.
	 */
	private String name;
	
	/**
	 * Variable registering the Earth�s standard acceleration that applies to all worms.
	 */
	private static final double G = 9.80665;

	
	/**
	 * Returns whether or not the worm can move a given number of steps.
	 * 
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than or equal to the
	 *         cost of moving the worm a given number of steps.
	 *       |  result == ( getActionPoints() >= costMove(nbSteps) )
	 */
	public boolean canMove() {
		Position nextPosition = getAdjacentorPassablePosition();
		if (nextPosition == null)
			return false;
		else if ( getActionPoints() <= costMove(nextPosition) ) 
			return false;
		else 
			return true;
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
	 *       | ! canHaveAsPosition(getX() + nbSteps*getRadius()*Math.cos(getDirection()))
	 * @throws IllegalPositionException
	 * 	     | The sum of the current y-coordinate and the given value is not a valid position.
	 *       | ! canHaveAsPosition(getY() + nbSteps*getRadius()*Math.sin(getDirection()))
	 */
	public void move()
	   throws IllegalPositionException {
		if (canMove()) {
			Position newPos = getAdjacentorPassablePosition();
			decreaseActionPoints(costMove(newPos));
			setPosition(newPos);
			eat();
			//if (canFall())
			//	fall();
		}
	}
	
	public Position getAdjacentorPassablePosition() 
	  throws IllegalPositionException {
		double distance = getRadius();
		double divergence = 0;
		double step = Math.min(getWorld().getPixelWidth(), getWorld().getPixelHeight());
		Position passablePos = null;
		while (Util.fuzzyGreaterThanOrEqualTo(distance, 0.1)) {
			Position tempPos = new Position(getPosition().addToX(distance*Math.cos(getDirection())).getX(),
					getPosition().addToY(distance*Math.sin(getDirection())).getY());
			if (getWorld().isAdjacent(tempPos.getX(), tempPos.getY(),getRadius()))
				return tempPos;	
			while (Util.fuzzyLessThanOrEqualTo(divergence, 0.7875)) {
				Position tempPosDecrease = getPosition().rotateBy(-divergence, distance, getDirection());
				if (getWorld().isAdjacent(tempPosDecrease.getX(), tempPosDecrease.getY(),getRadius()))
					return tempPosDecrease;
				Position tempPosIncrease = getPosition().rotateBy(divergence, distance, getDirection());
				if (getWorld().isAdjacent(tempPosIncrease.getX(), tempPosIncrease.getY(), getRadius()))
					return tempPosIncrease;
				divergence += 0.0175;
			}
			distance -= step;
			divergence = 0;
			if (passablePos == null)
				if (! getWorld().isImpassable(tempPos.getX(), tempPos.getY(), getRadius()))
					passablePos = tempPos;
		}
		return passablePos;		
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
	public int costMove(Position nextPosition) {
		double slope = getPosition().getSlope(nextPosition);
		double cost = Math.ceil((Math.abs(Math.cos(slope)) + 4 * Math.abs(Math.sin(slope))));
		if (cost < Integer.MAX_VALUE)
			return (int) cost;
		else
			return Integer.MAX_VALUE;
	}
	
	/**
	 * Returns whether or not the given worm can fall down
	 */
	public boolean canFall() {
		return  (!getWorld().isAdjacent(getPosition().getX(), getPosition().getY(), getRadius()) 
				  && !getWorld().isImpassable(getPosition().getX(), getPosition().getY(), getRadius())
			   || !getWorld().isInWorld(getPosition().getX(), getPosition().getY(), getRadius())) ;
	}
	
	/**
	 * Makes the given worm fall down until it rests on impassable terrain again.
	 */
	public void fall() {
		if (!canFall()) 
			throw new IllegalStateException("Cannot fall!");
		double step = getWorld().getPixelHeight();
		double distance = step;
		Position tempPos = getPosition().addToY(-distance);
		int cntr = 0;
		while ( (!getWorld().isImpassable(tempPos.getX(), tempPos.getY(), getRadius())) 
			    && (! getWorld().isAdjacent(tempPos.getX(), tempPos.getY(), getRadius()))) {
			distance += step;
			tempPos = getPosition().addToY(-distance);
			cntr += 1;
		}
		setPosition(tempPos); 
		decreaseHitPoints(costFall(step*cntr));
		eat();
		if (! getWorld().isInWorld(tempPos.getX(), tempPos.getY(), getRadius()) ) 
			terminate();//decreaseHitPoints(getHitPoints());
	}
	
	/**
	 * Returns the cost in hit points of falling down a certain distance.
	 *  
	 * @param  distance
	 * 		   The distance of falling down of which the cost is to be computed.
	 * @return The cost of falling down a given distance rounded down to the next integer.
	 * 	     | result == ( (int) Math.round(3*distance) ) 
	 */
	public int costFall(double distance) {
		double cost = 3*distance;
		if (cost > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		if (cost < Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		return (int) cost;
	}

	/**
	 * Returns whether or not the worm can jump.
	 * 
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than the minimum number 
	 * 		   of action points, the direction does not exceed Pi and is not less than zero.
	 *       |  result == (getActionPoints() >= minActionPoints &&  getDirection() >= 0 
	 *		 |		       && Util.fuzzyLessThanOrEqualTo(getDirection(), Math.PI)      
	 */
	public boolean canJump(double timeStep) {
		return ( (getActionPoints() > minPoints) 
				&& !(Util.fuzzyEquals(jumpTime(timeStep), 0))
				&& !(getWorld().isImpassable(getPosition().getX(), getPosition().getY(), this.getRadius())) );
	}
	
	/**
	 * Changes the position of the worm as the result of a jump 
	 * from the current position (x,y) and with respect to the 
	 * worm�s direction and the number of remaining action points.
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
	public void jump(double timeStep)
	   throws IllegalJumpException {
		if (! canJump(timeStep))
			throw new IllegalJumpException(); 
		Position positionAfterJump = jumpStep(jumpTime(timeStep));
		setPosition(positionAfterJump);
		
		setActionPoints(minPoints);
		eat();
		if (! getWorld().isAdjacent(getPosition().getX(), getPosition().getY(), this.getRadius()))
			terminate();	
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
	public double jumpTime(double timeStep) {
		double jumpTime = timeStep;
		Position tempPos = jumpStep(jumpTime);
		while ( !stopJump(tempPos) ) {
			jumpTime += timeStep;
			tempPos = jumpStep(jumpTime);
		}
		if (getWorld().isImpassable(tempPos.getX(),tempPos.getY(),getRadius()) && getWorld().isInWorld(tempPos.getX(),tempPos.getY(),getRadius()))
			return 0;
		return jumpTime;
	}

	public boolean stopJump(Position position) {
		return getWorld().isImpassable(position.getX(),position.getY(),getRadius())
		|| (getPosition().getDistanceTo(position) > getRadius() && getWorld().isAdjacent(position.getX(), position.getY(), getRadius()));
	}
	
	/**
	 * Returns in-flight positions of a jumping worm at any dt seconds after launch.
	 * 
	 * @param  dt
	 * 
	 * �
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
	public Position jumpStep(double dt) {
		double xdt = getPosition().getX() + (initialVelocityX()*dt);
		double ydt = getPosition().getY() + (initialVelocityY()*dt) - 0.5*G*Math.pow(dt, 2);
		return new Position(xdt,ydt);
	}
	
	/**
	 * Returns the force exerted on the worm.
	 * 
	 * @return The force equals the sum of five times 
	 * 		   the action points of this worm and the product 
	 *         of the mass of the worm and G.
	 *       | return == (5*getActionPoints() + getMass()*G)
	 */
	public double getForce() {
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
		return getForce() * 0.5 / getMass();
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
	 * Check whether this worm has the given weapon as one
	 * of the weapons attached to it.
	 * 
	 * @param weapon
	 * 		  The weapon to check.
	 */
	@Basic @Raw
	public boolean hasAsWeapon(Weapon weapon) {
		return this.weapons.contains(weapon);
	}
	
	/**
	 * Check whether this worm can have the given weapon as one of its weapons.
	 * 
	 * @param  weapon
	 *         The weapon to check.
	 * @return False if the given weapon is not effective.
	 *       | if (weapon == null) then result == false
	 *         Otherwise, true if and only if this worm is not yet terminated or if the given weapon is terminated.
	 *       | else result = ( (this.isAlive() || weapon.isTerminated() )     
	 */
	@Raw
	public boolean canHaveAsWeapon(Weapon weapon) {
		return ( (weapon != null) &&  (this.isAlive() || weapon.isTerminated()) );
	} 
	
	/**
	 * Check whether this worm has proper weapons associated with it.
	 * @return True if and only if this worm can have each of its weapons as a weapon attached to it, 
	 * 		   and if each of these weapons references this worm as their worm.
	 *       | result == for each weapon in Weapon : 
	 *       |             ( if (this.hasAsWeapon(weapon)) then 
	 *                           canHaveAsWeapon(weapon) && (weapon.getWorm() == this) )
	 */
	@Raw
	public boolean hasProperWeapons() {
		for (Weapon weapon: this.weapons) {
			if (! canHaveAsWeapon(weapon))
				return false;
			if (weapon.getWorm() != this)
				return false;
		}
		return true;
	}
	
	/**
	 * Return a list of all weapons associated with this worm.
	 * 
	 * @return Each weapon in the resulting is is attached to this worm and vice versa.
	 *       | for each weapon in Weapon:
	 *       |    (result.contains(weapon) == this.hasAsWeapon(weapon))
	 */
	public List<Weapon> getAllWeapons() {
		return new ArrayList<Weapon>(this.weapons);
	}
	
	/**
	 * Add the given weapon as a weapon for this worm.
	 * @param  weapon
	 *         The weapon to become a weapon of this worm.
	 * @post   This worm has the given weapon as one of its weapons.
	 *       | new.hasAsWeapon(weapon)
	 * @post   The given weapon references this worm as the worm to which it is attached.
	 *       | (new weapon).getWorm() == this
	 * @throws IllegalArgumentException
	 *         This worm cannot have the given weapon as one of its weapons.
	 *       | ! canHaveAsWeapon(weapon)
	 * @throws IllegalArgumentException
	 * 		   The given weapon is already attached to some worm.
	 *       | ( (weapon != null) && (weapon.getWorm() != null) )      
	 */
	public void addAsWeapon(Weapon weapon) 
	  throws IllegalArgumentException {
		if (!canHaveAsWeapon(weapon))
			throw new IllegalArgumentException();
		if (weapon.getWorm() != null)
			throw new IllegalArgumentException();
		this.weapons.add(weapon);
		weapon.setWorm(this);		
	}
	
	/**
	 * Remove the given weapon as a weapon associated with this worm.
	 * @param  weapon
	 *         The weapon to be removed.
	 * @post   This worm does not have the given weapon as one of its weapons.
	 *       | ! new.hasAsWeapon(weapon) 
	 * @post   If this worm has the given weapon as one of its weapons, the given weapon 
	 *         is no longer attached to any worm.
	 *       | if (hasAsWeapon(weapon))
	 *       |    ((new weapon).getWorm() == null)
	 */
	public void removeAsWeapon(Weapon weapon) {
		if (hasAsWeapon(weapon)) {
			this.weapons.remove(weapon);
			weapon.setWorm(null);
		}
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	public void setWeapons() {
		Weapon bazooka = new Bazooka(this);
		Weapon rifle = new Rifle(this);
		this.selectedWeapon = bazooka;
	}
	
	/**
	 * Returns the name of the weapon that is currently active for the given worm,
	 * or null if no weapon is active.
	 */
	public Weapon selectedWeapon() {
		return this.selectedWeapon;
	}
	
	/**
	 * Activates the next weapon for the given worm
	 */
	public void selectNextWeapon() 
	 throws IllegalStateException {
		if (this.weapons.size() == 0) {
			throw new IllegalStateException("This worm has no weapons!");
		}
		int index = this.weapons.indexOf(this.selectedWeapon) + 1;
		if (index == this.weapons.size()) 
			index = 0;
		this.selectedWeapon = this.weapons.get(index);
	}

	private Weapon selectedWeapon;
	
	/**
	 * Makes the given worm shoot its active weapon with the given propulsion yield.
	 */
	public void shoot(int yield) 
	  throws IllegalShootException {
		selectedWeapon().shoot(yield);
		decreaseActionPoints(selectedWeapon().costOfShooting());
	}
	
	/**
	 * Returns the name of the weapon that is currently active for the given worm,
	 * or null if no weapon is active.
	 */
	public String getSelectedWeapon() {
		return selectedWeapon().getName();		
	}	
	
	/**
	 * List collecting references to the weapons of this worm.
	 * 
	 * @invar The list of weapons is effective.
	 *      | weapons != null
	 * @invar Each element in the list of weapons is either not effective or it 
	 *        references a weapon that is acceptable as a weapon for this worm.
	 *      | for each weapon in weapons:
	 *           ( (weapon == null) || canHaveAsWeapon(weapon) )
	 */
	private final List<Weapon> weapons = new ArrayList<Weapon>();
	
	/**
	 * 
	 * @param 
	 */
	@Override
	public void terminate() {
		if (getWorld().getCurrentWorm() == this)
			getWorld().startNextTurn();
		for (Weapon weapon: weapons) 
			if (! weapon.isTerminated()) {
				weapon.setWorm(null);
				this.weapons.remove(weapon);
		}
		super.terminate();
	}
	
	/**
	 * 
	 * @param 
	 */
	@Override
	public void setToActive(boolean isActive) {
		if (isActive) {
			setActionPoints(getMaxPoints());
			setHitPoints(getHitPoints()+10);
		}
		super.setToActive(isActive);
	}

	/**	
	 * 
	 * @param ration
	 */
	public void eat(GameObject object) {
		if (canEat(object)) {
			setRadius(getEffectOfEating(object));
			object.terminate();
		}
	}
	
	public void eat() {
		List<GameObject> overlappingObjects = getWorld().getOverlappingObjectsOfType(GameObject.class, getPosition(), getRadius());
		for (GameObject object : overlappingObjects)
			eat(object);
	}
	
	public double getEffectOfEating(GameObject object) {
		Food ration = (Food) object;
		return (getRadius() * (1 + ration.getGrowthEffect()));
	}
	
	public boolean canEat(GameObject object) {
		return ( Food.class.isInstance(object) );
	}

}