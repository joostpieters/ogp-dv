package worms.model;
import java.util.Arrays;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;
import worms.util.Util;

/**
 * A class of game characters involving a name, position, direction, radius, mass and action points.
 * @author Delphine
 *
 */
public abstract class Character extends MoveableGameObject {

	/**
	 * 
	 * @param world
	 * @param position
	 * @param radius
	 * @param direction
	 * @param name
	 * @throws IllegalDirectionException
	 * @throws IllegalPositionException
	 * @throws IllegalRadiusException
	 * @throws IllegalNameException
	 */
	protected Character(World world, Position position, double radius, double direction, String name, Program program)
	  throws  IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction);
		setName(name);
		setActionPoints(getMaxPoints());
		if (program != null) {
			//this.program = program;
			program.setAgent(this);
			setProgram(program);
		}
	}
    
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
	public abstract boolean isValidName(String name);
	
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
	
	public static List<String> characterNames = Arrays.asList("Shari", "Shannon", 
			"Willard", "Jodi", "Santos", "Ross", "Cora", "Jacob", "Homer",
			"Kara", "Delphine", "Wormpje");
	
	/**
	 * Variable registering the name of the worm.
	 */
	private String name;
	
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
	protected static final int MINPOINTS = 0;
	
	/**
	 * Check whether the given mass is a valid mass for a worm.
	 * 
	 * @param  mass
	 *         The mass to check.
	 * @return True if and only if the given mass is not below 
	 * 		   the Integer.MIN_VALUE and not above Integer.MAX_VALUE.
	 *       | result == (mass >= Integer.MIN_VALUE) 
	 *                   && (mass <= Integer.MAX_VALUE)
	 */
	public static boolean isValidMass(double mass) {
		return (Util.fuzzyGreaterThanOrEqualTo(mass, Integer.MIN_VALUE)) 
			   && (Util.fuzzyLessThanOrEqualTo(mass, Integer.MAX_VALUE));
	}
	
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
        return points >= MINPOINTS && 
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
		else if (actionPoints <= MINPOINTS) {
			this.actionPoints = MINPOINTS;
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
		   && (getActionPoints() >= MINPOINTS + actionPoints ) )
			setActionPoints(getActionPoints() - actionPoints);
		else if ( ( (actionPoints > 0) 
		   && (getActionPoints() <= MINPOINTS + actionPoints ) ))
			setActionPoints(MINPOINTS);
	}
	
	/**
	 * Variable registering the current number of action points of the worm.
	 */
	private int actionPoints;
	
	@Override
	public abstract double getDensity();
	
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
	@Override
	public boolean canTurn(double angle) {
		return super.canTurn(angle) && (getActionPoints() >= costTurn(angle));
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
	 * @param ration
	 */
	public void eat(GameObject object) {
		if (canEat(object)) {
			object.terminate();
			setRadius(getEffectOfEating(object));	
		}
	}
	
	public abstract boolean canEat(GameObject object);

	public abstract double getEffectOfEating(GameObject object);

	/**
	 * 
	 */
	public void eat() {
		List<GameObject> overlappingObjects = getWorld().getOverlappingObjectsOfType(GameObject.class, getPosition(), getRadius());
		for (GameObject object : overlappingObjects)
			eat(object);
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
	
	/**
	 * Returns whether or not the worm can move a given number of steps.
	 * 
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than or equal to the
	 *         cost of moving the worm a given number of steps.
	 *       |  result == ( getActionPoints() >= costMove(nbSteps) )
	 */
	public boolean canMove() {
		Position nextPosition = getMovePosition();
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
			Position newPos = getMovePosition();
			decreaseActionPoints(costMove(newPos));
			setPosition(newPos);
			eat();
		}
	}
	
	public abstract Position getMovePosition();
	
	public abstract int costMove(Position nextPosition);
	
	/**
	 * Returns whether or not the given worm can fall down
	 */
	public boolean canFall() {
		return canFall(getPosition());
	}
	
	public boolean canFall(Position position) {
		return (!getWorld().isAdjacent(position, getRadius()) && !getWorld().isImpassable(position, getRadius()))
			   || !getWorld().isInWorld(position, getRadius()) ;
	}
	
	/**
	 * Makes the given worm fall down until it rests on impassable terrain again.
	 */
	public void fall() {
		if (!canFall()) 
			throw new IllegalStateException("Cannot fall!");
		double step = getRadius()/10;
		Position oldPos = new Position(getPosition());
		Position tempPos = oldPos;
		while ( canFall(tempPos) ) {
			if ( !getWorld().isInWorld(tempPos, getRadius()) ) {
				setPosition(tempPos); 
				terminate();
				return;
			}
			tempPos = tempPos.addToY(-step);
		}
		setPosition(tempPos); 
		eat();
		takeFallDamage(oldPos.getDistanceTo(tempPos));
	}
	
	public abstract void takeFallDamage(double distance);
	
	/**
	 * 
	 * @param 
	 */
	@Override
	public void terminate() {
		if (getWorld().getCurrentPlayer() == this)
			getWorld().startNextTurn();
		super.terminate();
	}
	
	/**
	 * Return the program attached to this worm. A null reference is returned if no program is attached.
	 */
	@Basic @Raw
	public Program getProgram() {
		return this.program;
	}

	/**
	 * Checks whether this worm has a program.
	 * @return True if and only if this worm references an effective program.
	 *       | result == (getProgram() != null)
	 */
	public boolean hasProgram() {
		return getProgram() != null;
	}

	/**
	 * Check whether this worm has a proper program attached to it.
	 * @return True if and only if this worm does not reference an effective program, or if 
	 *         the program referenced by this worm in turn references this worm  as its agent.
	 *       | result == ( (getProgram() == null) || (getProgram().getAgent() == this) )
	 */
	@Raw
	public boolean hasProperProgram() {
		return (getProgram() == null) || (getProgram().getAgent() == this);
	}
	
	/**
	 * Set the program attached to this worm to the given program.
	 * @param program
	 *        The program to be attached to this worm.
	 * @pre   If the given program is effective, it must already reference this worm as its agent.
	 *      | if (program != null) then program.getAgent() == this
	 * @pre   If the given program is not effective and this worm has a program attached to it, 
	 *        that program may not reference this worm as its agent.
	 *      | if ( (program == null) && this.hasProgram() ) then (getProgram().getAgent() != this)
	 * @post  This worm references the given program as the program attached to it.
	 *      | new.getProgram() == program
	 */
	@Raw
	private void setProgram(@Raw Program program) {
		assert (program == null) || (program.getAgent() == this);
		assert ( (program != null) || (! hasProgram()) || (getProgram().getAgent() == null) );
		this.program = program;
	}
	
	/**
	 * Variable referencing the program attached to this worm.
	 */
	private Program program;
	
}
