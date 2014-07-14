package worms.model;
import worms.exceptions.*;

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
			Worm worm = new Worm(world, new Position(x,y), direction, radius, name);
			return worm;
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
	}
	
	/**
	 * Returns whether the given worm is alive
	 */
	public boolean isAlive(Worm worm) {
		return worm.isAlive();
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
		return worm.getMinimalRadius();
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
		return worm.getMaxPoints();
	}

	/**
	 * Returns the current number of hit points of the given worm.
	 */
	public int getHitPoints(Worm worm) {
		return worm.getHitPoints();
	}

	/**
	 * Returns the maximum number of hit points of the given worm.
	 */
	public int getMaxHitPoints(Worm worm) {
		return worm.getMaxPoints();
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
		return worm.canFall();
	}
	
	/**
	 * Makes the given worm fall down until it rests on impassable terrain again.
	 */
	public void fall(Worm worm) {
		worm.fall();
		
	}

	/**
	 * Returns whether or not the given worm is allowed to move.
	 */
	public boolean canMove(Worm worm) {
		return worm.canMove();
	}

	/**
	 * Moves the given worm according to the rules in the assignment.
	 */
	public void move(Worm worm) {
		worm.move();		
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
	public double[] getJumpStep(Worm worm, double t) {
		double x = worm.jumpStep(t).getX();
		double y = worm.jumpStep(t).getY();
		double[] jumpStep = {x, y};
		return jumpStep;
		
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
	public double getJumpTime(Worm worm, double timeStep) 
		throws ModelException {
		  return worm.jumpTime(timeStep);
		
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
	public void jump(Worm worm, double timeStep) 
		 throws ModelException {
				try {
					worm.jump(timeStep);
				}
				catch (IllegalJumpException exc) {
					throw new ModelException("This worm cannot jump.");
				}
	}

	/**
	 * Activates the next weapon for the given worm
	 */
	public void selectNextWeapon(Worm worm) {
		worm.selectNextWeapon();
	}

	/**
	 * Makes the given worm shoot its active weapon with the given propulsion yield.
	 */
	public void shoot(Worm worm, int yield) 
	  throws ModelException {
		try {
			worm.shoot(yield);
		}
		catch (IllegalShootException exc) {
			throw new ModelException("Cannot shoot.");
		}
	}
	
	/**
	 * Returns the name of the weapon that is currently active for the given worm,
	 * or null if no weapon is active.
	 */
	public String getSelectedWeapon(Worm worm) {
		return worm.getSelectedWeapon();
	}
	
	

	/**
	 * Returns the location on the jump trajectory of the given projectile after a
	 * time t.
	 * 
	 * @return An array with two elements, with the first element being the
	 *         x-coordinate and the second element the y-coordinate
	 */
	public double[] getJumpStep(Projectile projectile, double t) {
		double x = projectile.jumpStep(t).getX();
		double y = projectile.jumpStep(t).getY();
		double[] jumpStep = {x, y};
		return jumpStep;
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
		return projectile.jumpTime(timeStep);
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
	public void jump(Projectile projectile, double timeStep)  
	   throws ModelException {
		try {
			projectile.jump(timeStep);
		}
		catch (IllegalJumpException exc) {
			throw new ModelException("This projectile cannot jump.");
		}		
	}
	
	/**
	 * Returns the radius of the given projectile.
	 */
	public double getRadius(Projectile projectile) {
		return projectile.getRadius();
	}

	/**
	 * Returns the x-coordinate of the given projectile.
	 */
	public double getX(Projectile projectile) {
		return projectile.getPosition().getX();
	}

	/**
	 * Returns the y-coordinate of the given projectile.
	 */
	public double getY(Projectile projectile) {
		return projectile.getPosition().getY();
	}

	/**
	 * Returns whether the given projectile is still alive (active).
	 */
	public boolean isActive(Projectile projectile) {
		return projectile.isAlive();
	}

	public World createWorld(double width, double height, boolean[][] passableMap, Random random) 
	   throws ModelException {
		try {
			World world = new World(width, height, passableMap, random);
			return world;
		}
		catch(IllegalDimensionException exc) {
			throw new ModelException("Not a valid dimension for this world!");
		}
	
	}

	/**
	 * Create and add a new worm to the given world.
	 * The new worm must be placed at a random adjacent location.
	 * The new worm can have an arbitrary (but valid) radius and direction.
	 * The new worm may (but isn't required to) have joined a team.
	 */
	public void addNewWorm(World world) {
		double radiusWorm = 0.6;
		Position pos = world.generateRandomAdjacentPosition(radiusWorm);
		if (pos != null)
			createWorm(world, pos.getX(), pos.getY(), Math.PI/3, radiusWorm, "Delphine");
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
		//return world.getWorms();
		return  world.getGameObjectsOfType(Worm.class);
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
	public void startGame(World world) 
	  throws ModelException {
		try {
			world.startGame();
		}
		catch(IllegalStartException exc) {
			throw new ModelException("You must add at least two worms to start this game!");
		}
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
		return world.getGameObjectsOfType(Food.class);
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
		Food food = new Food(world, new Position(x,y));
		return food;
	}

	/**
	 * Create and add a new food ration to the given world.
	 * The food must be placed at a random adjacent location.
	 * 
	 * (For single-student groups that do not implement food, this method should have no effect)
	 */
	public void addNewFood(World world) {
		Position position = world.generateRandomPosition(0.2);
		if (position != null)
			createFood(world, position.getX(), position.getY());
	}
	
	/**
	 * Returns the radius of the given food ration
	 * 
	 * (For single-student groups that do not implement food, this method may return any value)
	 */
	public double getRadius(Food food) {
		return food.getRadius();
	}

	/**
	 * Returns the x-coordinate of the given food ration
	 * 
	 * (For single-student groups that do not implement food, this method may return any value)
	 */
	public double getX(Food food) {
		return food.getPosition().getX();
	}
	
	/**
	 * Returns the y-coordinate of the given food ration
	 * 
	 * (For single-student groups that do not implement food, this method may return any value)
	 */
	public double getY(Food food) {
		return food.getPosition().getY();
	}
	
	/**
	 * Returns whether or not the given food ration is alive (active), i.e., not eaten.
	 * 
	 * (For single-student groups that do not implement food, this method should always return false)
	 */
	public boolean isActive(Food food) {
		return food.isAlive();
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
	 * Create and add an empty team with the given name to the given world.
	 * 
	 * (For single-student groups that do not implement teams, this method should have no effect)
	 */
	public void addEmptyTeam(World world, String newName) {		
	}

}
