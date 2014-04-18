package worms.model;
import java.util.*;
import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;
import worms.util.Util;

/**
 * A class of worlds involving a width, height, passableMap and a random number generator
 * and with game objects.
 * 
 * @invar    The game objects attached to each world must be proper game objects for that world.
 * 		   | hasProperGameObjects()
 * 
 * @author   Delphine Vandamme 
 */
public class World {
	
	/**
	 * Initialize this new world with given width, height, passableMap and random nr generator,
	 * with no game objects attached to it.
	 *
	 * @param  width
	 * 		   The width of the world (in meter)
	 * @param  height
	 * 	       The height of the world (in meter)
	 * @param  passableMap
	 * 		   A rectangular matrix indicating which parts of the terrain are passable and impassable.
	 * @param  random 
	 * 		   A random number generator, seeded with the value obtained from GUIOptions
	 * @post   No game objects are attached to this new bank account.
	 * 	     | new.getNbGameObjects() == 0 
	 */
	@Raw 
	public World(double width, double height, boolean[][] passableMap, Random random) {
		if (! isValidDimension(width, height))
			throw new IllegalDimensionException();
		this.width = width;
		this.height = height;
		if (random != null)
			this.random = random;
	}
	
	/**
	 * Variable registering the random number generator for this world.
	 */
	private Random random;
	
	/**
	 * Terminate this world.
	 * 
	 * @post new.isTerminated()
	 * @effect for each object getAllGameObjects():
	 * 	          if (! object.isTerminated())
	 * 				then this.removeAsGameObject(object)
	 */
	public void terminate() {
		for (GameObject object: objects)
			if (! object.isTerminated()) {
				object.setWorld(null);
				this.objects.remove(object);
			}
		this.isTerminated = true;
	}
	
	/**
	 * Check whether this world is terminated.
	 */
	@Basic @Raw
	public boolean isTerminated() {
		return isTerminated;
	}
	
	/**
	 * Variable registering whether or not this world is terminated.
	 */
	private boolean isTerminated;
	
	/**
	 * Check whether the given height and width is a valid height and width for a worm.
	 * 
	 * @param  width
	 *         The width to check.
	 * @param  height
	 *         The height to check.
	 * @return result == (width >= 0) && (width <= maxDimension) &&
	 * 				     (height >= 0) && (height <= maxDimension)
	 */
	public static boolean isValidDimension(double width, double height) {
		return (Util.fuzzyGreaterThanOrEqualTo(width, 0)) 
				&& (Util.fuzzyLessThanOrEqualTo(width, maxDimension)) 
				&& (Util.fuzzyGreaterThanOrEqualTo(height, 0)) 
				&& (Util.fuzzyLessThanOrEqualTo(height, maxDimension));
	}
	
	/**
	 * Variable registering the width the world.
	 */
	private final double width;
	
	/**
	 * Variable registering the height of the world.
	 */
	private final double height;
	
	/**
	 * Variable registering the upper bound on the width and height of a world.
	 */
	public static final double maxDimension = Double.MAX_VALUE;
	
	/**
	 * Check whether this world has the given game object as one
	 * of the game objects attached to it.
	 * 
	 * @param object
	 * 		  The game object to check.
	 */
	@Basic @Raw
	public boolean hasAsGameObject(GameObject object) {
		return this.objects.contains(objects);
	}
	
	/**
	 * Check whether this world can have the given game object as one
	 * of its game objects.
	 * 
	 * @param  object
	 * 		   The game object to check.
	 * @return if (objects == null)
	 * 			 then result == false
	 * 	       else result == ( (! this.isTerminated()) || objects.isTerminated() )
	 */
	@Raw
	public boolean canHaveAsGameObject(GameObject object) {
		return ( (objects != null) && ( (! this.isTerminated()) || object.isTerminated()) );
	}
	
	/**
	 * Check whether this world has proper game objects attached to it.
	 * 
	 * @return result == for each object in GameObject:
	 * 					    ( if (this.hasAsGameObject(object))
	 *                          then canHaveAsGameObject(object) && (object.getWorld() == this) )
	 */
	@Raw
	public boolean hasProperGameObjects() {
		for (GameObject object: this.objects) {
			if (! canHaveAsGameObject(object))
				return false;
			if (object.getWorld() != this)
				return false;
		}
		return true;
	}
	
	/**
	 * Return a set collecting all game objects attached to this world.
	 * 
	 * @return result != null
	 * @return for each object in GameObject:
	 *            result.contains(object) == this.hasAsGameObject(object)
	 */
	public Set<GameObject> getAllGameObjects() {
		return new HashSet<GameObject>(this.objects);
	}

