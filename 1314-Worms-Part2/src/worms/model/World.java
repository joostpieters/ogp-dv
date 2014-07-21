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
		if (passableMap != null)
			this.map = passableMap;
	}
	
	/**
	 * 
	 */
	private boolean[][] map;
	
	/**
	 * Variable registering the random number generator for this world.
	 */
	private Random random;
	
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
	 * Returns the width of this world.
	 * 
	 * @return The width of this world.
	 */
	@Basic @Raw
	public double getWidth() {
		return this.width;
	}

	/**
	 * Returns the height of this world.
	 * 
	 * @return The height of this world.
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
	public static final double maxDimension = Double.MAX_VALUE;
	
	/**
	 * 
	 * @return
	 */
	public double getPixelWidth() {
		return getWidth()/map[0].length;
	}

	/**
	 * 
	 * @return
	 */
	public double getPixelHeight() {
		return getHeight()/map.length;
	}
	
	/**
	 * Variable registering the Earth’s standard acceleration that applies to all worms.
	 */
	public static final double ACCELERATION = 9.80665;

	
	/**
	 * Returns a random position in this world for a game object with given radius.
	 * @return
	 */
	public Position getRandomPosition(double radius) 
	  throws IllegalPositionException {
		double x = radius + (width-2*radius) * random.nextDouble();
		double y = radius + (height-2*radius) * random.nextDouble();
		Position position = new Position(x,y);
		if ( !isInWorld(new Position(x,y), radius) )
			throw new IllegalPositionException(position);
		return position;
	}
	
	
	/**
	 * 
	 * @param radius
	 * @return
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
	 *         the radius of the circular domain to be checked.
	 * @return 
	 */
	public Position getRandomAdjacentPosition(double radius) {
		Position position  = getRandomPerimeterPosition(radius);
	    Position center = new Position(width/2, height/2);
	    double maxDistance = position.getDistanceTo(center);
	    double step = Math.min(getPixelWidth(), getPixelHeight());
	    double stepX = step * (width/2 - position.getX()) / (maxDistance);
	    double stepY = step * (height/2 - position.getY()) / (maxDistance);
	    for (double distance = 0;  Util.fuzzyLessThanOrEqualTo(distance, maxDistance); distance += step) {
	    	position = new Position( position.addToX(stepX).getX(), position.addToY(stepY).getY() );
	    	if (isAdjacent(position, radius))
	    		return position;
	    }
		return null;
	}
	
	/**
	 * 
	 * @param object
	 * @param maxDistance
	 * @return
	 * @throws IllegalPositionException
	 */
	public Position getAdjacentorPassablePositionTo(MoveableGameObject object, double maxDistance) 
	  throws IllegalPositionException {
		double radius = object.getRadius(); 
		double direction = object.getDirection(); 
		Position currentPos = new Position(object.getPosition());
		
		double step = Math.min(getPixelWidth(), getPixelHeight());
		Position passablePos = null;
		for (double distance = maxDistance; Util.fuzzyGreaterThanOrEqualTo(distance, 0.1); distance -= step ) {
			Position tempPos = new Position(currentPos.addToX(distance*Math.cos(direction)).getX(), currentPos.addToY(distance*Math.sin(direction)).getY());
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
	 * @param x 
	 *        The x-coordinate of the center of the circle to check  
	 * @param y 
	 *        The y-coordinate of the center of the circle to check
	 * @param radius 
	 *        The radius of the circle to check
	 * 
	 * @return True if the given region is passable and adjacent to impassable terrain, false otherwise.
	 */
	public boolean isAdjacent(Position position, double radius) {
		return (isInWorld(position, radius) && !isImpassable(position, radius) && 
				isInWorld(position,1.1*radius)) && isImpassable(position, 1.1*radius);
	}
	
	/**
	 * Checks whether the given circular region, defined by the
	 * given center coordinates and radius, is located in this world.
	 * 
	 * @param  x
	 * 		   The x-coordinate of the center of the circle to check  
	 * @param  y
	 * 		   The y-coordinate of the center of the circle to check  
	 * @param  radius
	 *         The radius of the circle to check
	 * @return 
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
	public boolean isImpassable(Position position, double radius) 
	  throws IllegalDimensionException {
		if (! isInWorld(position, radius) )
			return true;
		boolean passable = true;
		double p1 = position.getY() - radius; double p2 = position.getY() + radius;
	    double p3 = position.getX() - radius; double p4 = position.getX() + radius;
		int row1 = map.length - ((int) Math.ceil(p1/getPixelHeight()));
		int row2 = map.length - ((int) Math.ceil(p2/getPixelHeight()));
		int col1 = ((int) Math.ceil(p3/getPixelWidth())) - 1;
		int col2 = ((int) Math.ceil(p4/getPixelWidth())) - 1;
		for (int r = row2; r <= row1; r++) {//controleer hier op geldigheid! 0<=col<map[0].length en 0<=row<map.length
			for (int c = col1; c <= col2; c++) {
				passable = map[r][c];
				if (! passable)
					return !passable;
			}
		}
		return !passable;
	}
	
	/**
	 * Check whether this world has the given game object as one
	 * of the game objects attached to it.
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
			//object.setWorld(null);
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
	 * @invar for each object in objects:
	 *          canHaveAsGameObject(object)
	 * @invar for each object in objects:
	 * 		     ( (object == null) || canHaveAsGameObject(object) )
	 */
	private final List<GameObject> objects = new ArrayList<GameObject>();
	
	/**
	 * Returns all the game objects of the given type in this world.
	 * 
	 * @param    type
	 *           The type of gameObject of the objects to be added to the list.
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
	 * 
	 * @param gameObject
	 * @return
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
	 * 
	 * @param <T>
	 * @param position
	 * @param radius
	 * @return
	*/
	public <T extends GameObject> boolean overlapWithObjectOfType(Class<T> type, Position position, double radius) {
		List<T> overlappingObjects = getOverlappingObjectsOfType(type, position, radius);
		return  !overlappingObjects.isEmpty();
	}
	
	/**
	 * Starts a game in this world.
	 */
	public void startGame() throws IllegalStartException {
		if ( getGameObjectsOfType(Worm.class).size() < 2) 
			throw new IllegalStartException();
		Worm firstWorm = getGameObjectsOfType(Worm.class).get(0);
		firstWorm.setToActive(true);
		this.hasStarted = true;
	}
	
	/**
	 * Checks whether the game has started.
	 * 
	 * @return True if and only If the game has started.
	 */
	public boolean hasStarted() {
		return this.hasStarted;
	}

	/**
	 * Variable registering whether this game has started.
	 */
	private boolean hasStarted = false;
	
	/**
	 * Starts the next turn in this world.
	 */
	public void startNextTurn() {
		if (!isGameFinished()) {
			Character activeCharacter = getCurrentPlayer();
			activeCharacter.setToActive(false);
			List<Character> characters = getGameObjectsOfType(Character.class);
			int index = characters.indexOf(activeCharacter) + 1;
			if( index == characters.size() )
				index  = 0;
			Character nextCharacter = characters.get(index);
			nextCharacter.setToActive(true);
		}
	}
	
	/**
	 * 
	 * @return
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
	public Projectile getActiveProjectile() {
		return activeProjectile;
	}
	
	private Projectile activeProjectile;

	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner. This method should null if there is no winner.
	 * 
	 * (For single-student groups that do not implement teams, this method should always return the name of the winning worm, or null if there is no winner)
	 */
	public String getWinner() {
		String winner;
		if (isGameFinished())
			winner = getGameObjectsOfType(Worm.class).get(0).getName();
		else 
			winner = null;
		return winner;
	}
	
	/**
	 * Returns whether the game in this world has finished.
	 */
	public boolean isGameFinished() {
		if ( getGameObjectsOfType(Worm.class).size() == 1 || getCurrentPlayer() == null) 
			this.gameFinished = true;
		else 
			this.gameFinished = false;
		return this.gameFinished;
	}
	
	private boolean gameFinished = false;

	/**
	 * Terminate this world.
	 * 
	 * @post   new.isTerminated()
	 * @effect for each object in getAllGameObjects():
	 *           if (object.isAlive())
	 *             then this.removeAsGameObject(object)
	 */
	public void terminate() {
		for (GameObject object: objects) 
			if (object.isAlive()) {
				object.setWorld(null);
				 removeAsGameObject(object);
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
	 * ALTERNATIVE: Returns a random adjacent position in this world for a game object with given radius.
	 *
	 * @param  radius
	 *         the radius of the circular domain to be checked.
	 * @return 
	
	public Position generateRandomAdjacentPosition(double radius) {
		double direction = this.random.nextDouble() * (Math.PI * 2);
		Position position = new Position(getWidth()/2, getHeight()/2);
		double step = Math.min(getPixelWidth(), getPixelHeight());
		while (!isAdjacent(position.getX(),position.getY(), radius)) {
			position = new Position( position.addToX((step * Math.cos(direction))).getX(), 
					                 position.addToY((step * Math.sin(direction))).getY() );
			if (! isInWorld(position.getX(), position.getY(), radius))
				return null;
		}
		return position;
	}
	*/

	
	/**
	 * Return the number of game objects attached to this world.
	 
	@Basic @Raw
	public int getNbGameObjects() {
		int nbObjectsSoFar = 0;
		// @invar nbObjectsSoFar == 
		//          card({{ object in GameObject |
		//                    for some I in 0..pos-1: object == objects.get(i) }})                             
		for (int pos = 0; pos < objects.size(); pos++) {
			GameObject currentObject = objects.get(pos);
			if ( (currentObject != null) && (objects.indexOf(currentObject) == pos) )
				nbObjectsSoFar++;
		}
		return nbObjectsSoFar;
	}
	*/
	
	/**
	 * Check whether this world has the given game object as one
	 * of the game objects attached to it.
	 * 
	 * @param object
	 * 		  The game object to check.
	 
	@Basic @Raw
	public boolean hasAsGameObject(GameObject object) {
		return this.objects.contains(object);
	}
	*/
	/**
	 * Return the game object attached to this world at the given index.
	 * 
	 * @param  index
	 *         The index of the game object to be returned.
	 * @throws IndexOutOfBoundsException
	 *         (index < 1) || (index > getNbGameObjects())
	 
	@Basic @Raw
	public GameObject getGameObjectAt(int index)
	  throws IndexOutOfBoundsException {
		return objects.get(getInternalIndexOfGameObjectAt(index));
	}
	
	*/
	/**
	 * Check whether this world can have the given game object as one
	 * of its game objects.
	 * 
	 * @param  object
	 * 		   The game object to check.
	 * @return if (object == null)
	 * 			 then result == false
	 * 	       else if ( this.isTerminated() || !object.isAlive() )
	 *           then result == false
	 
	@Raw
	public boolean canHaveAsGameObject(GameObject object) {
		return ( (object != null) && (! this.isTerminated()) 
				 && (object.isAlive()) ) ;
	}
	*/
	
	/**
	 * Check whether this game object can have the given game object 
	 * as one of its game objects at the given index.
	 * 
	 * @param  object
	 *         The game object to check.
	 * @param  index
	 *         The index to check.
	 * @return if (! canHaveAsGameObect(object))
	 *           then result == false
	 *         else if ( (index < 1) || (index > getNbGameObjects()+1) )
	 *           then result == false
	 *         else result == 
	 *           for each I in 1..getNbGameObjects():
	 *             ( (I == index) || (getGameObjectAt(I) != object) )
	
	@Raw
	public boolean canHaveAsGameObjectAt(GameObject object, int index) {
		if (! canHaveAsGameObject(object))
			return false;
		if ( (index < 1) || (index > getNbGameObjects()+1) )
			return false;
		for (int i=1; i<=getNbGameObjects(); i++)
			if ( (i != index) && (getGameObjectAt(i) == object) )
				return false;
		return true;
	}
	*/ 
	
	/**
	 * Check whether this world has proper game objects attached to it.
	 * 
	 * @return result == for each I in 1..getNbGameObjects():
	 * 					    canHaveAsGameObjectAt(getGameObjectAt(I),I)
	 
	@Raw
	public boolean hasProperGameObjects() {
		for (int i=1; i<=getNbGameObjects(); i++)
			if (! canHaveAsGameObjectAt(getGameObjectAt(i), i))
				return false;
		return true;
	}
	*/
	
	/**
	 * Return the index at which the game object with the given ranking is registered 
	 * for the first time in the internal list of game objects.
	 * 
	 * @param  ranking
	 *         The ranking of the game object to search.
	 * @return if (ranking <= 0)
	 *           then result == -1
	 * @return if (ranking > 0) 
	 *           then objects.get(result) != null
	 * @return if (ranking > 0) then
	 *           for each I in 0..result-1:
	 *             (objects.get(i) != objects.get(result))
	 * @return if (ranking > 0) then
	 *           ranking == card ({{ object in GameObject |
	 *                                  for some I in 0..result:
	 *                                    (object == objects.get(i)) }})
	 * @throws IndexOutOfBoundsException
	 *         ranking >= getNbGameObjects()
	
	private int getInternalIndexOfGameObjectAt(int ranking)
	  throws IndexOutOfBoundsException {
		int nbObjectsSoFar = 0;
		int pos = 0;
		// @invar nbOfObjectsSoFar counts the number of different and effective 
		//        game objects registered at positions before pos.
		//      | nbObjectsSoFar == card ({{ object in GameObject |
		//                             for some I in 0..pos-1:
		//                                    (object == objects.get(i)) }})          
		while (nbObjectsSoFar < ranking) {
			GameObject currentObject = objects.get(pos);
			if ( (currentObject != null) && (objects.indexOf(currentObject) == pos) )
				nbObjectsSoFar++;
			pos++;
		}
		return (pos - 1);
	}
	 */
	
	/**
	 * Return a list of all game objects attached to this world.
	 * 
	 * @return result.size() == getNbGameObjects()
	 * @return for each I in 0..getNbGameObjects()-1:
	 *           (result.get(I) == getGameObjectAt(I+1))
	 
	public List<GameObject> getAllGameObjects() {
		LinkedList<GameObject> result = new LinkedList<GameObject>();
		for (int i=objects.size(); i<=0; i--)
			if (! result.contains(objects.get(i)))
				result.addFirst(objects.get(i));
		return result;
 	}
	*/
	
	/**
	 * Return a list of all game objects attached to this world.
	 * 
	 * @return result.size() == getNbGameObjects()
	 * @return for each I in 0..getNbGameObjects()-1:
	 *           (result.get(I) == getGameObjectAt(I+1))
	 
	public List<GameObject> getAllGameObjects() {
		List<GameObject> result = new  ArrayList<GameObject>();
		for (GameObject object: objects)
				result.add(object);
		return result;
		
		//List<Weapon> result = new ArrayList<Weapon>();
		//for (Weapon weapon : weapons)
		//		result.add(weapon);
 	}
	 */
	
	/**
	 * Add the given game object to this world.
	 * 
	 * @param  object
	 *         The game object to be added.
	 * @post   new.getGameObjectAt(getNbGameObjects()+1) == object
	 * @post   new.getgetNbGameObjects() == getNbGameObjects() + 1
	 * @throws IllegalArgumentException
	 *         ! canHaveAsGameObjectAt(object, getNbGameObjects()+1)
	 
	public void addAsGameObject(GameObject object) 
	  throws IllegalArgumentException {
		if (! canHaveAsGameObject(object))
			throw new IllegalArgumentException();
		if ( (! objects.isEmpty()) && (objects.get(objects.size() - 1) == null) )
			objects.set(objects.size()-1, object);
		else 
			objects.add(object);
	}
	*/
	
	/**
	 * 

 
 the given game object attached to this world.
	 * 
	 * @param  object
	 *         The game object to be removed from this world.
	 * @effect if (hasAsGameObject(object)) then removeGameObjectAt(getIndexOfGameObject(object))
	
	public void removeAsGameObject(GameObject object) {
		int firstIndexOfObject = objects.indexOf(object);
		while (firstIndexOfObject != -1) {
			objects.set(firstIndexOfObject, null);
			firstIndexOfObject = objects.indexOf(object);
		}
	}
	 */
	
	/**
	 * Add the given game object as a game object for this world at the given index.
	 * 
	 * @param  object
	 *         The game object to be added.
	 * @param  index
	 *         The index of the game object to be added.
	 * @post   new.getGameObjectAt(index) == object
	 * @post   new.getNbGameObjects() == getNbGameobjects + 1
	 * @post   for each I in index..getNbGameObjects():
	 *           (new.getGameObjectAt(I+1) == getGameObjectAt(I))
	 * @throws IllegalArgumentException
	 *         ! canHaveAsGameObjectAt(object, index)
	 
	public void addGameObjectAt(GameObject object, int index)
	  throws IllegalArgumentException {
		if (! canHaveAsGameObjectAt(object, index))
			throw new IllegalArgumentException("Invalid game object");
		int indexOfNewObject = getInternalIndexOfGameObjectAt(index-1)+1;
	    if (indexOfNewObject < objects.size()) {
	    	GameObject oldObject = objects.get(indexOfNewObject);
	    	if ( (oldObject == null) || (objects.indexOf(oldObject) < indexOfNewObject) )
	    		objects.set(indexOfNewObject, object);
	    	else
	    		objects.add(indexOfNewObject,object);
	    }
	    else
	    	objects.add(object);
	}
	*/
	
	/**
	 * Remove the game object for this world at the given index.
	 * 
	 * @param  index
	 *         The index of the game object to be removed.
	 * @post   ! new.hasAsGameObject(getGameObjectAt(index))
	 * @post   new.getNbGameObjects() ==  this.getNbGameObjects - 1
	 * @post   for each I in index+1..getNbGameObjects():
	 *            (new.getGameObjectAt(I-1) == this.getGameObjectAt(I))
	 * @throws IndexOutOfBoundsException
	 *         (index < 1) || (index > getNbGameObjects())
	 
	public void removeGameObjectAt(int index) 
	  throws IndexOutOfBoundsException {
		int indexOfObject = getInternalIndexOfGameObjectAt(index);
		GameObject objectToRemove = objects.get(indexOfObject);
		for (int pos=indexOfObject; pos<objects.size(); pos++)
			if (objects.get(pos) == objectToRemove)
				objects.set(pos, null);
	}
	 */
	
	/**
	 * Returns all the worms in this world.
	 * 
	 * @return
	 
	public List<Worm> getWorms() {
		List<Worm> result = new ArrayList<Worm>();
		for (GameObject object : objects) 
			if (Worm.class.isInstance(object)) {
				Worm worm = (Worm) object;
				if(worm.isAlive())
					result.add(worm);
			}				
		return result;
	}
	
	public Collection<Food> getFood() {
		List<Food> result = new ArrayList<Food>();
		for (GameObject object : objects) 
			if (Food.class.isInstance(object)) {
				Food food = (Food) object;
				if(food.isAlive())
					result.add(food);
			}				
		return result;
	}
	*/
	
	/**
	 * 
	 * @param gameObject
	 * @return
	 
	public List<GameObject> getOverlappingObjectsWith(Position position, double radius) {
		List<GameObject> result = new ArrayList<GameObject>();
		List<GameObject> objects = getAllGameObjects();
		for (GameObject object: objects){
			if ((position.getDistanceTo(object.getPosition())) < (radius + object.getRadius()))
				result.add(object);
		}
		return result;
	}
	*/
	/**
	 * 
	 * @param object
	 * @return
	 
	public List<Worm> getOverlappingWormsWith(Position position, double radius) {
		List<Worm> result = new ArrayList<Worm>();
		List <GameObject> overlappingObjects = getOverlappingObjectsWith(position, radius);
		for (GameObject overlappingObject: overlappingObjects) {
			if (Worm.class.isInstance(overlappingObject)) {
				Worm worm = (Worm) overlappingObject;
				result.add(worm);
			}		
		}
		return result;
	}
*/
	/**
	 * 
	 * @param position
	 * @param radius
	 * @return
	 
	public boolean overlapWithWorm(Position position, double radius) {
		List<Worm> overlappingWorms = getOverlappingWormsWith(position, radius);
		return  !overlappingWorms.isEmpty();
	}
	*/
	
}
