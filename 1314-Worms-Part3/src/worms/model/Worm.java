package worms.model;
import java.util.*;

import worms.model.abilities.*;
import worms.model.weapons.*;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;

/**
 * A class of worms which have hit points, weapons and the ability to jump and shoot.
 * 
 *   
 * @invar  The current number of hit points of each worm must be a valid number of
 *         hit points for a worm.
 *         | canHaveAsPoints(getHitPoints())
 *                  
 * @author   Delphine Vandamme 
 */
public class Worm extends Character implements JumpAbility, ShootAbility {

	/**
	 * Initialize this new worm with given name, location, direction and radius.
	 *
	 * @param  world
	 * 		   The world in which to place the created worm  
	 * @param  position 
	 * 		   The position of this new worm (in meter)
	 * @param  direction
	 * 		   The direction of the new worm (in radians)
	 * @param  radius 
	 * 		   The radius of the new worm (in meter)
	 * @param  name
	 * 		   The name of the new worm
	 * @param  program
	 *         The program of the new worm.
	 * @pre	   The given direction must be a valid direction for a worm.
     *       | isValidDirection(direction) 
	 * @effect The position of this new worm is equal to the given position.
     *       | setPosition(position)
     * @effect The radius of this new worm is equal to the given radius.
     * 		 | setRadius(radius) 
     * @effect The direction of this new worm is equal to the given direction.
     *       | setDirection(direction)
     * @effect The name of this new worm is equal to the given name.
     *       | setName(name)
     * @effect The nr of AP's of this new worm is equal to the maximum nr of AP's.
     *       | setActionPoints(getMaxPoints())
     * @effect The nr of HP's of this new worm is equal to the maximum nr of HP's.
     *       | setHitPoints(getMaxPoints())
     * @effect A bazooka and rifle are added as weapons for this worm.
     *       | setWeapons()
     * @effect If the given program is effective, this worm is the executing 
     *         agent of the program and the program of this worm is equal to the given program.
     * 		 |	if (program != null) then program.setAgent(this) && setProgram(program)
     * @effect If the given world is effective, this worm is added as a game object of the given world.
     * 	     | if (world != null) then world.addAsGameObject(this)
     * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for a worm.
	 *       | ! isValidRadius(radius)         
	 * @throws IllegalNameException
	 * 		   The given name is not a valid name for a worm.
	 *       | ! isValidName(name)  
	 * @throws IllegalPositionException
	 * 		   The given position is not a valid position for a worm.
	 *       | ! position.canHaveAsPosition(position)                
	 */
	@Raw 
	public Worm( World world, Position position, double direction, double radius, String name, Program program ) 
	   throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalNameException {
		super(world, position, radius, direction, name, program);
		
		setHitPoints(getMaxPoints());
		setWeapons();
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
	@Override
	public boolean isValidName(String name) {
		return (name.length() >= 2) && (name.matches("[A-Z][a-zA-Z0-9\'\" ]*"));
	}
	
	/**
	 * A list with names for randomly added worms.
	 */
	public static List<String> WORMNAMES = Arrays.asList("Shari", "Shannon", 
			"Willard", "Jodi", "Santos", "Ross", "Cora", "Jacob", "Homer",
			"Kara", "Delphine", "wormpje");
	
	/**
	 * Returns the minimal radius of this worm.
	 */
	@Override @Basic
	public double getMinimalRadius() {
		return this.minimalRadius;
	}
	
	/**
	 * Variable registering the minimal radius of the worm.
	 */
	public final double minimalRadius = 0.25;
	
	/**
	 * Returns the density of worms.
	 */
	@Override @Basic
	public double getDensity() {
		return Worm.DENSITY;
	}
	
	/**
	 * Variable registering the density of a worm that applies to all worms.
	 */
	private static final double DENSITY = 1062;

	/**
	 * Returns the position to which the worm can move.
	 * 
	 * @return If an effective adjacent or passable position is found, it is returned. 
	 *       | Position adjacentOrPassablePos = getWorld().getAdjacentorPassablePositionTo(this, getRadius());
	 *		 | if ( adjacentOrPassablePos != null ) then result == adjacentOrPassablePos;
	 * @return Else if a nearby position outside of the world is found, this is returned.
	 *       | Position impassablePos = getPosition().addToX(getRadius()*Math.cos(getDirection())).addToY(getRadius()*Math.sin(getDirection()))
	 *       | if ( adjacentOrPassablePos == null ) && ( !getWorld().isInWorld(impassablePos, getRadius() )
	 *       |    then result == impassablePos
	 * @return Otherwise, a null position is returned.
	 *       | if ( adjacentOrPassablePos == null ) && ( getWorld().isInWorld(impassablePos, getRadius() )  
	 *       |    then result == null
	 *      
	 */
	@Override
	public Position getMovePosition() {
		Position adjacentOrPassablePos = getWorld().getAdjacentorPassablePositionTo(this, getRadius());
		if ( adjacentOrPassablePos != null ) 
			return adjacentOrPassablePos;
		
		else {
			Position currentPos = new Position(getPosition());
			Position impassablePos = currentPos.addToX(getRadius()*Math.cos(getDirection())).addToY(getRadius()*Math.sin(getDirection()));
				if ( !getWorld().isInWorld( impassablePos, getRadius()) )
					return impassablePos;	
		}
		return null;
	}
	
	/**
	 * Returns the cost in action points of moving the worm to the given position.
	 *  
	 * @param  position
	 * 		   The new position to which the worm wants to move.
	 * @return The cost of moving the worm to the given position rounded up to the next integer 
	 *         depending on the slope (angle) between the current and given position.
	 *       | double slope = getPosition().getSlope(nextPosition)
	 * 	     | result == ( (int)  Math.ceil((Math.abs(Math.cos(slope)) + 4 * Math.abs(Math.sin(slope)))) ) 
	 */
	@Override
	public int costMove(Position nextPosition) {
		double slope = getPosition().getSlope(nextPosition);
		double cost = Math.ceil((Math.abs(Math.cos(slope)) + 4 * Math.abs(Math.sin(slope))));
		return (int) cost;
	}
	
	/**
	 * Sets the cost in hit points of falling down a given distance.
	 *  
	 * @param  distance
	 * 		   The distance along which the character has fallen.
	 * @effect If the cost exceeds the maximum value of an Integer, the current nr of action points is decreased by the max value.
	 *       | if (cost > Integer.MAX_VALUE) then decreaseHitPoints(Integer.MAX_VALUE)
	 * @effect If the cost is below the minimum value of an Integer,  the current nr of action points is decreased by the min value.
	 *       | if (cost < Integer.MIN_VALUE) then decreaseHitPoints(Integer.MIN_VALUE)
	 * @effect Otherwise, the cost of falling down a given distance rounded down to the next integer.
	 * 	     | result == decreaseHitPoints((int) 3*distance) 
	 */
	@Override
	public void takeFallDamage(double distance) {
		double cost = 3*distance;
		if (cost > Integer.MAX_VALUE)
			cost = Integer.MAX_VALUE;
		if (cost < Integer.MIN_VALUE)
			cost = Integer.MIN_VALUE;
		decreaseHitPoints((int) cost);	
	}
	
	/**
	 * Kill this worm.
	 * 
	 * @effect All weapons of this worm are removed from this worm.
	 *       | for each weapon in this.weapons:
	 *       |    removeAsWeapon(weapon))
	 * @effect The worm is terminated.
	 *       | super.terminate()
	 */
	@Override
	public void terminate() {
		for (Weapon weapon: this.weapons) 
			if (! weapon.isTerminated()) 
				removeAsWeapon(weapon);
		super.terminate();  
	}
	
	/**
	 * Sets the activity status of this character.
	 * 
	 * @param  isActive
	 *         The new activity status.
	 * @effect If the worm is activated, the nr of hit points are increased by 10.
	 *       | if (isActive) setHitPoints(getHitPoints() + 10)
	 * @effect The worm is (de)activated and if it is activated the nr of APs is set to the max nr of APs.
	 *       | super.setToActive(isActive)
	 */
	@Override
	public void setToActive(boolean isActive) {
		if (isActive) {
			setHitPoints(getHitPoints() + 10);
		}
		super.setToActive(isActive);
	}

	/**
	 * Returns the effect of eating the given object on the current nr of action points.
	 * 
	 * @param  object
	 *         The game object to be eaten. 
	 * @effect The effect of eating a food ration equals the radius multiplied by a given factor depending on the ration.
	 *       | result = getRadius() * (1 + ((Food) object).getGrowthEffect())
	 * @throws IllegalTypeException
	 *         The given object is not an instance of Food class.
	 *       | object.getClass() != Food.class 
	 */
	@Override
	public double getEffectOfEating(GameObject object) {
		if ( object.getClass() != Food.class )
			throw new IllegalTypeException();
		Food ration = (Food) object;
		return (getRadius() * (1 + ration.getGrowthEffect()));
	}
	
	/**
	 * Returns whether or not the given game object can be eaten.
	 * 
	 * @param  object
	 *         The game object to check.
	 * @return True if and only if the given game object is an instance of the class Food.
	 *       | result == Food.class.isInstance(object)
	 */
	@Override
	public boolean canEat(GameObject object) {
		return ( Food.class.isInstance(object) );
	}
	
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
		else if (hitPoints <= MINPOINTS) {
			this.hitPoints = MINPOINTS;
			terminate();
			if (getWorld().getCurrentPlayer() == null && getWorld().hasStarted() && !getWorld().isGameFinished())
				getWorld().startNextTurn();
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
		   && (getHitPoints() >= MINPOINTS + hitPoints ) )
			setHitPoints(getHitPoints() - hitPoints);
		else if ( ( (hitPoints > 0) 
		   && (getHitPoints() <= MINPOINTS + hitPoints ) ))
			setHitPoints(MINPOINTS);
	}
	