	/**
	 * Add the given game object to the set of game objects attached to this world.
	 * 
	 * @param  object
	 * 		   The game object to be added.
	 * @post   new.hasAsGameObject(object)
	 * @post   (new object).getWorld() == this
	 * @throws IllegalArgumentException
	 * 		   ! canHaveAsGameObject(object)
	 * @throws IllegalArgumentException
	 * 		   ( (object != null) && (object.getWorld() != null) )
	 */
	public void addAsGameObject(GameObject object) 
	  throws IllegalArgumentException, IllegalMassException {
		if (! canHaveAsGameObject(object))
			throw new IllegalArgumentException();
		if (object.getWorld() != null)
			throw new IllegalArgumentException();
		this.objects.add(object);
		object.setWorld(this);
	}
	
	/**
	 * Remove the given game object from the set of game objects attached to this world.
	 * 
	 * @param object
	 * 		  The game object to be removed.
	 * @post  ! new.hasAsGameObject(object)
	 * @post  if (hasAsGameObject(object))
	 * 			((new object).getWorld() == null)
	 */
	public void removeAsGameObject(GameObject object) {
		if (hasAsGameObject(object))
			this.objects.remove(object);
		object.setWorld(null);
	}
	
	/**
	 * Set collecting references to game objects attached to this world.
	 * 
	 * @invar objects != null
	 * @invar for each object in objects:
	 *          canHaveAsGameObject(object)
	 * @invar for each object in objects:
	 *          (objects.getWorld() == this)
	 */
	private final Set<GameObject> objects = new HashSet<GameObject>();
	
	/**
	 * Create and add a new worm to the given world. The worm is placed at a random adjacent position.
	 */
	public void addNewWorm() {
		double[] pos = generateRandomPosition(1);
		double x = pos[0];
		double y = pos[1];
		//Worm worm = new Worm(this, 0, 10, 0, 1.5, "Delphine");
		//addAsGameObject(worm);
		//String name = new String();
		//for (Worm worm : getWorms())
		//	name = worm.getName();
		System.out.println(x); System.out.println(y);
	}

	/**
	 * Returns a random adjacent position in this world.
	 *
	 * @param  radius
	 *         the radius of the circulair domain to be checked.
	 * @return 
	 */
	public double[] generateRandomPosition(double radius) {
	    double x = -width + (2*width) * random.nextDouble();
		double y = -height + (2*height) * random.nextDouble();
		while (! isAdjacent(x, y, radius) || (Util.fuzzyEquals(x, 0) && Util.fuzzyEquals(y, 0)) ) {
			double rico = Math.abs(y/x);
			if (Util.fuzzyGreaterThanOrEqualTo(x, 0))
				x--;
			else
				x++;
			if (Util.fuzzyGreaterThanOrEqualTo(y, 0))
				y -= rico;
			else
				y += rico;
		}
		double[] randomPosition = {x, y};
		return randomPosition;
	}
	
	/**
	 * Returns the active projectile in the world, or null if no active projectile exists.
	 */
	public Projectile getActiveProjectile() {
		return null;
		
	}

	/**
	 * Returns the active worm in the given world (i.e., the worm whose turn it is).
	 */
	public Worm getCurrentWorm() {
		return null;
		
	}

	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner. This method should null if there is no winner.
	 * 
	 * (For single-student groups that do not implement teams, this method should always return the name of the winning worm, or null if there is no winner)
	 */
	public String getWinner() {
		return null;
		
	}

	/**
	 * Returns all the worms in this world.
	 * 
	 * @return
	 */
	public Collection<Worm> getWorms() {
		Set<Worm> result = new HashSet<Worm>();
		for (GameObject object : objects)
			if (Worm.class.isInstance(object)) {
				Worm worm = (Worm) object;
			    result.add(worm);
			}
		return result;
	}

	/**
	 * Checks whether the given circular region of this world, defined by the 
	 * given center coordinates and radius, is passable and adjacent to impassable terrain. 
	 * 
	 * @param x 
	 *        The x-coordinate of the center of the circle to check  
	 * @param y 
	 *        The y-coordinate of the center of the circle to check
	 * @param radius 
	 *        The radius of the circle to check
	 * 
	 * @return True if the given region is passable and adjacent to impassable terrain, false otherwise.
	 */
	public boolean isAdjacent(double x, double y, double radius) {
		return true;
	}

	/**
	 * Returns whether the game in this world has finished.
	 */
	public boolean isGameFinished() {
		return this.isGameFinished;
	}
	
	/**
	 * Variable registering whether the game in this world has finished.
	 */
	private boolean isGameFinished = false;

	/**
	 * Checks whether the given circular region of this world,
	 * defined by the given center coordinates and radius, is impassable. 
	 * 
	 * @param x 
	 *        The x-coordinate of the center of the circle to check  
	 * @param y 
	 *        The y-coordinate of the center of the circle to check
	 * @param radius 
	 *        The radius of the circle to check 
	 * @return True 
	 *         if the given region is impassable, false otherwise.
	 */
	public boolean isImpassable(double x, double y, double radius) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Starts a game in this world.
	 */
	public void startGame() {
		//addNewWorm();
		
	}

	/**
	 * Starts the next turn in this world.
	 */
	public void startNextTurn() {
		// TODO Auto-generated method stub
		
	}

}
