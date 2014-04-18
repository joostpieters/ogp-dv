package worms.model;
import worms.exceptions.*;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of game objects attached to worlds.
 * 
 * @invar  Each game object must have a proper world to which it is attached.
 * 		 | hasProperWorld()
 * 
 * @author Delphine
 *
 */
public abstract class GameObject {

	/**
	 * Initialize this new game object with given position(x,y) and given radius 
	 * and given world to attach to.
	 * 
	 * @effect if (world != null)
	 *           then world.addAsGameObject(this)
	 */
	@Raw @Model
	protected GameObject(World world) //, double x, double y, double radius) 
	  throws IllegalPositionException, IllegalRadiusException {
		//setPosition(new Position(x,y));
		//setRadius(radius);
		if (world != null)
			world.addAsGameObject(this);
		setWorld(world);
	}
	
	/**
	 * Returns the position of this game object.
	 */
	@Basic @Raw
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Returns whether the given position is a valid position for this game object.
	 * 
	 * @param position
	 * 		  The position to be checked.
	 * @return True if and only if the given position references an effective position.
     *       | result == (getPosition != null)	
	 */
	@Raw
	public boolean canHaveAsPosition(Position position) {
		return (getPosition() != null);
	}
	
	/**
	 * Sets the position of this game object to the given position.
	 * 
	 * @param position
	 * 	      The new position for this game object.
	 * 
	 * @post  The position of this new game object is equal to a new Position(x,y), given x and y.
     *      | new.getPosition() = new Position(x,y)
	 */
	@Raw
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Variable registering the position of this game object.
	 */
	private Position position;
	
	/**
	 * Returns the radius of the worm.
	 */
	@Basic @Raw
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Check whether the given radius is a valid radius for a worm.
	 * 
	 * @param  radius
	 *         The radius to check.
	 * @return True if and only if the given radius is not below 
	 * 		   the minimal radius and not above Double.POSITIVE_INFINITY .
	 *       | result == (radius >= getMinimalRadius()) 
	 *                   && (radius <= Double.POSITIVE_INFINITY)
	 */
	public static boolean isValidRadius(double radius) {
		return (Util.fuzzyGreaterThanOrEqualTo(radius, Double.NEGATIVE_INFINITY)) 
				&& (Util.fuzzyLessThanOrEqualTo(radius, Double.POSITIVE_INFINITY));
	}
	
	/**
	 * Sets the radius of the worm to the given value.
	 * 
	 * @param  radius
	 *		   The new radius for this worm.
	 * @post   If the given radius is a valid radius for this worm, 
	 *         the radius of this worm is equal to the given radius.
     *       | new.getRadius() == radius
     * @throws IllegalRadiusException
     * 		   The given radius is not a valid radius for any worm.  
     * 		 | ! isValidRadius(radius)    
	 */
	@Raw
	public void setRadius(double radius)
	   throws IllegalRadiusException {
		if (! isValidRadius(radius))
			throw new IllegalRadiusException(radius,this);
		this.radius = radius;
	}
	
	/**
	 * Variable registering the radius of the worm.
	 */
	private double radius;

	
	/**
	 * Terminate this game object.
	 * 
	 * @post new.isTerminated()
	 */
	public void terminate() {
		this.isTerminated = true;
	}
	
	/**
	 * Check whether this game object is terminated.
	 */
	@Basic @Raw
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	/**
	 * Variable registering whether or ot this game object is terminated.
	 */
	private boolean isTerminated = false;
	
	/**
	 * Return the world to which this game object is attached.
	 *   A null reference is returned if this game object is not attached to any world.
	 */
	@Basic @Raw
	public World getWorld() {
		return this.world;
	}

	/**
	 * Check whether this game object can be attached to the given world.
	 * 
	 * @param  world
	 * 		   The world to check.
	 * @return result == ( (world == null) || world.canHaveAsGameObject(this) )
	 */
	@Raw
	public boolean canHaveAsWorld(World world) {
		return ( (world == null) || world.canHaveAsGameObject(this) );
	}
	
	/**
	 * Check whether this game object has a proper world to which it is attached.
	 * 
	 * @return result == ( canHaveAsWorld(getWorld()) && ( (getWorld() == null)
	 *                     || getWorld().hasAsGameObject(this)))
	 */
	@Raw
	public boolean hasProperWorld() {
		return ( canHaveAsWorld(getWorld()) && ( (getWorld() == null) 
				 || getWorld().hasAsGameObject(this)));
	}
	
	/**
	 * Set the world to which this game object is attached to the given world.
	 * 
	 * @param world
	 * 		  The world to attach this game object to.
	 * @pre   if (world != null) 
	 *          then world.hasAsGameObject(this)
	 * @pre   if ( (world == null) && (getWorld() != null) )
	 *          then !getWorld().hasAsGameObject(this)
	 * @post  new.getWorld() == world
	 */
	public void setWorld(@Raw World world) {
		assert ( (world == null) || world.hasAsGameObject(this) );
		assert ( (world != null) || (getWorld() == null) || (! getWorld().hasAsGameObject(this)));
		this.world = world;
		
	}
	
	/**
	 * Variable registering the world to which this game object is attached.
	 */
	private World world = null;

}
