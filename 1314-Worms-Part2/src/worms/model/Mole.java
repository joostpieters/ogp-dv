package worms.model;
import java.util.List;

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
public class Mole extends MoveableGameObject {

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
	public Mole(World world, Position position, double direction, double radius,
		String name) 
	   throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction);
		setName(name);
		setActionPoints(getMaxPoints());
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
		if (radius > getMaximalRadius()) {
			radius = getMaximalRadius();
			terminate();
		}
		super.setRadius(radius);
	}
	
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
	private final double p = 1564.5;
	
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
		}
	}
	
	public Position getAdjacentorPassablePosition() 
	  throws IllegalPositionException {
		double distance = getRadius()/3;
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
		return 3;
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
		double step = getWorld().getPixelHeight();
		double distance = step;
		Position tempPos = getPosition().addToY(-distance);
		//int cntr = 0;
		while ( (!getWorld().isImpassable(tempPos.getX(), tempPos.getY(), getRadius())) 
			    && (! getWorld().isAdjacent(tempPos.getX(), tempPos.getY(), getRadius()))) {
			distance += step;
			tempPos = getPosition().addToY(-distance);
			//cntr += 1;
		}
		setPosition(tempPos); 
		eat();
		//decreaseHitPoints(costFall(step*cntr));
		if (! getWorld().isInWorld(tempPos.getX(), tempPos.getY(), getRadius()) ) 
			terminate();
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
	 * 
	 * @param 
	 */
	@Override
	public void terminate() {
	//	if (getWorld().getCurrentPlayer() == this)
			getWorld().startNextTurn();
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
		}
		super.setToActive(isActive);
	}

	@Override
	public double getForce() {
		return 0;
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
		return ( getRadius() * (1 + (object.getRadius()/getRadius())) );
	}
	
	public boolean canEat(GameObject object) {
		return ( Worm.class.isInstance(object) && Util.fuzzyLessThanOrEqualTo(object.getRadius(), getRadius()));
	}
	
}
