package worms.exceptions;
import be.kuleuven.cs.som.annotate.*;
import worms.model.*;

@SuppressWarnings("serial")
/**
 * A class for signaling illegal positions of worms.
 * 
 * @author   Delphine Vandamme & Pieter Noyens
 */
public class IllegalPositionException extends RuntimeException {
	
	/**
	 * Initialize this new illegal position exception with 
	 * given position and given worm.
	 *
	 * @param   position
	 *          The position for this new illegal position exception.
	 * @post    The position is equal to the given position.
	 *          | new.getPosition() == position
	 */
	public IllegalPositionException(Position position) {
		this.position = position;
	}
	
	/**
	 * Return the position of this illegal position exception.
	 */
	@Basic @Raw @Immutable
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Variable registering the position of this illegal position exception.
	 */
	private final Position position;

}
