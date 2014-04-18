package worms.model;
import worms.exceptions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * A class Facade which implements class IFacade
 * 
 * @author   Delphine Vandamme
 */
public class Facade implements IFacade {
	
	/**
	 * Create a new worm that is positioned at the given location,
	 * looks in the given direction, has the given radius and the given name.
	 * 
	 * @param world
	 * The world in which to place the created worm  
	 * @param x
	 * The x-coordinate of the position of the new worm (in meter)
	 * @param y
	 * The y-coordinate of the position of the new worm (in meter)
	 * @param direction
	 * The direction of the new worm (in radians)
	 * @param radius 
	 * The radius of the new worm (in meter)
	 * @param name
	 * The name of the new worm
	 * @throws ModelException
	 */
	public Worm createWorm(World world, double x, double y, double direction, double radius,
			String name)
	   throws ModelException {
		try {
			worm = new Worm(world, x, y, direction, radius, name);
		}
		catch(IllegalPositionException exc) {
			throw new ModelException("Not a valid position!");
		}
		catch(IllegalDirectionException exc) {
			throw new ModelException("Not a valid direction!");
		}
		catch(IllegalRadiusException exc) {
			throw new ModelException("Not a valid radius!");
		}
		catch(IllegalNameException exc) {
			throw new ModelException("Not a valid name!");
		}
		return worm;
	}
	
	/**
	 * Returns whether or not the given worm can move a given number of steps.
	 */
	public boolean canMove(Worm worm, int nbSteps) {
		return worm.canMove(nbSteps);
	}

	/**
	 * Moves the given worm by the given number of steps.
	 */
	public void move(Worm worm, int nbSteps) {
		worm.move(nbSteps);	
		
	}

	/**
	 * Returns whether or not the given worm can turn by the given angle.
	 */
	public boolean canTurn(Worm worm, double angle) {
		return worm.canTurn(angle);
	}

	/**
	 * Turns the given worm by the given angle.
	 */
	public void turn(Worm worm, double angle) {
		worm.turn(angle);		
	}

	/**
	 * Makes the given worm jump.
	 * 
	 * @throws ModelException
	 */
	public void jump(Worm worm) 
	   throws ModelException {
		try {
			worm.jump();
		}
		catch (IllegalJumpException exc) {
			throw new ModelException("This worm cannot jump.");
		}
	}

	/**
	 * Returns the total amount of time (in seconds) that a
	 * jump of the given worm would take.
	 * 
	 * @throws ModelException
	 */
	public double getJumpTime(Worm worm) 
	   throws ModelException {
		try {
		return worm.jumpTime();
		}
		catch(ArithmeticException exc) {
			throw new ModelException("Cannot divide by zero");
		}
	}

	/**
	 * Returns the location on the jump trajectory of the given worm
	 * after a time t.
	 *  
	 * @return An array with two elements,
	 *  	   with the first element being the x-coordinate and
	 *         the second element the y-coordinate
	 * @throws ModelException
	 */
	public double[] getJumpStep(Worm worm, double t) 
       throws ModelException {
		try {
			return worm.jumpStep(t);
		}
		catch (IllegalJumpException exc) {
			throw new ModelException("This worm cannot jump.");
		}
	}
	
	/**
	 * Returns the x-coordinate of the current location of the given worm.
	 */
	public double getX(Worm worm) {
		return worm.getPosition().getX();
	}
	
	/**
	 * Returns the y-coordinate of the current location of the given worm.
	 */
	public double getY(Worm worm) {
		return worm.getPosition().getY();
	}
	
	/**
	 * Returns the current orientation of the given worm (in radians).
	 */
	public double getOrientation(Worm worm) {
		return worm.getDirection();
	}

	/**
	 * Returns the radius of the given worm.
	 */
	public double getRadius(Worm worm) {
		return worm.getRadius();
	}

	/**
	 * Sets the radius of the given worm to the given value.
	 * 
	 * @throws ModelException
	 */
	public void setRadius(Worm worm, double newRadius) 
	   throws ModelException {
		try {
			worm.setRadius(newRadius);
		}
		catch(IllegalRadiusException exc){
			throw new ModelException("Not a valid radius.");
		}
		
	}

	/**
	 * Returns the minimal radius of the given worm.
	 */
	public double getMinimalRadius(Worm worm) {
		return Worm.minimalRadius;
	}

	/**
	 * Returns the current number of action points of the given worm.
	 */
	public int getActionPoints(Worm worm) {
		return worm.getActionPoints();
	}

	/**
	 * Returns the maximum number of action points of the given worm.
	 */
	public int getMaxActionPoints(Worm worm) {
		return worm.getMaxActionPoints();
	}

