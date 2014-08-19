package worms.model;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import worms.model.Worm;
import worms.util.Util;
import worms.exceptions.*;

public class MoleTest { 
	
	private static World world;
	private Mole mole;
	private static Worm worm;
    static final double EPS = Util.DEFAULT_EPSILON;
	private static final boolean[][] passableMap = new boolean[][] {
			{ false, false, false, false, false}, 
			{ false, false, false, false, false}, 
			{ true,  true,  true,  true,  true },
			{ true,  true,  true,  true,  true },
			{ true,  true,  true,  true,  true } };

	@Before
	public void setUp() throws Exception {
		Random random = new Random(3);
		world = new World(5, 5, passableMap, random);
		mole = new Mole( world, new Position(1,1), 1, 0.5, "Dark JacK", null);
		worm =  new Worm( world, new Position(2,2), 2, 0.3, "Bob", null);
		world.startGame();
	}
	
	@Test
	public void testCanEat_TrueCase() {
		assertTrue(mole.canEat(worm));
	}
	
	@Test
	public void testCanEat_Food() {
		Food food = new Food(world, new Position(0,0));
		assertFalse(mole.canEat(food));
	}
	
	@Test
	public void testCanEat_Mole() {
		Mole mole1 = new Mole( world, new Position(1,1), 1, 0.3, "Dark JacK", null);
		assertFalse(mole.canEat(mole1));
	}
	 
	@Test
	public void testCanEat_Null() {
		assertFalse(mole.canEat(null));
	}
	
	@Test
	public void testGetEffectOfEating_Legal() {
		assertEquals(0.8, mole.getEffectOfEating(worm), EPS);
	} 
	
	@Test(expected = IllegalTypeException.class)
	public void testGetEffectOfEating_IllegalCast() throws Exception {
		Mole mole1 = new Mole( world, new Position(1,1), 1, 0.3, "Dark JacK", null);
		mole.getEffectOfEating(mole1);
	}
	
	@Test
	public void testEat_OverLap() { 
		Worm overlappingWorm =  new Worm(world, new Position(1,1), 2, 0.3, "Bob", null);
		assertTrue(mole.isActive());
		mole.eat();
		assertFalse(mole.isActive());
		assertFalse(overlappingWorm.isAlive());
		assertEquals(0.8, mole.getRadius(), EPS);
	}
	
	@Test
	public void testEat_NoOverlap() {
		assertTrue(mole.isActive());
		mole.eat();
		assertFalse(mole.isActive());
		assertTrue(worm.isAlive());
		assertEquals(0.5, mole.getRadius(), EPS);
	}
	
	@Test
	public void testEat_Worm() {
		Mole mole1 = new Mole( world, new Position(1,1), 1, 0.3, "Dark JacK", null);
		mole.eat(mole1);
		assertTrue(mole1.isAlive());
		assertTrue(mole.isActive());
		assertEquals(0.5, mole.getRadius(), EPS);
	}
	
	
	
	@Test
	public void testCanMove_TrueCase() {
		Mole mole = new Mole( world, new Position(1,1), 0, 0.3, "Dark Jack", null);
		assertTrue(mole.canMove());
	}
	
	@Test 
	public void testCanMove_NoAP() {
		Mole mole = new Mole( world, new Position(1,1), 0, 0.3, "Dark Jack", null);
		mole.setActionPoints(0);
		assertFalse(mole.canMove());
	}
	
	@Test 
	public void testCanMove_toImpassablePos() {
		Mole mole = new Mole( world, new Position(4,4), 1, 0.3, "Dark Jack", null);
		assertTrue(mole.canMove());
	}
	
	@Test 
	public void testGetMovePosition() {
		Mole mole = new Mole( world, new Position(1,1), 1, 0.3, "Dark Jack", null);
		assertEquals(1.0540, mole.getMovePosition().getX(), EPS);
		assertEquals(1.0841, mole.getMovePosition().getY(), EPS);
	}
	
	@Test
	public void testMove_ToPassablePos() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		Mole mole = new Mole( world, new Position(1,1), 1, 0.3, "Dark Jack", null);
		assertEquals(177, mole.getActionPoints(), EPS);
		mole.move();
		assertEquals(1.0540, mole.getPosition().getX(), EPS);
		assertEquals(1.0841, mole.getPosition().getY(), EPS);
		assertEquals(174, mole.getActionPoints(), EPS);
		assertFalse(worm.isAlive());
	}
	
	@Test
	public void testMove_ToImpassablePos() {
		Mole mole = new Mole( world, new Position(4,4), 1, 0.3, "Dark Jack", null);
		assertEquals(177, mole.getActionPoints(), EPS);
		mole.move();
		assertEquals(4.0540, mole.getPosition().getX(), EPS);
		assertEquals(4.0841, mole.getPosition().getY(), EPS);
		assertEquals(174, mole.getActionPoints(), EPS);
	}
	 
	@Test
	public void testCostMove() {
		Mole mole = new Mole( world, new Position(1,1), 0, 0.25, "Dark Jack", null);
		assertEquals(3, mole.costMove(new Position(1.25,1)));
	}

	
}