	/**
	 * Variable registering the current number of hit points of the worm.
	 */
	private int hitPoints;

	/**
	 * Returns whether or not the worm can jump.
	 * 
	 * @return True if and only if the number action points of this worm is greater than the minimum number of action points, 
	 *         and if the current position of this worm is passable and if the jump time with given time step exceeds zero.
	 *       |  result == ( (getActionPoints() > MINPOINTS) && !(Util.fuzzyEquals(jumpTime(timeStep), 0))
	 *			             && !(getWorld().isImpassable(getPosition(), this.getRadius())) )     
	 */
	public boolean canJump(double timeStep) {
		return ( (getActionPoints() > MINPOINTS) 
				&& !(Util.fuzzyEquals(jumpTime(timeStep), 0))
				&& !(getWorld().isImpassable(getPosition(), this.getRadius())) );
	}
	
	/**
	 * Changes the position of the worm as the result of a jump from the current position and with respect to the 
	 * worm’s direction and the number of remaining action points.
	 * 
	 * @effect If the worm can jump, the jump distance is added to the
	 * 		   current x-coordinate of the position of this worm.
	 *  	 | setPosition( jumpStep(jumpTime(timeStep)) )
	 * @effect The number of action points of the worm is set to the minimum value.
	 * 		 | setActionPoints( MINPOINTS )
	 * @effect If possible, the worm eats all overlapping objects.
	 *       | eat()
	 * @effect If the worm can fall down, it falls down.
	 *       | if (canFall()) then fall()
	 * @throws IllegalStateException
	 *         The worm is unable to jump.
	 *       | ! canJump(timeStep) 
	 */
	public void jump(double timeStep) throws IllegalStateException {
		if (! canJump(timeStep))
			throw new IllegalStateException(); 
		Position positionAfterJump = jumpStep(jumpTime(timeStep));
		setPosition(positionAfterJump);
		if (canFall())
			fall();	
		eat();
		setActionPoints(MINPOINTS);	
	} 
	
