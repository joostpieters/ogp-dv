package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import worms.exceptions.IllegalJumpException;
import worms.util.Util;

public class JumpAbility {

	public JumpAbility(MoveableGameObject object) 
	  throws IllegalArgumentException {
		if ( ! isValidMoveableGameObject(object) )
			throw new IllegalArgumentException("Not a valid moveable gameObject");
		this.object = object;
	}
	
	/**
	 * Returns the movable game object.
	 */
	@Basic @Raw @Immutable
	public MoveableGameObject getGameObject() {
		return this.object;
	}
	
	/**
	 * Check whether the given game object is a valid game object.
	 * 
	 * @param  object
	 *         The game object to check.
	 * @return result == (object != null)
	 */
	public static boolean isValidMoveableGameObject(MoveableGameObject object) {
		return  (object != null) ;
	}
	
	/**
	 * Variable referencing the game object who has this jumping ability.
	 */
	private final MoveableGameObject object;
	
	
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
		return  !(Util.fuzzyEquals(jumpTime(timeStep), 0));
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
		Position positionAfterJump = new Position(jumpStep(jumpTime(timeStep))[0], jumpStep(jumpTime(timeStep))[1]);
		//setPosition(positionAfterJump);
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

		return jumpTime;
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
	public double[] jumpStep(double dt) {	
		double xdt = getGameObject().getPosition().getX() + (initialVelocityX()*dt);
		double ydt = getGameObject().getPosition().getY() + (initialVelocityY()*dt) - 0.5*G*Math.pow(dt, 2);
		double[] dPosition = {xdt, ydt};
		return dPosition;
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
		if (getGameObject().getMass() == 0)
			throw new ArithmeticException();
		return getGameObject().getForce() * 0.5 / getGameObject().getMass();
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
		return initialVelocity() * Math.cos(getGameObject().getDirection());
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
		return initialVelocity() * Math.sin(getGameObject().getDirection());
	}
	
	
	/**
	 * Variable registering the Earth’s standard acceleration that applies to all worms.
	 */
	private static final double G = 9.80665;

}
