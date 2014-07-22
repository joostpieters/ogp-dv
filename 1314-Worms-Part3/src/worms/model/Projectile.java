package worms.model;
import java.util.List;
import worms.exceptions.*;
import worms.model.abilities.JumpAbility;

/**
 * 
 * @author Delphine
 *
 */
public abstract class Projectile extends MoveableGameObject implements JumpAbility {

	/**
	 * 
	 * @param worm
	 * @param yield
	 * @throws IllegalDirectionException
	 * @throws IllegalPositionException
	 * @throws IllegalRadiusException
	 * @throws IllegalWeaponException
	 */
	public Projectile(Worm worm, int yield)  
	  throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException, IllegalWeaponException {
		super(worm.getWorld(), new Position(0,0), 0, worm.getDirection());
		if (!Projectile.isValidYield(yield)) {
			throw new IllegalArgumentException("The given yield is not valid.");
		}

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
	 *       | result == yield > 0 && yield < 100
	 */
	public static boolean isValidYield(int yield) {
		return (yield >= 0) && (yield <= 100);
	}

	
	/**
	 * Computes the radius of this projectile as a function of its mass.
	 * @return radius
	 */
	@Override
	public double getRadius() {
		return Math.cbrt((getMass() * 3.0) / (Projectile.DENSITY * 4.0 * Math.PI));
	}
	
	/**
	 * Variable registering the density of a projectile that applies to all projectiles.
	 */
	private static final double DENSITY = 7800;
	
	@Override
	public double getDensity() {
		return Projectile.DENSITY;
	}
	
	public double getForce() {
		return this.force;
	}
	
	public abstract void setForce(int yield);
	
	protected double force;
	
	public abstract int costInHitPoints();
    
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
		//return !(Util.fuzzyEquals(jumpTime(timeStep), 0) );
		return true;
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
		if (! canJump(timeStep)) {
			this.terminate();
		    throw new IllegalJumpException();
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
	 * @return The jump time equals the jump distance divided by the initial velocity.
	 * 		 | result == distanceJump() / initialVelocityX()
	 * @throws ArithmeticException
	 *         Division by zero.
	 *       | initialVelocityX() == 0
	 */
	public double jumpTime(double timeStep) {
		double jumpTime = timeStep;
		Position tempPos = jumpStep(jumpTime);
		while (!stopJump(tempPos) ) {
			jumpTime += timeStep;
			tempPos = jumpStep(jumpTime);
		}
		return jumpTime;
	}
	
	public boolean stopJump(Position position) {
		return ( getWorld().isImpassable(position, getRadius())
		         || getWorld().overlapWithObjectOfType(Worm.class, position, getRadius())
		         || (getPosition().getDistanceTo(position) > getRadius()
                      && getWorld().isAdjacent(position, getRadius())) );
	}
	
	/**
	 * Returns in-flight positions of a jumping worm at any dt seconds after launch.w
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
	
}
