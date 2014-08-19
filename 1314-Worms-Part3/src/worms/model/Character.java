package worms.model;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;

/**
 * A class of game characters involving a name, action points and a program.
 * Characters can eat, move and fall.
 * 
 * @invar  The name of each character must be a valid name for a character.
 *         | isValidName(getName())     
 * @invar  The current number of action points of each character must be a valid number of
 *         action points for a character.
 *         | canHaveAsPoints(getActionPoints())
 * @invar  Each program must have a proper program attached to it.
 *         | hasProperProgram(getProgram())
 * 
 * @author Delphine Vandamme
 *
 */
public abstract class Character extends MobileGameObject {

	/**
	 * Initialize this new character with given name, position, direction and radius.
	 *
	 * @param  world
	 * 		   The world in which to place the created character  
	 * @param  position 
	 * 		   The position of this new character (in meter)
	 * @param  direction
	 * 		   The direction of the new character (in radians)
	 * @param  radius 
	 * 		   The radius of the new character (in meter)
	 * @param  name
	 * 		   The name of the new character
	 * @param  program
	 *         The program of the new character.
	 * @pre	   The given direction must be a valid direction for a character.
     *       | isValidDirection(direction) 
	 * @effect The position of this new character is equal to the given position.
     *       | setPosition(position)
     * @effect The radius of this new character is equal to the given radius.
     * 		 | setRadius(radius) 
     * @effect The direction of this new character is equal to the given direction.
     *       | setDirection(direction)
     * @effect The name of this new character is equal to the given name.
     *       | setName(name)
     * @effect The nr of AP's of this new character is equal to the maximum nr of AP's.
     *       | setActionPoints(getMaxPoints())
     * @effect If the given program is effective, this character is the executing 
     *         agent of the program and the program of this character is equal to the given program.
     * 		 |	if (program != null)
     *       |     then program.setAgent(this) && setProgram(program)
     * @effect If the given world is effective, this character is added as a game object of the given world.
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
	protected Character(World world, Position position, double radius, double direction, String name, Program program)
	  throws  IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction);
		
		setName(name);
		setActionPoints(getMaxPoints());
		if (program != null) {
			program.setAgent(this);
			setProgram(program);
		}
	}
    
	/**
	 * Returns the name the character.
	 */
	@Basic @Raw
	public String getName() {
		return this.name;
	}
	
	/**
	 * Check whether the given name is a valid name for a character.
	 * 
	 * @param  name
	 *         The name to check.
	 * @return True if and only if the given name is a valid name for the character.
	 */
	public abstract boolean isValidName(String name);
	
	/**
	 * Sets the name of the character to the given string.
	 * 
	 * @param  name
	 *		   The new name for this character.
	 * @post   The name of this new character is equal to the given name.
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
	 * Variable registering the name of the character.
	 */
	private String name;
	
	/**
	 * Returns The current number of action points of the character.
	 * 
	 * @return The current number of action points of the character.
	 *  	 | result == this.actionPoints
	 * @effect If the nr of action points is above the maximum nr of action points, the nr of action points is set 
	 *         to the maximum nr of action points.
	 *       | setMaxActionPoints(getMaxPoints())
	 */
	public int getActionPoints() {
		if ( actionPoints > getMaxPoints() )
			setActionPoints(getMaxPoints());
		return this.actionPoints;
	}
	
	/**
	 * Variable registering the lowest possible value of the action and hit points of all characters.
	 */
	protected static final int MINPOINTS = 0;

	
	/**
	 * Return the highest possible value for the action points of this character.
	 * 
	 * @return The highest possible value for the action points
	 *         of this character is equal to the mass of this character rounded to the nearest integer.
	 *       | result == (int) Math.round(getMass())
	 * @throws IllegalMassException
	 *       | ! isValidMass(getMass())
	 */ 
	public int getMaxPoints() throws IllegalMassException {
		if (! isValidMass(getMass()))
			throw new IllegalMassException();			
		return (int) Math.round(getMass());
	}
	
