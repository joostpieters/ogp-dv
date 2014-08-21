package worms.model;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import worms.model.abilities.JumpAbility;

/**
 * A class of Projectiles involving a worm and a yield.
 * 
 * @author Delphine
 *
 */
public abstract class Projectile extends MobileGameObject implements JumpAbility {

	/**
	 * Initialize this new projectile with given worm and yield.
	 *
	 * @param  worm
	 *         The worm that shoots this new projectile.
	 * @param  yield
	 *         The yield to shoot this new projectile with.
	 * @effect super( worm.getWorld(), new Position(0,0), 0, worm.getDirection() )
	 * @effect setForce(yield)
	 * @effect setPosition(new Position( worm.getPosition().getX() + (worm.getRadius() + this.getRadius())*Math.cos(worm.getDirection()),
	 * 									worm.getPosition().getY() + (worm.getRadius() + this.getRadius())*Math.sin(worm.getDirection())));
	 * @effect setToActive(true);
	 * @throws IllegalArgumentException
	 *         ! Projectile.isValidYield(yield)
	 */
	public Projectile(Worm worm, int yield) throws IllegalArgumentException {
		super(worm.getWorld(), new Position(0,0), 0, worm.getDirection());
		
		if ( !Projectile.isValidYield(yield) ) 
			throw new IllegalArgumentException("The given yield is not valid.");

		setForce(yield);
		double x = worm.getPosition().getX() + (worm.getRadius() + this.getRadius())*Math.cos(worm.getDirection());
		double y = worm.getPosition().getY() + (worm.getRadius() + this.getRadius())*Math.sin(worm.getDirection());
		setPosition(new Position(x,y));
		setToActive(true);
	}
	
	/**
	 * Checks whether the given yield is a valid yield.
	 * 
	 * @param  yield
	 *         The yield to check.
	 * @return True if and only if the yield is between 0 and 100.
	 *       | result == yield > 0 && yield <= 100
	 */
	public static boolean isValidYield(int yield) {
		return (yield >= 0) && (yield <= 100);
	}

	/**
	 * Computes the radius of this projectile as a function of its mass.
	 * 
	 * @return result == Math.cbrt((getMass() * 3.0) / (Projectile.DENSITY * 4.0 * Math.PI))
	 */
	@Override
	public double getRadius() {
		return Math.cbrt((getMass() * 3.0) / (Projectile.DENSITY * 4.0 * Math.PI));
	}
	
	/**
	 * Variable registering the density of a projectile that applies to all projectiles.
	 */
	private static final double DENSITY = 7800;
	
	/**
	 * Returns the density of all projectiles.
	 */
	@Override
	public double getDensity() {
		return Projectile.DENSITY;
	}
	
	/**
	 * Returns the force exerted on the projectile.
	 */
	@Basic
	public double getForce() {
		return this.force;
	}
	
	/**
	 * Sets the force exerted on the projectile using the given yield.
	 * 
	 * @param yield
	 *        The yield to shoot the projectile with.
	 */
	public abstract void setForce(int yield);
	
	/**
	 * Variable registering the force exerted on the projectile.
	 */
	protected double force;
	
	/**
	 * Returns the cost in hit points when a worm is hit by this projectile.
	 */
	public abstract int costInHitPoints();
    
	/**
	 * Returns whether or not the projectile can jump.
	 * 
	 * @return result == true    
	 */
	public boolean canJump(double timeStep) {
		return true;
	}
	
	/**
	 * Changes the position of the projectile as the result of a jump from the current position.
	 * 
	 * @effect setPosition( jumpStep(jumpTime(timeStep)) )
	 * @effect for each worm in getWorld().getOverlappingObjectsOfType(Worm.class,positionAfterJump, getRadius()):
	 *              worm.decreaseHitPoints(costInHitPoints())
	 * @effect this.terminate()
	 * @effect if (! canJump(timeStep) ) then this.terminate()
	 * @throws IllegalStateException
	 *         ! canJump(timeStep) 
	 */
	public void jump(double timeStep) throws IllegalStateException {
		if (! canJump(timeStep)) {
			this.terminate();
		    throw new IllegalStateException();
		}
		Position positionAfterJump = jumpStep(jumpTime(timeStep));
		setPosition(positionAfterJump);
		
		List<Worm> hitWorms = getWorld().getOverlappingObjectsOfType(Worm.class,positionAfterJump, getRadius());
		for(Worm worm : hitWorms)
			worm.decreaseHitPoints(costInHitPoints());
		this.terminate();
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
	public double jumpTime(double timeStep) {
		if (timeStep < 0)
			throw new IllegalArgumentException("Time step cannot be negative.");
		double jumpTime = timeStep;
		Position tempPos = jumpStep(jumpTime);
		while (!stopJump(tempPos) ) {
			jumpTime += timeStep;
			tempPos = jumpStep(jumpTime);
		}
		return jumpTime;
	}
	
	/**
	 * Returns whether or not the jump needs to be interrupted.
	 * 
	 * @param  position
	 *         The position to check.
	 * @return result == ( getWorld().isImpassable(position, getRadius())
		                  || getWorld().overlapWithObjectOfType(Worm.class, position, getRadius())
		                  || (getPosition().getDistanceTo(position) > getRadius()
                          && getWorld().isAdjacent(position, getRadius())) )
	 */
	public boolean stopJump(Position position) {
		return ( getWorld().isImpassable(position, getRadius())
		         || getWorld().overlapWithObjectOfType(Worm.class, position, getRadius())
		         || (getPosition().getDistanceTo(position) > getRadius()
                      && getWorld().isAdjacent(position, getRadius())) );
	}
	
	/**
	 * Returns in-flight positions of a jumping projectile at any dt seconds after launch.
	 * 
	 * @param  dt
     *         Time after launch.
	 * @return result.getX() == getPosition().getX() + (initialVelocity() * Math.cos(getDirection()) * dt)
	 *         result.getY() == getPosition().getY() + (initialVelocity() * Math.sin(getDirection()) * dt) - 0.5 * World.ACCELERATION * Math.pow(dt, 2)	 
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
	 * Returns the initial velocity of the projectile. 
	 * 
	 * @return The initial velocity equals 0.5 times the force on the worm divided by the mass of the projectile.
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
	
}
