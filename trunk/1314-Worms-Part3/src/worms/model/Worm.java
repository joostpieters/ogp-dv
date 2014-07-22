package worms.model;
import java.util.*;

import worms.model.abilities.*;
import worms.model.weapons.*;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;

/**
 * A class of worms involving a world, name, position, direction and radius.
 * A worm can move and jump.
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
public class Worm extends Character implements JumpAbility, ShootAbility {

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
	public Worm(World world, Position position, double direction, double radius, String name, Program program) 
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
	@Override
	public double getDensity() {
		return Worm.DENSITY;
	}
	
	/**
	 * Variable registering the density of a worm that applies to all worms.
	 */
	private static final double DENSITY = 1062;

	/**
	 * 
	 */
	@Override
	public Position getMovePosition() {
		Position adjacentOrPassablePos = getWorld().getAdjacentorPassablePositionTo(this, getRadius());
		Position currentPos = new Position(getPosition());
		Position tempPos = currentPos.addToX(getRadius()*Math.cos(getDirection())).addToY(getRadius()*Math.sin(getDirection()));
		if ( adjacentOrPassablePos == null && !getWorld().isInWorld(tempPos, getRadius()) )
			return tempPos;	
		else 
			return adjacentOrPassablePos;
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
	 * Returns the cost in hit points of falling down a certain distance.
	 *  
	 * @param  distance
	 * 		   The distance of falling down of which the cost is to be computed.
	 * @return The cost of falling down a given distance rounded down to the next integer.
	 * 	     | result == ( (int) Math.round(3*distance) ) 
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
	 * 
	 * @param 
	 */
	@Override
	public void terminate() {
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
			setHitPoints(getHitPoints()+10);
		}
		super.setToActive(isActive);
	}

	/**
	 * 
	 */
	@Override
	public double getEffectOfEating(GameObject object) {
		Food ration = (Food) object;
		return (getRadius() * (1 + ration.getGrowthEffect()));
	}
	
	/**
	 * 
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
	 * @return True if and only if the number action points of 
	 * 		   this worm is greater than the minimum number 
	 * 		   of action points, the direction does not exceed Pi and is not less than zero.
	 *       |  result == (getActionPoints() >= minActionPoints &&  getDirection() >= 0 
	 *		 |		       && Util.fuzzyLessThanOrEqualTo(getDirection(), Math.PI)      
	 */
	public boolean canJump(double timeStep) {
		return ( (getActionPoints() > MINPOINTS) 
				&& !(Util.fuzzyEquals(jumpTime(timeStep), 0))
				&& !(getWorld().isImpassable(getPosition(), this.getRadius())) );
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
	public void jump(double timeStep)
	   throws IllegalJumpException {
		if (! canJump(timeStep))
			throw new IllegalJumpException(); 
		Position positionAfterJump = jumpStep(jumpTime(timeStep));
		setPosition(positionAfterJump);
		setActionPoints(MINPOINTS);
		eat();
		if (canFall())
			fall();	
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
		if (getWorld().isImpassable(tempPos,getRadius()) && getWorld().isInWorld(tempPos,getRadius()))
			return 0;
		return jumpTime;
	}

	/**
	 * 
	 */
	public boolean stopJump(Position position) {
		return getWorld().isImpassable(position,getRadius())
		       || (getPosition().getDistanceTo(position) > getRadius() && getWorld().isAdjacent(position, getRadius()));
	}
	
	/**
	 * Returns in-flight positions of a jumping worm at any dt seconds after launch.
	 * 
	 * @param  dt
	 * 
	 * ²
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
		double initialVelocityX = initialVelocity() * Math.cos(getDirection());
		double initialVelocityY = initialVelocity() * Math.sin(getDirection());
		double xdt = getPosition().getX() + (initialVelocityX * dt);
		double ydt = getPosition().getY() + (initialVelocityY * dt) - 0.5 * World.ACCELERATION * Math.pow(dt, 2);
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
		return 5*getActionPoints() + getMass()*World.ACCELERATION;
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
	public void setWeapons() {
		Weapon bazooka = new Bazooka(this);
		Weapon rifle = new Rifle(this);
		addAsWeapon(bazooka);
		addAsWeapon(rifle);
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
		if ( !canShoot(selectedWeapon()) )
			throw new IllegalShootException();
		selectedWeapon().shoot(yield);
		decreaseActionPoints(selectedWeapon().costOfShooting());
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canShoot(Weapon weapon) {
		return  (getActionPoints() >= weapon.costOfShooting()) &&
				 !(getWorld().isImpassable(getPosition(), getRadius()));
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
}