	/**
	 * Returns the current number of action points of the character.
	 * 
	 * @param  actionPoints
	 *		   The new number of action points for this character.
	 * @post    If the given number of AP's are in the range established by the minimum
	 *          and maximum number of AP's for this character, the number of AP's
	 *          of this character are equal to the given number of AP's.
	 *        | if ( (actionPoints >= this.MINPOINTS) && (actionPoints <= getMaxPoints()) )
	 *        |   then new.getActionPoints() == actionPoints
	 * @post   If the given number of AP's exceeds the maximum number of AP's for this character, 
	 *         the number of AP's of this character are equal to the the highest possible value 
	 *         for the number of AP's of this character.
	 *       | if (actionPoints> getMaxPoints())
	 *       |   then new.getActionPoints() == getMaxPoints() 
	 * @post   If the given number of AP's are below the minimum number of AP's for this character, 
	 *         the number of AP's of this character are equal to the lowest possible value 
	 *         for the number of AP's of this character.
	 *        | if (actionPoints < this.MINPOINTS)
	 *        |     then new.getActionPoints() = this.MINPOINTS     
	 * @post   If the given number of AP's are below the minimum number of AP's for this character, 
	 *         the next turn in the world is started.
	 * 		  | if (actionPoints < this.MINPOINTS)
	 *        |     then getWorld().startNextTurn();    
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
	 * Decreases the current number of action points of the character with the given number of points.
	 * 
	 * @param  actionPoints
	 * 		   The number of action points to be subtracted from the 
	 * 		   current number of action points.
	 * @effect If the given nr of action points is positive and if 
	 * 		   the old nr of action points of this character is not below 
	 * 		   the minimum nr of action points incremented with the given 
	 * 		   nr of action points, the new nr of action points is equal to
	 * 		   the old nr of action points decremented with the given nr of action points.
	 *       | setActionPoints( getActionPoints() - actionPoints )
	 * @effect If the given nr of action points is positive and if 
	 * 		   the old nr of action points of this character is below 
	 * 		   the minimum nr of action points incremented with the given 
	 * 		   nr of action points, the new nr of action points is equal to
	 * 		   the minimum action points.
	 *       | setActionPoints( this.MINPOINTS )
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
	 * Variable registering the current number of action points of the character.
	 */
	private int actionPoints;
	
	/**
	 * Returns the density of this character.
	 */
	@Override @Basic
	public abstract double getDensity();
	
	/**
	 * Returns whether or not the character can turn by the given angle.
	 * 
	 * @param  angle
	 * 		   The angle to check.
	 * @return True if and only if the angle is in the valid range and if the number action points 
	 *         of this character is greater than or equal to the cost of turning the character by a given angle.
	 *       | result == (super.canTurn(angle)) && ( getActionPoints() >= costTurn(angle) )
	 */
	@Override
	public boolean canTurn(double angle) {
		return super.canTurn(angle) && (getActionPoints() >= costTurn(angle));
	}
	
	/**
	 * Turns the character by the given angle.
	 * 
	 * @param  angle
	 *  	   The angle by which the character has to turn.
	 * @pre    The direction must be a valid direction for a character.
     *       | isValidDirection(getDirection() + angle)
     * @effect Turns the character by the given angle.
     *       | super.turn(angle)
	 * @effect If the given angle is a valid angle, the number of action points of the 
	 * 		   character is decreased by the cost of turning the character by the given angle.
	 * 		 | decreaseActionPoints(costTurn(angle))
	 */
	@Override
	public void turn(double angle) {
		super.turn(angle);
		decreaseActionPoints(costTurn(angle));
	}
	
	/**
	 * Returns the cost in action points of turning the character by a given angle.
	 * 
	 * @param  angle
	 		   The angle of which the cost of turning is to be computed.
	 * @return The cost of turning the character by the given angle rounded up to the next integer.	   
	 * 		 | result == ( (int) Math.ceil(Math.abs(60/(2*Math.PI/angle))) )
	 */
	public int costTurn(double angle) {
		return (int) Math.ceil(Math.abs(60/(2*Math.PI/angle)));
	}
	
	/**
	 * Returns whether or not the character can move a given number of steps.
	 * 
	 * @return True if and only if the next position is effective and if the number action points of 
	 *         this character is greater than or equal to the cost of moving the character a given number of steps.
	 *       |  result == (getMovePosition() != null) && ( getActionPoints() >= costMove(nbSteps) ) 
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
	 * Moves the character to a certain nearby position.
	 *
	 * @effect The number of action points of the character is decreased
	 * 		   by the cost of moving the character a number of steps.
	 * 		 | if(canMove()) then decreaseActionPoints(costMove(nbSteps))
	 * @effect The position of this character is equal to the new position.
	 *       | if(canMove()) then setPosition(getMovePosition())
	 * @effect If possible the character eats all overlapping game objects on the new position.
	 *       | if(canMove()) then eat()
	 */
	public void move() {
		if (canMove()) {
			Position newPos = getMovePosition();
			decreaseActionPoints(costMove(newPos));
			setPosition(newPos);
			eat();
		}
	}
	
	/**
	 * Returns the position to which the character can move.
	 */
	public abstract Position getMovePosition();
	
	/**
	 * Returns the cost in action points of moving to the given position.
	 * 
	 * @param nextPosition
	 *        The new position to which the character wants to move. 
	 */
	public abstract int costMove(Position nextPosition);
	
	/**
	 * Returns whether or not a character can fall down from its current position.
	 * 
	 * @return | result == canFall(getPosition())
	 */
	public boolean canFall() {
		return canFall(getPosition());
	}
	
