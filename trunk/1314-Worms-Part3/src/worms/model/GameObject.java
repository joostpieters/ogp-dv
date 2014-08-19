package worms.model;
import worms.exceptions.*;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of game objects attached to a world.
 * 
 * @invar  Each game object must have a proper world to which it is attached.
 * 		   | hasProperWorld()
 * @invar  The radius of each game object must be a valid radius for a game object.
 *         | isValidRadius(getRadius())
 * @invar  The position of each game object must be a valid position for a game object.
 *         | canHaveAsPosition(getPosition())  
 * 
 * @author Delphine Vandamme
 *
 */
public abstract class GameObject {

	/**
	 * Initialize this new game object with given position(x,y) and given radius 
	 * and given world to attach to.
	 * 
	 * @param  radius 
	 * 		   The radius of the new game object (in meter)
	 * @param  world
	 * 		   The world in which to place the created game object  
	 * @param  position
	 * 		   The position of this new game object (in meter)
	 * @effect if (world != null)
	 *           then world.addAsGameObject(this)
	 * @effect The position of this new game object is equal to the given position.
     *       | setPosition(position)
     * @effect The radius of this new game object is equal to the given radius.
     * 		 | setRadius(radius) 
     * @throws IllegalRadiusException
	 * 		   The given radius is not a valid radius for a game object.
	 *       | ! isValidRadius(radius) 
	 * @throws IllegalPositionException
	 * 		   The given position is not a valid position for a game object.
	 *       | ! position.canHaveAsPosition(position)  
	 */
	@Raw @Model
	protected GameObject(World world, Position position, double radius) 
	  throws IllegalPositionException, IllegalRadiusException {
		setPosition(position);
		setRadius(radius);
		if (world != null)
			world.addAsGameObject(this);
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
	 * Returns the radius of the game object.
	 */
	@Basic @Raw
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Check whether the given radius is a valid radius for a game object.
	 * 
	 * @param  radius
	 *         The radius to check.
	 * @return True if and only if the given radius is not below the minimal radius and not above the maximal radius.
	 *       | result == (radius >= getMinimalRadius()) 
	 *                   && (radius <= getMaximalRadius())
	 */
	public boolean canHaveAsRadius(double radius) {
		return (Util.fuzzyGreaterThanOrEqualTo(radius, getMinimalRadius())) 
				&& (Util.fuzzyLessThanOrEqualTo(radius, getMaximalRadius()));
	}
	
	/**
	 * Sets the radius of the game object to the given value.
	 * 
	 * @param  radius
	 *		   The new radius for this game object.
	 * @post   If the given radius is a valid radius for this game object, 
	 *         the radius of this game object is equal to the given radius.
     *       | new.getRadius() == radius
     * @throws IllegalRadiusException
     * 		   The given radius is not a valid radius for any game object.  
     * 		 | ! isValidRadius(radius)    
	 */
	@Raw
	public void setRadius(double radius)
	   throws IllegalRadiusException {
		if (! canHaveAsRadius(radius))
			throw new IllegalRadiusException(radius,this);
		this.radius = radius;
	}
	
	/**
	 * Returns the minimal radius of a game object.
	 */
	@Basic 
	public double getMinimalRadius() {
		return 0.0;
	}
	
	/**
	 * Returns the maximal radius of a game object.
	 */
	@Basic
	public double getMaximalRadius() {
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Variable registering the radius of the game object.
	 */
	private double radius;
	
	/**
	 * Terminate this game object by removing it from the world it is attached to.
	 * 
	 * @effect getWorld().removeAsGameObject(this)
	 * @effect setToActive(false)
	 * @post   new.isTerminated()
	 */
	public void terminate() {
		getWorld().removeAsGameObject(this);
		setToActive(false);
		this.isTerminated = true;
	}
	
	/**
	 * Check whether this game object is terminated.
	 */
	@Basic
	public boolean isAlive() {
		return (! this.isTerminated);
	}
	
	/**
	 * Variable registering whether or not this game object is terminated.
	 */
	private boolean isTerminated = false;
	
	/**
	 * Activate or deactivate this game object.
	 * 
	 * @param isActive
	 *        True to activate the game object, false to deactivate the game object.
	 * @post  new.isActive() == isActive
	 */
	public void setToActive (boolean isActive) {
		this.isActive =  isActive;
	}
	
	/**
	 * Check whether this game object is active.
	 */
	@Basic @Raw
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * Variable registering whether or not this game object is active.
	 */
	private boolean isActive = false;
	
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
	 * @return result == ( canHaveAsWorld(getWorld()) && ( (getWorld() == null) || getWorld().hasAsGameObject(this)))
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
