package worms.model;
import be.kuleuven.cs.som.annotate.*;

@SuppressWarnings("serial")
/**
 * A class for signaling illegal radius of worms.
 * 
 * @author   Delphine Vandamme & Pieter Noyens
 */
public class IllegalRadiusException extends RuntimeException {
	
	/**
	 * Initialize this new illegal radius exception with 
	 * given radius and given worm.
	 *
	 * @param   radius
	 *          The radius for this new illegal radius exception.
	 * @param   worm
	 *          The worm for this new illegal radius exception.
	 * @post    The radius is equal to the given radius.
	 *          | new.getRadius() == radius
	 * @post    The worm is equal to the given worm.
	 *          | new.getWorm() == worm
	 */
	public IllegalRadiusException(double radius, Worm worm) {
		this.radius = radius;
		this.worm = worm;
	}
	
	/**
	 * Return the radius of this illegal radius exception.
	 */
	@Basic @Raw @Immutable
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Variable registering the radius of this illegal radius exception.
	 */
	private final double radius;
	
	/**
	 * Return the worm of this illegal radius exception.
	 */
	@Basic @Raw @Immutable
	public Worm getWorm() {
		return worm;
	}
	
	/**
	 * Variable referencing the worm of this illegal radius exception.
	 */
	private final Worm worm;
	
}