	/**
	 * Returns the name the given worm.
	 */
	public String getName(Worm worm) {
		return worm.getName();
	}

	/**
	 * Renames the given worm.
	 * 
	 * @throws ModelException
	 */
	public void rename(Worm worm, String newName) 
	   throws ModelException {
		try {
			worm.setName(newName);;
		}
		catch (IllegalNameException exc) {
			throw new ModelException("Invalid name.");
		}		
	}

	/**
	 * Returns the mass of the given worm.
	 */
	public double getMass(Worm worm) {
		return worm.getMass();
	}

	/**
	 * Returns whether or not the given worm can fall down
	 */
	public boolean canFall(Worm worm) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Returns whether or not the given worm is allowed to move.
	 */
	public boolean canMove(Worm worm) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Makes the given worm fall down until it rests on impassable terrain again.
	 */
	public void fall(Worm worm) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the current number of hit points of the given worm.
	 */
	public int getHitPoints(Worm worm) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns the location on the jump trajectory of the given projectile after a
	 * time t.
	 * 
	 * @return An array with two elements, with the first element being the
	 *         x-coordinate and the second element the y-coordinate
	 */
	public double[] getJumpStep(Projectile projectile, double t) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Determine the time that the given projectile can jump until it hits the terrain, hits a worm, or leaves the world.
	 * The time should be determined using the given elementary time interval.
	 * 
	 * @param projectile The projectile for which to calculate the jump time.
	 * 
	 * @param timeStep An elementary time interval during which you may assume
	 *                 that the projectile will not completely move through a piece of impassable terrain.
	 *                 
	 * @return The time duration of the projectile's jump.
	 */
	public double getJumpTime(Projectile projectile, double timeStep) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Determine the time that the given worm can jump until it hits the terrain or leaves the world.
	 * The time should be determined using the given elementary time interval.
	 * 
	 * @param worm The worm for which to calculate the jump time.
	 * 
	 * @param timeStep An elementary time interval during which you may assume
	 *                 that the worm will not completely move through a piece of impassable terrain.
	 *                 
	 * @return The time duration of the worm's jump.
	 */
	public double getJumpTime(Worm worm, double timeStep) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns the maximum number of hit points of the given worm.
	 */
	public int getMaxHitPoints(Worm worm) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns the radius of the given projectile.
	 */
	public double getRadius(Projectile projectile) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns the name of the weapon that is currently active for the given worm,
	 * or null if no weapon is active.
	 */
	public String getSelectedWeapon(Worm worm) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the x-coordinate of the given projectile.
	 */
	public double getX(Projectile projectile) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns the y-coordinate of the given projectile.
	 */
	public double getY(Projectile projectile) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns whether the given projectile is still alive (active).
	 */
	public boolean isActive(Projectile projectile) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Returns whether the given worm is alive
	 */
	public boolean isAlive(Worm worm) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Make the given projectile jump to its new location.
	 * The new location should be determined using the given elementary time interval. 
	 *  
	 * @param projectile The projectile that needs to jump
	 * 
	 * @param timeStep An elementary time interval during which you may assume
	 *                 that the projectile will not completely move through a piece of impassable terrain.
	 */
	public void jump(Projectile projectile, double timeStep) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Make the given worm jump to its new location.
	 * The new location should be determined using the given elementary time interval. 
	 *  
	 * @param worm The worm that needs to jump
	 * 
	 * @param timeStep An elementary time interval during which you may assume
	 *                 that the worm will not completely move through a piece of impassable terrain.
	 */
	public void jump(Worm worm, double timeStep) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Moves the given worm according to the rules in the assignment.
	 */
	public void move(Worm worm) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Activates the next weapon for the given worm
	 */
	public void selectNextWeapon(Worm worm) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Makes the given worm shoot its active weapon with the given propulsion yield.
	 */
	public void shoot(Worm worm, int yield) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Variable registering the worm.
	 */
	private Worm worm;
	
	public World createWorld(double width, double height, boolean[][] passableMap, Random random) 
	   throws ModelException {
		//try {
			world = new World(width, height, passableMap, random);
		//}
		
		return world;
	}
	
	/**
	 * Variable registering the world.
	 */
	private World world;

	/**
	 * Create and add a new worm to the given world.
	 * The new worm must be placed at a random adjacent location.
	 * The new worm can have an arbitrary (but valid) radius and direction.
	 * The new worm may (but isn't required to) have joined a team.
	 */
	public void addNewWorm(World world) {
		createWorm(world,0,11,0,2,"Delphine");
		world.addNewWorm();
	}

