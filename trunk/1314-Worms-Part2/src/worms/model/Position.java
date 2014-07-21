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
	   throws IllegalPositionException {
		if ( (!isValidPosition(x)) || (!isValidPosition(y)) )
			throw new IllegalPositionException(this);
		this.x = x;
		this.y = y;
	}
	
	public Position(Position position) {
		this(position.getX(),position.getY());
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
	 * Rotate this position by a given divergence,
	 * about a position at a given distance and angle from this position.
	 * 
	 * @param divergence
	 * @param distance
	 * @param angle
	 * @return
	 */
	public Position rotateBy(double divergence, double distance, double angle) {
		double x = getX()+ distance*Math.cos(angle+divergence);
		double y = getY() + distance*Math.sin(angle+divergence);
		if (! isValidPosition(x) || ! isValidPosition(y))
			throw new IllegalPositionException(this);
		return new Position(x, y);
	}

	/**
	 * 
	 * @param position
	 * @return
	 */
	public double getSlope(Position position) {
		double dx = position.getX() - this.getX();
		double dy = position.getY() - this.getY();
		return Math.atan(dy/dx);
	}

	/**
	 * Return the distance between this position and the given position pos.
	 * 
	 * @param pos
	 * @return
	 */
	public double getDistanceTo(Position pos) {
		return (Math.sqrt( Math.pow((getX() - pos.getX()), 2) + Math.pow((getY() - pos.getY()), 2)));
		
	}
	
	
}
