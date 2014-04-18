package worms.exceptions;
import be.kuleuven.cs.som.annotate.*;
import worms.model.*;

@SuppressWarnings("serial")
/**
 * A class for signaling illegal radius of worms.
 * 
 * @author   Delphine Vandamme & Pieter Noyens
 */
public class IllegalRadiusException extends RuntimeException {
	
	/**
	 * Initialize this new illegal radius exception with 
	 * given radius and given game object.
	 *
	 * @param   radius
	 *          The radius for this new illegal radius exception.
	 * @param   worm
	 *          The worm for this new illegal radius exception.
	 * @post    The radius is equal to the given radius.
	 *          | new.getRadius() == radius
	 * @post    The worm is equal to the given game object.
	 *          | new.getWorm() == game object
	 */
	public IllegalRadiusException(double radius, GameObject object) {
		this.radius = radius;
		this.object = object;
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
	 * Return the game object of this illegal radius exception.
	 */
	@Basic @Raw @Immutable
	public GameObject getGameObject() {
		return object;
	}
	
	/**
	 * Variable referencing the game object of this illegal radius exception.
	 */
	private final GameObject object;
	
}