	/**
	 * Returns the active projectile in the world, or null if no active projectile exists.
	 */
	public Projectile getActiveProjectile(World world) {
		return world.getActiveProjectile();
	}

	/**
	 * Returns the active worm in the given world (i.e., the worm whose turn it is).
	 */
	public Worm getCurrentWorm(World world) {
		return world.getCurrentWorm();
	}

	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner. This method should null if there is no winner.
	 * 
	 * (For single-student groups that do not implement teams, this method should always return the name of the winning worm, or null if there is no winner)
	 */
	public String getWinner(World world) {
		return world.getWinner();
	}
	
	/**
	 * Returns all the worms in the given world
	 */
	public Collection<Worm> getWorms(World world) {
		return world.getWorms();
	}
	
	/**
	 * Checks whether the given circular region of the given world,
	 * defined by the given center coordinates and radius,
	 * is passable and adjacent to impassable terrain. 
	 * 
	 * @param world The world in which to check adjacency
	 * @param x The x-coordinate of the center of the circle to check  
	 * @param y The y-coordinate of the center of the circle to check
	 * @param radius The radius of the circle to check
	 * 
	 * @return True if the given region is passable and adjacent to impassable terrain, false otherwise.
	 */
	public boolean isAdjacent(World world, double x, double y, double radius) {
		return world.isAdjacent(x,y,radius);
	}
	
	/**
	 * Returns whether the game in the given world has finished.
	 */
	public boolean isGameFinished(World world) {
		return world.isGameFinished();
	}
	
	/**
	 * Checks whether the given circular region of the given world,
	 * defined by the given center coordinates and radius,
	 * is impassable. 
	 * 
	 * @param world The world in which to check impassability 
	 * @param x The x-coordinate of the center of the circle to check  
	 * @param y The y-coordinate of the center of the circle to check
	 * @param radius The radius of the circle to check
	 * 
	 * @return True if the given region is impassable, false otherwise.
	 */
	public boolean isImpassable(World world, double x, double y, double radius) {
		return world.isImpassable(x, y, radius);
	} 

	/**
	 * Starts a game in the given world.
	 */
	public void startGame(World world) {
		world.startGame();
		
	}

	/**
	 * Starts the next turn in the given world
	 */
	public void startNextTurn(World world) {
		world.startNextTurn();
		
	}
	
	/**
	 * Returns all the food rations in the world
	 * 
	 * (For single-student groups that do not implement food, this method must always return an empty collection)
	 */
	public Collection<Food> getFood(World world) {
		Collection<Food> food = new ArrayList<Food>();
		return food;
	}
	
	/**
	 * Create a new food ration that is positioned at the given location in the given world.
	 * 
	 * @param world
	 * The world in which to place the created food ration
	 * @param x
	 * The x-coordinate of the position of the new food ration (in meter)
	 * @param y
	 * The y-coordinate of the position of the new food ration (in meter)
	 * 
	 * (For single-student groups that do not implement food, this method should have no effect)
	 */
	public Food createFood(World world, double x, double y) {
		food = new Food();
		return food;
	}
	
	/**
	 * Variable registering the food.
	 */
	private Food food;
	
	/**
	 * Create and add an empty team with the given name to the given world.
	 * 
	 * (For single-student groups that do not implement teams, this method should have no effect)
	 */
	public void addEmptyTeam(World world, String newName) {		
	}

	/**
	 * Create and add a new food ration to the given world.
	 * The food must be placed at a random adjacent location.
	 * 
	 * (For single-student groups that do not implement food, this method should have no effect)
	 */
	public void addNewFood(World world) {
	}
	
	/**
	 * Returns the radius of the given food ration
	 * 
	 * (For single-student groups that do not implement food, this method may return any value)
	 */
	public double getRadius(Food food) {
		return 0;
	}
	

	/**
	 * Returns the name of the team of the given worm, or returns null if this
	 * worm is not part of a team.
	 * 
	 * (For single-student groups that do not implement teams, this method should always return null)
	 */
	public String getTeamName(Worm worm) {
		return null;
	}

	/**
	 * Returns the x-coordinate of the given food ration
	 * 
	 * (For single-student groups that do not implement food, this method may return any value)
	 */
	public double getX(Food food) {
		return 0;
	}
	
	/**
	 * Returns the y-coordinate of the given food ration
	 * 
	 * (For single-student groups that do not implement food, this method may return any value)
	 */
	public double getY(Food food) {
		return 0;
	}
	
	/**
	 * Returns whether or not the given food ration is alive (active), i.e., not eaten.
	 * 
	 * (For single-student groups that do not implement food, this method should always return false)
	 */
	public boolean isActive(Food food) {
		return false;
	}


}