	/**
	 * Returns whether or not a character can fall down from the given position
	 * 
	 * @param  position
	 *         The position to check.
	 * @return result == ( !getWorld().isAdjacent(position, getRadius()) && !getWorld().isImpassable(position, getRadius()))
			               || !getWorld().isInWorld(position, getRadius() )
	 */ 
	public boolean canFall(Position position) {
		return (!getWorld().isAdjacent(position, getRadius()) && !getWorld().isImpassable(position, getRadius()))
			   || !getWorld().isInWorld(position, getRadius()) ;
	}
	
	/**
	 * Makes the given character fall down until it rests on impassable terrain again.
	 * 
	 * @effect | setPosition(tempPos)
	 * @effect | eat()
	 * @effect | takeFallDamage(oldPos.getDistanceTo(tempPos))
	 * @throws IllegalStateException
	 *         ! canFall()
	 */
	public void fall() throws IllegalStateException {
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
	
	/**
	 * Sets the effect of falling a given distance.
	 * 
	 * @param distance
	 *        The distance along which the character has fallen.
	 */
	public abstract void takeFallDamage(double distance);

	/**
	 * Eat all overlapping objects.
	 * 
	 * @effect | List<GameObject> overlappingObjects = getWorld().getOverlappingObjectsOfType(GameObject.class,getPosition(),getRadius())
	 *         | for each object in overlappingObjects:
	 *         |     eat(object)
	 */
	public void eat() {
		List<GameObject> overlappingObjects = getWorld().getOverlappingObjectsOfType(GameObject.class, getPosition(), getRadius());
		for (GameObject object : overlappingObjects)
			eat(object);
	}

	/**
	 * Returns whether or not the given game object can be eaten.
	 * 
	 * @param  object
	 *         The game object to check.
	 * @return True if and only if the game object can be eaten.
	 */
	public abstract boolean canEat(GameObject object);

	
	/**	
	 * Eat the given game object.
	 * 
	 * @param  object
	 *         The game object to be eaten
	 * @effect | if (canEat(object)) then object.terminate()
	 * @effect | if (canEat(object)) then setRadius(getEffectOfEating(object))
	 */
	public void eat(GameObject object) {
		if (canEat(object)) {
			object.terminate();
			setRadius(getEffectOfEating(object));	
		}
	}
	
	/**
	 * Returns the effect of eating the given object on the current nr of action points.
	 * 
	 * @param  object
	 *         The game object to be eaten.
	 */
	public abstract double getEffectOfEating(GameObject object);

	/**
	 * Return the program attached to this character. A null reference is returned if no program is attached.
	 */
	@Basic @Raw
	public Program getProgram() {
		return this.program;
	}

	/**
	 * Checks whether this character has a program.
	 * 
	 * @return True if and only if this character references an effective program.
	 *       | result == (getProgram() != null)
	 */
	public boolean hasProgram() {
		return getProgram() != null;
	}

	/**
	 * Check whether this character has a proper program attached to it.
	 * 
	 * @return True if and only if this character does not reference an effective program, or if 
	 *         the program referenced by this character in turn references this character  as its agent.
	 *       | result == ( (getProgram() == null) || (getProgram().getAgent() == this) )
	 */
	@Raw
	public boolean hasProperProgram() {
		return (getProgram() == null) || (getProgram().getAgent() == this);
	}
	
	/**
	 * Set the program attached to this character to the given program.
	 * 
	 * @param program
	 *        The program to be attached to this character.
	 * @pre   If the given program is effective, it must already reference this character as its agent.
	 *      | if (program != null) then program.getAgent() == this
	 * @pre   If the given program is not effective and this character has a program attached to it, 
	 *        that program may not reference this character as its agent.
	 *      | if ( (program == null) && this.hasProgram() ) then (getProgram().getAgent() != this)
	 * @post  This character references the given program as the program attached to it.
	 *      | new.getProgram() == program
	 */
	@Raw
	private void setProgram(@Raw Program program) {
		assert (program == null) || (program.getAgent() == this);
		assert ( (program != null) || (! hasProgram()) || (getProgram().getAgent() == null) );
		this.program = program;
	}
	
	/**
	 * Variable referencing the program attached to this character.
	 */
	private Program program;
	
	/**
	 * Sets the activity status of this character.
	 * 
	 * @param  isActive
	 *         The new activity status.
	 * @effect | if (isActive) then setActionPoints(getMaxPoints())
	 * @effect | super.setToActive(isActive)
	 */
	@Override
	public void setToActive(boolean isActive) {
		if (isActive) {
			setActionPoints(getMaxPoints());
		}
		super.setToActive(isActive);
	}
	
	/**
	 * Kill this character.
	 * 
	 * @effect If this character is the current player then the next turn in the world is started.
	 * 		 | if (getWorld().getCurrentPlayer() == this) then getWorld().startNextTurn()
	 * @effect Terminate this character.
	 *       | super.terminate()
	 */
	@Override
	public void terminate() {
		super.terminate();
		if (getWorld().getCurrentPlayer() == this)
			getWorld().startNextTurn();
	}
}
