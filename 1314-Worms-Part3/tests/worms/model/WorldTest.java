package worms.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import worms.model.Worm;
import worms.model.projectiles.BazookaProjectile;
import worms.util.Util;
import worms.exceptions.*;

public class WorldTest { 
	
	private static Worm worm;
	private static World world;
	private static Random random;
	private static final double EPS = Util.DEFAULT_EPSILON;
	private static final boolean[][] passableMap = new boolean[][] {
			{ false, false, false, false, false}, 
			{ false, false, false, false, false}, 
			{ true,  true,  true,  true,  true },
			{ true,  true,  true,  true,  true },
			{ true,  true,  true,  true,  true } };

	@Before @SuppressWarnings("unused") 
	public void setUp() throws Exception {
		random = new Random(3);
		world = new World(5, 5, passableMap, random);
		worm = new Worm( world, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Test", null);
		Worm otherWorm = new Worm( world, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Test1", null);
	}
	
	@Test
	public void extendedConstructor_LegalCase() throws Exception {
		assertEquals(5, world.getWidth(), EPS);
		assertEquals(5, world.getHeight(), EPS);
	}
	
	@Test(expected = IllegalDimensionException.class)
	public void extendedConstructor_InvalidWidth() throws Exception {
		world = new World(-5, 5, passableMap, random);
	}
	
	@Test(expected = IllegalDimensionException.class)
	public void extendedConstructor_InvalidHeight() throws Exception {
		world = new World(5, -5, passableMap, random);
	}
	
	@Test(expected = IllegalDimensionException.class)
	public void extendedConstructor_NaNHeight() throws Exception {
		world = new World(5, Double.NaN, passableMap, random);
	}
	
	@Test(expected = IllegalDimensionException.class)
	public void extendedConstructor_NaNWidth() throws Exception {
		world = new World(Double.NaN, 5, passableMap, random);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void extendedConstructor_NoEffectiveMap() throws Exception {
		world = new World(5, 5, null, random);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void extendedConstructor_InvalidMap() throws Exception {
		boolean[][] passableMap = new boolean[][] { {},{} };
		world = new World(5, 5, passableMap, random);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void extendedConstructor_InvalidRandom() throws Exception {
		world = new World(5, 5, passableMap, null);
	}
	
	@Test
	public void testGetPixelWidth_SingleCase() {
		assertEquals(1, world.getPixelWidth(), EPS);
	}
	
	@Test
	public void testGetPixelHeight_SingleCase() {
		assertEquals(1, world.getPixelHeight(), EPS);
	}
	
	@Test
	public void testIsAdjacent_TrueCase() {
		assertTrue(world.isAdjacent(new Position(2.5, 2.5), 0.5));
	}
	
	@Test
	public void testIsAdjacent_NotInWorld() {
		assertFalse(world.isInWorld(new Position(8,9), 0.5));
		assertFalse(world.isAdjacent(new Position(8, 9), 0.5));
	}
	
	@Test
	public void testIsAdjacent_ImpassablePos() {
		assertTrue(world.isImpassable(new Position(4,4), 0.5));
		assertFalse(world.isAdjacent(new Position(4, 4), 0.5));
	}
	
	@Test
	public void testIsAdjacent_PassablePos() {
		assertFalse(world.isImpassable(new Position(1,1), 1.1*0.5));
		assertFalse(world.isAdjacent(new Position(1, 1), 0.5));
	}
	
	@Test
	public void testIsInWorld_TrueCase() {
		assertTrue(world.isInWorld(new Position(1,1), 0.5));
	}
	
	@Test
	public void testIsInWorld_FalseCase() {
		assertFalse(world.isInWorld(new Position(7,8), 0.5));
	}
	
	@Test
	public void testIsInWorld_PartiallyOutOfWorld() {
		assertFalse(world.isInWorld(new Position(0,0), 0.5));
	}
	
	@Test
	public void testIsInWorld_PartiallyOutOfWorld1() {
		assertFalse(world.isInWorld(new Position(5,5), 0.5));
	}
	
	@Test
	public void testIsInWorld_PartiallyOutOfWorld2() {
		assertFalse(world.isInWorld(new Position(0,5), 0.5));
	}
	
	@Test
	public void testIsInWorld_PartiallyOutOfWorld3() {
		assertFalse(world.isInWorld(new Position(5,0), 0.5));
	} 
	
	@Test
	public void testIsImpassable_TrueCase() {
		assertTrue(world.isImpassable(new Position(4,4), 0.5));
	} 
	
	@Test
	public void testIsImpassable_OutOfWorld() {
		assertTrue(world.isImpassable(new Position(8,9), 0.5));
	} 
	
	@Test
	public void testIsImpassable_PassablePos() {
		assertFalse(world.isImpassable(new Position(1,1), 0.5));
	} 
	
	@Test
	public void testHasAsGameObject_Worm() {
		assertTrue(world.getAllGameObjects().contains(worm));
	} 
	
	@Test
	public void testHasAsGameObject_Projectile() {
		Worm wormpje = new Worm( world, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		assertTrue(world.getAllGameObjects().contains(bazookaProjectile));
	} 
	
	@Test
	public void testCanHaveAsGameObject_TrueCase() {
		Worm wormpje = new Worm( world, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		assertTrue(world.canHaveAsGameObject(bazookaProjectile));
	}
	
	@Test
	public void testCanHaveAsGameObject_NoEffectiveGO() {
		assertFalse(world.canHaveAsGameObject(null));
	}
	
	@Test
	public void testHasProperGO_TrueCase() {
		assertTrue(world.hasProperGameObjects());
	}
	
	@Test
	public void testHasProperGO_OtherWorld() {
		Worm wormpje = new Worm( world, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		World newWorld = new World(5, 5, passableMap, random);
		wormpje.setWorld(newWorld);
		assertFalse(world.hasProperGameObjects());
	}
	
	@Test
	public void testAddAsGO_LegalCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		assertTrue(newWorld.getAllGameObjects().contains(wormpje));
	}
	
	@Test
	public void testAddAsGO_Projectile() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		assertTrue(newWorld.getAllGameObjects().contains(bazookaProjectile));
		assertEquals(bazookaProjectile, newWorld.getActiveProjectile());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddAsGO_HasWorld() throws IllegalArgumentException {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		world.addAsGameObject(wormpje);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAsGO_InvalidGO() throws IllegalArgumentException {
		World newWorld = new World(5, 5, passableMap, random);
		newWorld.addAsGameObject(null);
	}
	
	@Test
	public void testRemoveAsGO_LegalCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.removeAsGameObject(wormpje);
		assertFalse(newWorld.getAllGameObjects().contains(wormpje));
	}

	@Test
	public void testRemoveAsGO_Projectile() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		newWorld.removeAsGameObject(bazookaProjectile);
		assertFalse(newWorld.getAllGameObjects().contains(bazookaProjectile));
		assertEquals(null, newWorld.getActiveProjectile());
	}
	
	@Test
	public void testRemoveAsGO_InvalidGO() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		world.removeAsGameObject(wormpje);
		assertFalse(world.getAllGameObjects().contains(wormpje));
	}
	
	@Test
	public void testGetGoOfType_Worm() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		assertTrue(newWorld.getGameObjectsOfType(Worm.class).contains(wormpje));
	}
	
	@Test
	public void testGetGoOfType_Projectile() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		assertTrue(newWorld.getGameObjectsOfType(Projectile.class).contains(bazookaProjectile));
	}
	
	@Test
	public void testGetGoOfType_Mole() {
		World newWorld = new World(5, 5, passableMap, random);
		Mole mole = new Mole( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob Dob", null);
		assertTrue(newWorld.getGameObjectsOfType(Mole.class).contains(mole));
	}
	
	@Test
	public void testGetGoOfType_Food() {
		World newWorld = new World(5, 5, passableMap, random);
		Food food = new Food(newWorld, new Position(1,1));
		assertTrue(newWorld.getGameObjectsOfType(Food.class).contains(food));
	}
	
	@Test
	public void testGetGoOfType_otherCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Food food = new Food(newWorld, new Position(1,1));
		assertFalse(newWorld.getGameObjectsOfType(Worm.class).contains(food));
	}
	
	@Test
	public void testGetGoOfType_NotAlive() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		wormpje.terminate();
		assertFalse(newWorld.getGameObjectsOfType(Worm.class).contains(wormpje));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetOverlappingObjectsOfType_NoOverlap() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Food food = new Food(newWorld, new Position(1,1));
		List<Food> list = newWorld.getOverlappingObjectsOfType(Food.class, wormpje.getPosition(), wormpje.getRadius());
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testGetOverlappingObjectsOfType_Overlap() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Food food = new Food(newWorld, wormpje.getPosition());
		List<Food> list = newWorld.getOverlappingObjectsOfType(Food.class, wormpje.getPosition(), wormpje.getRadius());
		assertTrue(list.contains(food));
	}
	
	@SuppressWarnings("unused") @Test
	public void testOverlapWithObjectsOfType_TrueCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Food food = new Food(newWorld, wormpje.getPosition());
		assertTrue(newWorld.overlapWithObjectOfType(Food.class, wormpje.getPosition(), 0.5));
	}
	
	@SuppressWarnings("unused") @Test
	public void testOverlapWithObjectsOfType_FalseCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Food food = new Food(newWorld, new Position(1,1));
		assertFalse(newWorld.overlapWithObjectOfType(Food.class, wormpje.getPosition(), 0.5));
	}
	