	/**
	 * Returns the above time for a potential jump from the current position.
	 * 
	 * @param  timeStep
	 *         Time interval
	 * @return The in-flight position after a given time step. 
	 * 		 | while ( !stopJump(tempPos) )  
	 *       |      jumpTime += timeStep;
	 *		 |      tempPos = jumpStep(jumpTime);
	 *		 | result == jumpTime	
	 * @throws IllegalArgumentException
	 *         The given time step is negative.
	 *       | timeStep < 0
	 */
	public double jumpTime(double timeStep) throws IllegalArgumentException {
		if (timeStep < 0)
			throw new IllegalArgumentException("Time step cannot be negative.");
		double jumpTime = timeStep;
		Position tempPos = jumpStep(jumpTime);
		while ( !stopJump(tempPos) ) {
			jumpTime += timeStep;
			tempPos = jumpStep(jumpTime);
		}
		if (getWorld().isImpassable(tempPos,getRadius()) && getWorld().isInWorld(tempPos,getRadius()))
			return 0;
		return jumpTime;
	}

	/**
	 * Returns whether or not the jump needs to be interrupted.
	 * 
	 * @param  position
	 *         The position to check.
	 * @return True if and only if the circular region with current radius and given position is impassable or 
	 *         if the distance from the current position to the given position exceeds the current radius and 
	 *         the given position is adjacent to impassable terrain.
	 *       | result == getWorld().isImpassable(position,getRadius())
		           || (getPosition().getDistanceTo(position) > getRadius() && getWorld().isAdjacent(position, getRadius()))
	 */
	public boolean stopJump(Position position) {
		return getWorld().isImpassable(position,getRadius())
		       || (getPosition().getDistanceTo(position) > getRadius() && getWorld().isAdjacent(position, getRadius()));
	}
	
