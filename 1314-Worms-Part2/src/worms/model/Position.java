package worms.model;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;

/**
 * A class of positions involving an X- and Y-coordinate.
 * 
 * @invar The x-coordinate of each Position must be a valid x-coordinate.
 *      | isValidPosition(getX())
 * @invar The y-coordinate of each Position must be a valid y-coordinate.
 *      | isValidPosition(getY())
 *      
 * @author Delphine Vandamme & Pieter Noyens
 */
@Value
public class Position {

	/**
	 * Initialize this new position with given X-coordinate and given Y-coordinate.
	 * 
	 * @param  x
	 * 		   the value for the X-coordinate of this new position.
	 * @param  y
	 * 		   the value for the Y-coordinate of this new position.
	 * @post   The initial value for the X-coordinate of this new position
	 * 		   is equal to the given value. 
	 * 		 | new.getX() == x
     * @post   The initial value for the Y-coordinate of this new position 
	 * 		   is equal to the given value. 
	 * 		 | new.getY() == y
	 * @throws IllegalPositionException
	 *         The given position is not a valid position.
	 *       | ((!isValidPosition(x)) || (!isValidPosition(y)) )
     */
	@Raw
	public Position(double x, double y)
	   throws IllegalPositionException{
		if ( (!isValidPosition(x)) || (!isValidPosition(y)) )
			throw new IllegalPositionException(this);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-coordinate of this position.
	 */
	@Basic @Raw @Immutable
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the Y-coordinate of this position.
	 */
	@Basic @Raw @Immutable
	public double getY() {
		return this.y;
	}
	
	/**
	 * Check whether the given coordinate is a valid coordinate for any worm.
	 * 
	 * @param  position
	 *         The position to check.
	 * @return True if and only if the given position is not below 
	 * 		   Double.NEGATIVE_INFINITY and not above Double.POSITIVE_INFINITY.
	 *       | result == (position >=  Double.NEGATIVE_INFINITY) 
	 *                   && (position <= Double.POSITIVE_INFINITY)
	 */
	public static boolean isValidPosition(double position) {
		return (Util.fuzzyGreaterThanOrEqualTo(position, Double.NEGATIVE_INFINITY)) 
			   && (Util.fuzzyLessThanOrEqualTo(position, Double.POSITIVE_INFINITY));
	}

	/**
	 * Variable referencing the X-coordinate of this position.
	 */
	private final double x;
	
	/**
	 * Variable referencing the Y-coordinate of this position.
	 */
	private final double y;
	
	/**
	 * Compute the sum of the x-coordinate of this position and a given value.
	 * 
	 * @param  value
	 * 		   The value to add to the x-coordinate of this position.
	 * @return The x-coordinate of this position is equal to the sum of
	 *         the x-coordinate and the given value.
	 *       | result.getX().add(value)
	 * @throws IllegalPositionException
	 *         The given position is not a valid position.
	 *       | ! isValidPosition(getX() + value)
	 */
	public Position addToX(double value) {
		if (! isValidPosition(getX() + value))
			throw new IllegalPositionException(this);
		return new Position(getX() + value, getY());
	}
	
	/**
	 * Compute the sum of the y-coordinate of this position and a given value.
	 * 
	 * @param  value
	 * 		   The value to add to the y-coordinate of this position.
	 * @return The y-coordinate of this position is equal to the sum of
	 *         the y-coordinate and the given value.
	 *       | result.getY().add(value)
	 * @throws IllegalPositionException
	 *         The given position is not a valid position.
	 *       | ! isValidPosition(getY() + value)
	 */
	public Position addToY(double value) {
		if (! isValidPosition(getY() + value))
			throw new IllegalPositionException(this);
		return new Position(getX(), getY() + value);
	}
	
	/**
	* Check whether this position is equal to the given object.
	*
	* @return 	True if and only if the given object is
	* 			effective, if this position and the given object belong 
	* 		    to the same class, and if this position and the other 
	* 			object interpreted as a position have equal coordinates.
	* 			| result ==
	* 			| ( (other != null)
	* 			| && (this.getClass() == other.getClass())
	* 			| && (this.getX() == 
	* 					 ((Position other).getX()))
	* 			| && (this.getY() == 
	* 					 ((Position other).getY()))
	*/
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		Position otherPosition = (Position) other;
		return 
			( this.getX() == otherPosition.getX() )
	     && ( this.getY() == otherPosition.getY() );
	}

}