	@Test
	public void testStartGame_LegalCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		assertTrue( wormpje.isActive() );
		assertTrue( newWorld.hasStarted() );
	}
	
	@Test
	public void testStartGame_LegalCase2() {
		World newWorld = new World(5, 5, passableMap, random);
		Mole mole = new Mole( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob Bob", null);
		newWorld.startGame();
		assertTrue( mole.isActive() );
		assertTrue( newWorld.hasStarted() );
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalStartException.class)
	public void testStartGame_IllegalCase() throws Exception {
		World newWorld = new World(5, 5, passableMap, random);
		Food food = new Food(newWorld, new Position(1,1));
		newWorld.startGame();
	}  
	
	@Test
	public void testStartNextTurn() {
		World newWorld = new World(5, 5, passableMap, random);
		Mole mole = new Mole( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob Bob", null);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		assertEquals(mole, newWorld.getCurrentPlayer() );
		newWorld.startNextTurn();
		assertEquals(wormpje, newWorld.getCurrentPlayer() );
	}
	
	
	@Test
	public void testStartNextTurn_SinglePlayer() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		assertEquals(wormpje, newWorld.getCurrentPlayer() );
		newWorld.startNextTurn();
		assertEquals(wormpje, newWorld.getCurrentPlayer() );
	}
	
	@Test
	public void testGetCurrentPlayer_LegalCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		assertEquals(wormpje, newWorld.getCurrentPlayer() );
	}
	
	@Test
	public void testGetCurrentPlayer_OtherCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Mole mole = new Mole( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob Bob", null);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		mole.terminate();
		assertEquals(wormpje, newWorld.getCurrentPlayer() );
	}
	
	@Test
	public void testGetActiveProjectile_LegalCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		newWorld.startGame();
		assertEquals(bazookaProjectile, newWorld.getActiveProjectile() );
	}
	
	@Test
	public void testGetActiveProjectile_TerminatedProjectile() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Projectile bazookaProjectile = new BazookaProjectile(wormpje, 20);
		newWorld.startGame();
		bazookaProjectile.terminate();
		assertEquals(null, newWorld.getActiveProjectile() );
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetWinner_NoWinner() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Worm worm = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob Bob", null);
		newWorld.startGame();
		assertEquals(null, newWorld.getWinner());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetWinner_Winner() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		assertEquals("Bob", newWorld.getWinner());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testIsGameFinished_TrueCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		newWorld.startGame();
		assertTrue(newWorld.isGameFinished());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testIsGameFinished_FalseCase() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob", null);
		Worm worm = new Worm( newWorld, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Bob Bob", null);
		newWorld.startGame();
		assertFalse(newWorld.isGameFinished());
	}
	
	@Test
	public void testAddRandomWorm_LegalCase() {
		World newWorld = new World(5, 5, passableMap, random);
		newWorld.addRandomWorm(null);
		assertEquals(1, newWorld.getAllGameObjects().size());
	}
	
	@Test
	public void testGetRandomAdjacentPosition_PassablePos() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, new Position(1,1), 0, 0.5, "Bob", null);
		Position pos = newWorld.getAdjacentorPassablePositionTo(wormpje, 0.5);
		assertTrue(!newWorld.isImpassable(pos, 0.5));
	}
	
	@Test
	public void testGetRandomAdjacentPosition_AdjacentPos1() {
		World newWorld = new World(5, 5, passableMap, random);
		Worm wormpje = new Worm( newWorld, new Position(2.2,2.2), 0, 0.5, "Bob", null);
		Position pos = newWorld.getAdjacentorPassablePositionTo(wormpje, 0.5);
		assertTrue(newWorld.isAdjacent(pos, 0.5));
	}
	
}