	/**
	 * Returns in-flight positions of a jumping worm at any dt seconds after launch.
	 * 
	 * @param  dt
     *         Time after launch.
	 * @return If the worm can jump, an array of the in-flight positions (xdt, ydt) is returned. 
	 * 		   The x-position equals the sum of the current x-coordinate of the position, 
	 *         and the product of the initial velocity and time dt. 
	 *         The y-position equals the sum of the current y-coordinate of the position, 
	 *         and the product of the initial velocity and time dt, diminished with (0.5* World.ACCELERATION*dt^2).
	 *       | return == new Position( (getPosition().getX() + (initialVelocityX()*dt)), 
	 *                     (getPosition().getY() + (initialVelocityY()*dt) - 0.5* World.ACCELERATION*Math.pow(dt, 2)) )
	 * @throws IllegalArgumentException
	 *         The given time step is negative.
	 *       | dt < 0
	 */
	public Position jumpStep(double dt) {
		if (dt < 0)
			throw new IllegalArgumentException("Time step cannot be negative.");
		double initialVelocityX = initialVelocity() * Math.cos(getDirection());
		double initialVelocityY = initialVelocity() * Math.sin(getDirection());
		double xdt = getPosition().getX() + (initialVelocityX * dt);
		double ydt = getPosition().getY() + (initialVelocityY * dt) - 0.5 * World.ACCELERATION * Math.pow(dt, 2);
		return new Position(xdt,ydt);
	}
	
	/**
	 * Returns the force exerted on the worm.
	 * 
	 * @return The force equals the sum of five times the action points of this worm and the product 
	 *         of the mass of the worm and G.
	 *       | return == (5*getActionPoints() + getMass()*World.ACCELERATION)
	 */
	public double getForce() {
		return 5*getActionPoints() + getMass()*World.ACCELERATION;
	}
	
	/**
	 * Returns the initial velocity of the worm. 
	 * 
	 * @return The initial velocity equals 0.5 times the force on the worm divided by the mass of the worm.
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
	public void addAsWeapon(Weapon weapon) throws IllegalArgumentException {
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
	 * Initializes the weapons of a new worm.
	 * 
	 * @effect A new bazooka is added to the weapons of the worm.
	 *       | addAsWeapon( new Bazooka() )
	 * @effect A new rifle is added to the weapons of the worm.
	 *       | addAsWeapon( new Rifle() ) 
	 * @post   The bazooka is selected as weapon for this worm.
	 *       | new.selectedWeapon() == bazooka
	 */
	public void setWeapons() {
		Weapon bazooka = new Bazooka();
		Weapon rifle = new Rifle();
		addAsWeapon(bazooka);
		addAsWeapon(rifle);
		this.selectedWeapon = bazooka;
	}
	
	/**
	 * Returns the name of the weapon that is currently active for the given worm, or null if no weapon is active.
	 */
	@Basic
	public Weapon selectedWeapon() {
		return this.selectedWeapon;
	}
	
	/**
	 * Activates the next weapon of the given worm.
	 * 
	 * @post   If the last weapon of the worm is selected, the first weapon is selected.
	 *       | if (index == this.weapons.size()) then this.selectedWeapon() == this.weapons.get(0)
	 * @post   Otherwise, the next weapon is selected.
	 *       | if (index != this.weapons.size()) then new.selectedWeapon() == weapons.get( this.weapons.indexOf(this.selectedWeapon) + 1 )
	 * @throws IllegalStateException 
	 *         The worm has no weapons.
	 *       | this.weapons.isEmpty()
	 */
	public void selectNextWeapon() throws IllegalStateException {
		if (this.weapons.isEmpty()) {
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
	 * 
	 * @param  yield
	 *         The propulsion yield to shoot with.
	 * @effect The worm shoots using the selected weapon and with given yield.
	 *       | selectedWeapon().shoot(yield)
	 * @effect The current nr of AP's are decreased by the cost of shooting the selected weapon.
	 *       | decreaseActionPoints(selectedWeapon().costOfShooting())
	 * @throws IllegalShootException
	 *         The worm cannot shoot the selected weapon.
	 *       | ! canShoot(selectedWeapon())
	 */
	public void shoot(int yield) throws IllegalShootException {
		if ( !canShoot(selectedWeapon()) )
			throw new IllegalShootException();
		selectedWeapon().shoot(yield);
		decreaseActionPoints(selectedWeapon().costOfShooting());
	}
	
	/**
	 * Returns whether or not the worm can shoot the given weapon.
	 * 
	 * @return result == (getActionPoints() >= weapon.costOfShooting()) &&
				          !(getWorld().isImpassable(getPosition(), getRadius()))
	 */
	public boolean canShoot(Weapon weapon) {
		return  (getActionPoints() >= weapon.costOfShooting()) &&
				 !(getWorld().isImpassable(getPosition(), getRadius()));
	}
	
	/**
	 * Returns the name of the weapon that is currently active for the given worm,
	 * or null if no weapon is active.
	 * 
	 * @return The name of the selected weapon.
	 *        | if (selectedWeapon() != null) then result == selectedWeapon().getName()
	 *        | else result == null
	 */
	public String getSelectedWeapon() {
		if ( selectedWeapon() == null )
			return null;
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
}
