package worms.model;
import java.util.*;

import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.*;
import worms.util.Util;

/**
 * A class of worlds involving a width, height, passableMap, a random number generator and with game objects.
 * 
 * @invar    The game objects attached to each world must be proper game objects for that world.
 * 		   | hasProperGameObjects()
 * @invar    The current dimensions (width, height) of each world must be proper dimensions of this world.
 *         | isValidDimension( getWidth(), getHeight() )
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
	 * @post   The width of this new world is equal to the given width.
	 *       | new.getWidth() == width
	 * @post   The height of this new world is equal to the given height.
	 *       | new.getHeight() == height
	 * @throws IllegalArgumentException
	 *       | random != null 
	 * @throws IllegalDimensionException
	 *       | ! isValidDimension(width, height)
	 * @throws IllegalArgumentException
	 *       | ! isValidPassableMap(passableMap)
	 */
	@Raw 
	public World(double width, double height, boolean[][] passableMap, Random random) 
	  throws IllegalDimensionException, IllegalArgumentException {
		if (! isValidDimension(width, height))
			throw new IllegalDimensionException();
		if (random == null)
			throw new IllegalArgumentException("Random nr generator must be effective.");
		if (! isValidPassableMap(passableMap))
			throw new IllegalArgumentException("PassableMap must be valid."); 
		
		this.width = width;
		this.height = height;
		this.random = random;
		this.map = passableMap;
	}
	
	/**
	 * Check whether the given passable map is a valid map for a world.
	 * 
	 * @param  map
	 *         The passable map to check.
	 * @return result == (map != null) && (map.length > 0) && (map[0].length > 0)
	 */
	public static boolean isValidPassableMap( boolean[][] map ) {
		return (map != null) && (map.length > 0) && (map[0].length > 0);
	}

	/**
	 * Variable registering the passable map of this world.
	 */
	private final boolean[][] map;
	
	/**
	 * Variable registering the random number generator for this world.
	 */
	private final Random random;
	
	/**
	 * Check whether the given height and width is a valid height and width for a world.
	 * 
	 * @param  width
	 *         The width to check.
	 * @param  height
	 *         The height to check.
	 * @return result == (width >= 0) && (width <= maxDimension) && (height >= 0) && (height <= maxDimension)
	 */
	public static boolean isValidDimension(double width, double height) {
		return (Util.fuzzyGreaterThanOrEqualTo(width, 0)) 
				&& (Util.fuzzyLessThanOrEqualTo(width, MAX_DIMENSION)) 
				&& (Util.fuzzyGreaterThanOrEqualTo(height, 0)) 
				&& (Util.fuzzyLessThanOrEqualTo(height, MAX_DIMENSION));
	}
	
	/**
	 * Returns the width of this world.
	 */
	@Basic @Raw
	public double getWidth() {
		return this.width;
	}

	/**
	 * Returns the height of this world.
	 */
	@Basic @Raw
	public double getHeight() {
		return this.height;
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
	public static final double MAX_DIMENSION = Double.MAX_VALUE;
	
	/**
	 * Returns the width of a pixel.
	 * 
	 * @return result == getWidth() / this.map[0].length
	 */
	public double getPixelWidth() {
		return getWidth() / this.map[0].length;
	}

	/**
	 * Returns the height of a pixel.
	 * 
	 * @return result == getHeight() / this.map.length
	 */
	public double getPixelHeight() {
		return getHeight() / this.map.length;
	}
	
	/**
	 * Variable registering the Earth’s standard acceleration that applies to all worlds.
	 */
	public static final double ACCELERATION = 9.80665;

	
	/**
	 * Returns a random position in this world for a game object with given radius.
	 * 
	 * @param  radius
	 *         The radius of the game object for which to determine a random position.
	 * @return result == new Position( radius + (width  - 2 * radius) * random.nextDouble(), 
	 *                                 radius + (height - 2 * radius) * random.nextDouble() )
	 */
	public Position getRandomPosition(double radius) {
		double x = radius + (width  - 2 * radius) * random.nextDouble();
		double y = radius + (height - 2 * radius) * random.nextDouble();
		Position position = new Position(x,y);
		if ( isInWorld(position, radius) )
			return position;
		else 
			return null;
	}
	
	
	/**
	 * Returns a random position on one of the four perimeters of this world.
	 * 
	 * @param  radius
	 *         The radius of the circular region around the position.
	 * @return result == ( new Position(getRandomPosition(radius).getX(), 0) || 
	 *                     new Position(getRandomPosition(radius).getX(), getHeight()) ||
	 *                     new Position(0, getRandomPosition(radius).getY()) || 
	 *                     new Position(getWidth, getRandomPosition(radius).getY()) )
	 */
	public Position getRandomPerimeterPosition(double radius) {
		Position randomPos = getRandomPosition(radius);
		double xStart = randomPos.getX();
		double yStart = randomPos.getY();
		int nrOfPerimeter = random.nextInt(4);
		if (nrOfPerimeter == 0) yStart = 0; if (nrOfPerimeter == 1) yStart = getHeight();
		if (nrOfPerimeter == 2) xStart = 0; if (nrOfPerimeter == 3) xStart = getWidth();
	    return new Position(xStart,yStart);
	}
	
	/**
	 * Returns a random adjacent position in this world for a game object with given radius.
	 *
	 * @param  radius
	 *         The radius of the circular domain to be checked.
	 * @return a random position adjacent to impassable terrain 
	 */
	public Position getRandomAdjacentPosition(double radius) {
		Position position  = getRandomPerimeterPosition(radius);
		Position center = new Position(getWidth() / 2, getHeight() / 2);
	    double maxDistance = position.getDistanceTo(center);
	    double step = Util.DEFAULT_EPSILON;
	    double stepX = step * (getWidth()  / 2 - position.getX()) / (maxDistance);
	    double stepY = step * (getHeight() / 2 - position.getY()) / (maxDistance);
	    for (double distance = 0;  Util.fuzzyLessThanOrEqualTo(distance, maxDistance); distance += step) {
	    	position = position.addToX(stepX).addToY(stepY);
	    	if (isAdjacent(position, radius))
	    		return position;
	    }
		return null;
	}
	
	/**
     * Returns an adjacent or passable position in a certain region around the given mobile game object.
     * 
	 * @param  object
	 *         Mobile game object which determines the region in which the position is to be found.
	 * @param  maxDistance
	 *         The maximal distance between the position of the game object and the new position.
	 * @return if ( (!isAdjacent(tempPos,radius) && (tempPosDiverged == null) && (passablePos == null) ) 
	 *            then result == null
	 * @return if ( (!isAdjacent(tempPos,radius) && (tempPosDiverged == null) && (passablePos != null) ) 
	 *            then 
	 *            	!isImpassable(result, radius) && 
	 *            	(result.getDistanceTo(currentPos) <= maxDistance) && 
	 *            	(result.getDistanceTo(currentPos) >= 0.1) 
	 * @return if ( isAdjacent(tempPos,radius) )
	 *            then 
	 *            	isAdjacent(result, radius) && 
	 *            	(result.getDistanceTo(currentPos) <= maxDistance) && 
	 *            	(result.getDistanceTo(currentPos) >= 0.1) 
	 * @return if ( (!isAdjacent(tempPos,radius)) && (tempPosDiverged != null) )
	 *            then 
	 *            	isAdjacent(result, radius) && 
	 *              (result.getDistanceTo(currentPos) <= maxDistance) && 
	 *              (result.getDistanceTo(currentPos) >= 0.1) &&
	 *              (currentPos.getSlope(result) - currentPos.getDirection() <= 0.7875)
	 *
	 */
	public Position getAdjacentorPassablePositionTo(MobileGameObject object, double maxDistance) {
		double radius = object.getRadius(); 
		double direction = object.getDirection(); 
		Position currentPos = new Position(object.getPosition());
		double step = radius / 100;
		Position passablePos = null;
		for (double distance = maxDistance; Util.fuzzyGreaterThanOrEqualTo(distance, 0.1); distance -= step ) {
			
			Position tempPos = currentPos.addToX(distance*Math.cos(direction)).addToY(distance*Math.sin(direction));
			if ( isAdjacent(tempPos, radius) )
				return tempPos;	
			
			for ( double divergence = 0; Util.fuzzyLessThanOrEqualTo(divergence, 0.7875); divergence += 0.0175 ) {
				
				Position tempPosDiverged = currentPos.rotateBy(divergence, distance, direction);
				if (isAdjacent(tempPosDiverged, radius))
					return tempPosDiverged;
				
				tempPosDiverged = currentPos.rotateBy(-divergence, distance, direction);
				if (isAdjacent(tempPosDiverged, radius))
					return tempPosDiverged;
			}
			if ( passablePos == null )
				if (! isImpassable(tempPos, radius))
					passablePos = tempPos;
		}
		return passablePos;		
	}
		
	/**
	 * Checks whether the given circular region, defined by the 
	 * given center coordinates and radius, is passable and adjacent to impassable terrain. 
	 * 
	 * @param position 
	 *        The position of the center of the circle to check.
	 * @param radius 
	 *        The radius of the circle to check
	 * @return result == (isInWorld(position, radius) && !isImpassable(position, radius) && 
				          isInWorld(position,1.1*radius)) && isImpassable(position, 1.1*radius)
	 */
	public boolean isAdjacent(Position position, double radius) {
		return (isInWorld(position, radius) && !isImpassable(position, radius) && 
				isInWorld(position,1.1*radius)) && isImpassable(position, 1.1*radius);
	}
	
	/**
	 * Checks whether the given circular region, defined by the given center coordinates and radius, is located in this world.
	 * 
	 * @param  position 
	 *         The position of the center of the circle to check.
	 * @param  radius
	 *         The radius of the circle to check
	 * @return result == ((position.getY() - radius) > 0) && ((position.getY() + radius) < getHeight()) &&
	 * 					 ((position.getX() - radius) > 0) && ((position.getX() + radius) < getWidth())
	 */
	public boolean isInWorld(Position position, double radius) {
		boolean isInWorld = true;
		double x = position.getX(); double y = position.getY();
		if ((x > getWidth()) || (x < 0) || (y > getHeight()) || (y < 0)) 
			return !isInWorld;
		double p1 = y - radius; double p2 = y + radius;
	    double p3 = x - radius; double p4 = x + radius;
	    if ((p1 <= 0) || (p2 >= getHeight()) || (p3 <= 0 ) || (p4 >= getWidth()))
	    	return !isInWorld;
	    return isInWorld;
	}
	
	/**
	 * Checks whether the given circular region of this world, defined by the given center coordinates and radius, is impassable. 
	 * 
	 * @param  position
	 *         The position of the center of the circle to check 
	 * @param  radius 
	 *         The radius of the circle to check 
	 * @return True 
	 *         if the given region is impassable, false otherwise.
	 * @throws IllegalStateException
	 *        | ( (c < 0)|| (c >= this.map[0].length) || (r < 0)|| (r >= this.map.length) )
	 */
	public boolean isImpassable(Position position, double radius) throws IllegalStateException {
		if (! isInWorld(position, radius) )
			return true;
		boolean passable = true;
		double p1 = position.getY() - radius; double p2 = position.getY() + radius;
	    double p3 = position.getX() - radius; double p4 = position.getX() + radius;
		int row1 = map.length - ((int) Math.floor(p1/getPixelHeight())) - 1;
		int row2 = map.length - ((int) Math.ceil(p2/getPixelHeight()));
		int col1 = ((int) Math.floor(p3 / getPixelWidth()));
		int col2 = ((int) Math.ceil(p4 / getPixelWidth())) - 1;
		for (int r = row2; r <= row1; r++) {
			for (int c = col1; c <= col2; c++) {
				if ( (c < 0)|| (c >= this.map[0].length) || (r < 0)|| (r >= this.map.length) )
					throw new IllegalStateException();
				passable = map[r][c]; 
				if (! map[r][c])
					return !passable;
			}
		}
		return !passable;
	}
	
	/**
	 * Check whether this world has the given game object as one of the game objects attached to it.
	 * 
	 * @param object
	 * 		  The game object to check.
	 */
	@Basic @Raw
	public boolean hasAsGameObject(GameObject object) {
		return this.objects.contains(object);
	}
	
	
	/**
	 * Check whether this world can have the given game object as one of its game objects.
	 * 
	 * @param  game object
	 *         The game object to check.
	 * @return False if the given game object is not effective.
	 *       | if (object == null) then result == false
	 *         Otherwise, true if and only if this world is not yet terminated or if the given object is terminated.
	 *       | else result = ( (this.isAlive() || object.isTerminated() )     
	 */
	@Raw
	public boolean canHaveAsGameObject(GameObject object) {
		return ( (object != null) && ( !this.isTerminated() || !object.isAlive()) );
	}
	
	/**
	 * Check whether this world has proper game objects associated with it.
	 * @return True if and only if this world can have each of its game objects as a game object attached to it, 
	 * 		   and if each of these game objects references this world as their world.
	 *       | result == for each object in GameObject : 
	 *       |             ( if (this.hasAsGameObject(object)) then 
	 *                           canHaveAsGameObject(object) && (object.getWorld() == this) )
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
	 * Add the given game object as a game object for this world.
	 * @param  object
	 *         The game object to become a game object of this world.
	 * @post   This world has the given object as one of its game objects.
	 *       | new.hasAsGameObject(object)
	 * @post   The given game object references this world as the world to which it is attached.
	 *       | (new object).getWorld() == this
	 * @throws IllegalArgumentException
	 *         This world cannot have the given object as one of its objects.
	 *       | ! canHaveAsGameObject(object)
	 * @throws IllegalArgumentException
	 * 		   The given object is already attached to some world.
	 *       | ( (object != null) && (object.getWorld() != null) )      
	 */
	public void addAsGameObject(GameObject object) 
	  throws IllegalArgumentException {
		if (! canHaveAsGameObject(object))
			throw new IllegalArgumentException();
		if (object.getWorld() != null)
			throw new IllegalArgumentException();
		this.objects.add(object);
		object.setWorld(this);
		if (object instanceof Projectile) {
			this.activeProjectile = (Projectile) object;
		}

	}
	
	/**
	 * Remove the given game object as a game object associated with this world.
	 * @param  object
	 *         The object to be removed.
	 * @post   This world does not have the given object as one of its objects.
	 *       | ! new.hasAsGameObject(object) 
	 */
	public void removeAsGameObject(GameObject object) {
		if (hasAsGameObject(object)) {
			this.objects.remove(object);
			if (object instanceof Projectile) 
				this.activeProjectile = null;
		}
	}
	
	/**
	 * Return a list of all game objects associated with this world.
	 * 
	 * @return Each object in the resulting list is is attached to this world and vice versa.
	 *       | for each object in GameObject:
	 *       |    (result.contains(object) == this.hasAsGameObject(object))
	 */
	public List<GameObject> getAllGameObjects() {
		return new ArrayList<GameObject>(this.objects);
	}
	
	/**
	 * List collecting references to game objects attached to this world.
	 * 
	 * @invar objects != null
	 * @invar for each object in objects: canHaveAsGameObject(object)
	 * @invar for each object in objects:
	 * 		     ( (object == null) || canHaveAsGameObject(object) )
	 */
	private final List<GameObject> objects = new ArrayList<GameObject>();
	
	/**
	 * Returns all the game objects of the given type in this world.
	 * 
	 * @param    type
	 *           The type of game objects to be added to the list.
	 * @return   All the game objects of the given type attached to this world.
	 * 		   | for each object in GameObject:
	 *         |    (result.contains(object) == this.hasAsGameObject(object) && type.isInstance(object))
	 */
	@SuppressWarnings("unchecked")
	public <T extends GameObject> List<T> getGameObjectsOfType(Class<T> type) {
		List<T> result = new ArrayList<T>();
		for (GameObject object : this.objects)
			if (type.isInstance(object) && object.isAlive()) 
				result.add((T) object);
		return result;
	}

	/**
	 * Returns a list with all the game objects of the given type which overlap with the region at the given 
	 * position and with given radius.
	 * 
	 * @param  type
	 * 		   The type of game objects to be added to the list.
	 * @param  position
	 *         The position of the center of the circular region to check.
	 * @param  radius
	 *         The radius of the circular region to check.
	 * @return for each object in GameObject:
	 *             ( result.contains(object) == this.hasAsGameObject(object) && type.isInstance(object)
	 *               && (position.getDistanceTo(object.getPosition())) < (radius + object.getRadius()) )
	 */
	@SuppressWarnings("unchecked")
	public <T extends GameObject> List<T> getOverlappingObjectsOfType(Class<T> type, Position position, double radius) {
		List<T> result = new ArrayList<T>();
		for (GameObject object: this.objects) {
			if (type.isInstance(object))
				if ((position.getDistanceTo(object.getPosition())) < (radius + object.getRadius()))
					result.add((T) object);
		}
		return result;
	}
	
	/**
	 * Returns whether or not there is overlap with a game object of the given type in a circular 
	 * region with given position and radius.
	 * 
	 * @param type
	 *        The type of game objects to check for overlap.
	 * @param  position
	 *         The position of the center of the circular region to check.
	 * @param  radius
	 *         The radius of the circular region to check.
	 * @return result == ! getOverlappingObjectsOfType(type, position, radius).isEmpty()
	*/
	public <T extends GameObject> boolean overlapWithObjectOfType(Class<T> type, Position position, double radius) {
		List<T> overlappingObjects = getOverlappingObjectsOfType(type, position, radius);
		return  !overlappingObjects.isEmpty();
	}
	
	/**
	 * Starts a game in this world.
	 * 
	 * @effect | setIndexOfCurrentWorm(0)
	 * @post     The game is started
	 * 		   | new.hasStarted == true
	 * @effect | startNextTurn()
	 * @throws   IllegalStartException
	 *           The world contains no characters.
	 *         | getGameObjectsOfType(Character.class).size() < 1 
	 */	
	public void startGame() throws IllegalStartException {
		if ( getGameObjectsOfType(Character.class).size() < 1 ) 
			throw new IllegalStartException();
		
		setIndexOfCurrentPlayer(0);
		startNextTurn();
		this.hasStarted = true;
	}
	
	/**
	 * Checks whether the game has started.
	 */
	@Basic
	public boolean hasStarted() {
		return this.hasStarted;
	}

	/**
	 * Variable registering whether this game has started.
	 */
	private boolean hasStarted = false;
	
	/**
	 * Starts the next turn in this world.
	 * 
	 * @effect activeCharacter.setToActive(false)
	 * @effect if (index != characters.size()) 
	 *            then characters.get( characters.indexOf(activeCharacter) + 1 ).setToActive(true)
	 * @effect if (index == characters.size()) 
	 *            then characters.get(0).setToActive(true)
	 * @effect setIndexOfCurrentPlayer(index)
	 * @effect if (nextCharacter.hasProgram()) then ( nextCharacter.getProgram().execute() )
	 * @effect if (nextCharacter.hasProgram()) then ( startNextTurn() )
	 */
	public void startNextTurn() {
		int index;
		Character activeCharacter = getCurrentPlayer();
		if (activeCharacter == null) 
			index = getIndexOfCurrentPlayer();
		else {
			activeCharacter.setToActive(false);
			index = getIndexOfCurrentPlayer() + 1;
		}
		List<Character> characters = getGameObjectsOfType(Character.class);
		if ( index == characters.size() )
			index = 0;
		Character nextCharacter = characters.get(index);
		setIndexOfCurrentPlayer(index);
		nextCharacter.setToActive(true);
		
		if (nextCharacter.hasProgram()) {
			nextCharacter.getProgram().execute();
			startNextTurn();
		}
	}

	/**
	 * returns the index of the current player.
	 */
	@Basic
	public int getIndexOfCurrentPlayer() {
		return this.IndexOfCurrentPlayer;
	}
	
	/**
	 * Sets the index of the current player to the given number.
	 * @param index
	 *        The new index of the current player.
	 * @post  new.IndexOfCurrentWorm = index
	 */
	public void setIndexOfCurrentPlayer(int index) {
		this.IndexOfCurrentPlayer = index;
	}
	
	/** 
	 * Variable registering the index of the current player in this world. 
	 */ 
	private int IndexOfCurrentPlayer; 

	
	/**
	 * Returns the current player in this world.
	 * 
	 * @return  for each character in characters :
	 *             ( characters.contains(character) == this.hasAsGameObject(character) && Character.class.isInstance(character) &&
	 *                                              character.isActive() && character.isAlive() )
	 *         if (!characters.isEmpty()) then result == characters.get(0)
	 *         else result == null
	 */
	public Character getCurrentPlayer() {
		List<Character> characters = getGameObjectsOfType(Character.class);
		for (Character character : characters)
			if ( character.isActive() && character.isAlive())
				return character;
		return null;
	}

	/**
	 * Returns the active projectile in the world, or null if no active projectile exists.
	 */
	@Basic
	public Projectile getActiveProjectile() {
		return this.activeProjectile;
	}
	
	/**
	 * Variable registering the active projectile in this world.
	 */
	private Projectile activeProjectile;

	/**
	 * Returns the name of a single worm if that worm is the winner or null if there is no winner.
	 * 
	 * @return if (isGameFinished()) then result == getGameObjectsOfType(Worm.class).get(0).getName()
	 *         else result == null
	 */
	public String getWinner() {
		String winner;
		if (isGameFinished())
			winner = getGameObjectsOfType(Character.class).get(0).getName();
		else 
			winner = null;
		return winner;
	}
	
	/**
	 * Returns whether the game in this world has finished.
	 * 
	 * @return result == (getGameObjectsOfType(Character.class).size() == 1)
	 */
	public boolean isGameFinished() {
		List<Character> characters =  getGameObjectsOfType(Character.class);
		if ( !characters.isEmpty() ) 
			return ( characters.size() == 1 );
		else 
			return false;
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
	 * Adds a worm with random position, random radius, random name and given program to this world.
	 * 
	 * @param  program
	 *         The program to be attached to the new worm.
	 * @effect addAsGameObject( new Worm(this, getRandomAdjacentPosition(randomRadius), 2 * Math.PI * random.nextDouble(),
	 *                          0.25 + random.nextDouble()/2, Worm.WORMNAMES.get(random.nextInt(Worm.WORMNAMES.size())), program) )
	 *         
	 */
	@SuppressWarnings("unused")
	public void addRandomWorm(Program program) {
		double radius =  0.25 + random.nextDouble()/2;
		double direction = 2 * Math.PI * random.nextDouble();
		String name = Worm.WORMNAMES.get(random.nextInt(Worm.WORMNAMES.size()));
		Position position = getRandomAdjacentPosition(radius);
		Worm worm = null;
		if (position != null)
			 worm = new Worm(this, position, direction, radius, name, program);
	}
}
