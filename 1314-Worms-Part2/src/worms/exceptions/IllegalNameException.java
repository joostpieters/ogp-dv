package worms.exceptions;
import be.kuleuven.cs.som.annotate.*;


@SuppressWarnings("serial")
/**
 * A class for signaling illegal names of worms.
 * 
 * @author   Delphine Vandamme & Pieter Noyens
 */
public class IllegalNameException extends RuntimeException {
	
	/**
	 * Initialize this new illegal name exception with 
	 * given name and given worm.
	 *
	 * @param   name
	 *          The name for this new illegal name exception.
	 * @param   worm
	 *          The worm for this new illegal name exception.
	 * @post    The name is equal to the given name.
	 *          | new.getName() == name
	 * @post    The worm is equal to the given worm.
	 *          | new.getWorm() == worm
	 */
	public IllegalNameException(String name) {
		this.name = name;
	}
	
	/**
	 * Return the name of this illegal name exception.
	 */
	@Basic @Raw @Immutable
	public String getName() {
		return name;
	}
	
	/**
	 * Variable registering the name of this illegal name exception.
	 */
	private final String name;
	
	
}
