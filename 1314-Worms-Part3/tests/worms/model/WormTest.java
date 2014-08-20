package worms.model;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import worms.model.Worm;
import worms.model.programs.ParseOutcome;
import worms.model.programs.ParseOutcome.Success;
import worms.model.weapons.Bazooka;
import worms.model.weapons.Rifle;
import worms.util.Util;
import worms.exceptions.*;
import worms.gui.game.IActionHandler;

public class WormTest { 
	private IFacade facade;
	private static Worm worm;
	private static World world;
	private static Weapon someWeapon;
	private Random random;
	
	private static final double EPS = Util.DEFAULT_EPSILON;
	private static final boolean[][] passableMap = new boolean[][] {
			{ false, false, false, false, false}, 
			{ false, false, false, false, false}, 
			{ true,  true,  true,  true,  true },
			{ true,  true,  true,  true,  true },
			{ true,  true,  true,  true,  true } };

	@Before @SuppressWarnings("unused")
	public void setUp() throws Exception {
		facade = new Facade();
		random = new Random(7357);
		world = facade.createWorld(5.0, 5.0, passableMap, random);
		worm = facade.createWorm(world, 1.05,1.05, 0, 1, "Test", null);
		someWeapon = new Bazooka();
		worm.addAsWeapon(someWeapon);
		Worm otherWorm = facade.createWorm(world, 1,1, 0, 1, "Test1", null);
		facade.startGame(world);;
	}
	
	@Test
	public void extendedConstructor_LegalCase() throws Exception {
		Worm someWorm = new Worm( world, new Position(5,24), 3, 2, "Bob", null);
		assertEquals(5, someWorm.getPosition().getX(), EPS);
		assertEquals(24, someWorm.getPosition().getY(), EPS);
		assertEquals(3, someWorm.getDirection(), EPS);
		assertEquals(2, someWorm.getRadius(), EPS);
		assertEquals("Bob", someWorm.getName());
		assertEquals("Bazooka", worm.getAllWeapons().get(0).getName());
		assertEquals("Rifle", worm.getAllWeapons().get(1).getName());
	}
	
	@Test(expected = IllegalRadiusException.class)
	public void extendedConstructor_InvalidRadius() throws Exception {
		new Worm(world, new Position(0,0), 0.4, 0.1, "Wormpie", null);
	}
	
	@Test(expected = IllegalNameException.class)
	public final void extendedConstructor_InvalidName() throws Exception {
		new Worm(world, new Position(0,0), 0.4, 1, "nietgoed", null);
	}
	
	@Test(expected = IllegalPositionException.class)
	public final void extendedConstructor_InvalidPosition() throws Exception {
		new Worm(world, new Position(Double.NaN,0), 0.4, 1, "Wormpie", null);
	}
	
	@Test
	public void testIsValidPosition_TrueCase() throws Exception {
		new Position(Double.POSITIVE_INFINITY,-29344);
	}
	
	@Test(expected = IllegalPositionException.class)
	public void testIsValidPosition_InvalidXcoord() throws Exception {
		new Position(Double.NaN,3);
	}
	
	@Test(expected = IllegalPositionException.class)
	public void testIsValidPosition_InvalidYcoord() throws Exception {
		new Position(-39,Double.NaN);
	}
	
	@Test
	public void testGetPostition_SingleCase() {
		assertEquals(1.05, worm.getPosition().getX(), EPS);
		assertEquals(1.05, worm.getPosition().getY(), EPS);
	}
	
	@Test
	public void testSetPosition_SingleCase() {
		Worm worm = new Worm( world, new Position(5,24), 2, 2, "Bob", null);
		worm.setPosition(new Position(2,4));
		assertEquals(2, worm.getPosition().getX(), EPS);
		assertEquals(4, worm.getPosition().getY(), EPS);
	}
	
	@Test
	public void testGetDirection_SingleCase() {
		assertEquals(0, worm.getDirection(), EPS);
	}
	
	@Test
	public void testRestrictDirection() {
		assertEquals(Math.PI, Worm.restrictDirection(3*Math.PI), EPS);
		assertEquals(Math.PI, Worm.restrictDirection(-5*Math.PI), EPS);
	}
	
	@Test
	public void testIsValidDirection_TrueCase() {
		assertTrue(Worm.isValidDirection(1));
		assertEquals(false, Worm.isValidDirection(3*Math.PI));
	}
	
	@Test
	public void testIsValidDirection_ExceedsMaximum() {
		assertFalse(Worm.isValidDirection(3*Math.PI));
	}
	
	@Test
	public void testIsValidDirection_isBelowMinimum() {
		assertFalse(Worm.isValidDirection(-2*Math.PI));
	}
	
	@Test
	public void testSetDirection_SingleCase() {
		Worm worm = new Worm( world, new Position(5,24), 2, 2, "Bob", null);
		worm.setDirection(Math.PI);
		assertEquals(Math.PI, worm.getDirection(), EPS);
	}
	
	@Test
	public void testGetRadius_SingleCase() {
		assertEquals(1, worm.getRadius(), EPS);
	}
	
	@Test
	public void testGetDensity_SingleCase() {
		assertEquals(1062, worm.getDensity(), EPS);
	}
	
	@Test
	public void testGetMinimalRadius_SingleCase() {
		assertEquals(0.25, worm.getMinimalRadius(), EPS);
	}
	
	@Test
	public void testIsValidRadius_TrueCase() {
		assertTrue(worm.canHaveAsRadius(2.3));
	}
	
	@Test
	public void testIsValidRadius_isBelowMinimum() {
		assertFalse(worm.canHaveAsRadius(0.1));
	}
	
	@Test
	public void testSetRadius_LegalRadius() throws Exception {
		Worm worm = new Worm( world, new Position(5,24), 2, 2, "Bob", null);
		worm.setRadius(5);
		assertEquals(5, worm.getRadius(), EPS);
	}
	
	@Test(expected = IllegalRadiusException.class)
	public void testSetRadius_IllegalRadius() throws Exception {
		worm.setRadius(0);
	}
	
	@Test
	public void testGetMass_SingleCase() {
		assertEquals(4448.4952, worm.getMass(), EPS);
	}
	
	@Test
	public void testGetActionPoints() {
		assertEquals(4448, worm.getActionPoints());
	}
	
	@Test
	public void testGetHitPoints() {
		assertEquals(4448, worm.getHitPoints());
	}
	
	@Test
	public void testGetActionPoints_AfterMovingThreeSteps() {
		//worm.move(3);
		//assertEquals(4445, worm.getActionPoints());
	}
	
	@Test
	public void testGetMaxPoints_SingleCase() {
		assertEquals(4448, worm.getMaxPoints());
	}
	
	@Test(expected = IllegalMassException.class)
	public void testGetMaxPoints_IllegalMass() throws Exception {
		worm.setRadius(Integer.MAX_VALUE);
		worm.getMaxPoints();
	} 
	
	@Test
	public void testSetActionPoints_ValidCase() {
		Worm worm = new Worm( world, new Position(5,24), 2, 1, "Bob", null);
		worm.setActionPoints(5);
		assertEquals(5, worm.getActionPoints());
	}
	
	@Test
	public void testSetHitPoints_ValidCase() {
		Worm worm = new Worm( world, new Position(5,24), 2, 1, "Bob", null);
		worm.setHitPoints(200);
		assertEquals(200, worm.getHitPoints());
	}
	
	@Test
	public void testSetActionPoints_AboveMax() {
		Worm worm = new Worm( world, new Position(5,24), 2, 1, "Bob", null);
		worm.setActionPoints(5000);
		assertEquals(4448, worm.getActionPoints());
	}
	
	@Test
	public void testSetHitPoints_AboveMax() {
		Worm worm = new Worm( world, new Position(5,24), 2, 1, "Bob", null);
		worm.setHitPoints(4892);
		assertEquals(4448, worm.getHitPoints());
	}
	
	@Test
	public void testSetActionPoints_BelowMin() {
		Worm someWorm = new Worm( world, new Position(5,24), 3, 1, "Bob", null);
		someWorm.setActionPoints(-40);
		assertEquals(0, someWorm.getActionPoints());
	}
	
	@Test
	public void testSetHitPoints_BelowMin() {
		Worm someWorm = new Worm( world, new Position(5,24), 3, 1, "Bob", null);
		someWorm.setHitPoints(-340);
		assertEquals(0, someWorm.getHitPoints());
	}
	
	@Test
	public void testDecreaseActionPoints_CasePositiveAP() {
		worm.decreaseActionPoints(1120);
		assertEquals(3328, worm.getActionPoints());
	}
	
	@Test
	public void testDecreaseHitPoints_CasePositiveHP() {
		worm.decreaseHitPoints(1120);
		assertEquals(3328, worm.getHitPoints());
	}
	
	@Test
	public void testDecreaseActionPoints_CaseNegativeAP() {
		worm.decreaseHitPoints(-1120);
		assertEquals(4448, worm.getActionPoints());
	}
	
	@Test
	public void testDecreaseHitPoints_CaseNegativeHP() {
		worm.decreaseHitPoints(-1120);
		assertEquals(4448, worm.getHitPoints());
	}
	
	@Test
	public void testDecreaseActionPoints_CaseUnderMin() {
		Worm someWorm = new Worm( world, new Position(5,24), 3, 1, "Bob", null);
		someWorm.decreaseActionPoints(5000);
		assertEquals(0, someWorm.getActionPoints());
	}
	
	@Test
	public void testDecreaseHitPoints_CaseUnderMin() {
		Worm someWorm = new Worm( world, new Position(5,24), 3, 1, "Bob", null);
		someWorm.decreaseHitPoints(6500);
		assertEquals(0, someWorm.getHitPoints());
	}
	
	@Test
	public void testGetName_SingleCase() {
		assertEquals("Test", worm.getName());
	}
	
	@Test
	public void testIsValidName_TrueCase() {
		assertTrue(worm.isValidName("John o'Connor"));
		assertTrue(worm.isValidName("Pieter"));
	}
	
	@Test
	public void testIsValidName_NoCapital() {
		assertFalse(worm.isValidName("pieter"));
		assertFalse(worm.isValidName("*"));
	}
	
	@Test
	public void testIsValidName_OneCharacter() {
		assertFalse(worm.isValidName("P"));
	}
	
	@Test
	public void testIsValidName_InvalidSign() {
		assertFalse(worm.isValidName("*"));
	}
	
	@Test
	public void testSetName_LegalName() throws Exception {
		worm.setName("Pieter");
		assertEquals("Pieter", worm.getName());
	}
	
	@Test(expected = IllegalNameException.class)
	public void testSetName_IllegalCharacter() throws Exception {
		worm.setName("*");
	}
	
	@Test(expected = IllegalNameException.class)
	public void testSetName_TooShort() throws Exception {
		worm.setName("D");
	}
	
	@Test(expected = IllegalNameException.class)
	public void testSetName_NoCapital() throws Exception {
		worm.setName("delphine");
	}
		
	@Test
	public void testCanJump_TrueCase() {
		Worm worm = new Worm( world, new Position(1,1), 1, 0.25, "Test", null);
		assertTrue(worm.canJump(EPS));
	}
	
	@Test
	public void testCanJump_ImpassablePos() {
		Worm worm = new Worm( world, new Position(3,4), 1, 0.25, "Test", null);
		assertFalse(worm.canJump(1));
	}
	
	@Test
	public void testCanJump_ZeroAP() {
		Worm worm = new Worm( world, new Position(1,1), 1, 0.25, "Test", null);
		worm.setActionPoints(0);
		assertFalse(worm.canJump(1));
	}
	
	@Test
	public void testJump_LegalJump() throws Exception {
		Worm worm = new Worm( world, new Position(1,1), 1, 0.25, "Test", null);
		worm.jump(EPS);
		assertEquals(2.6243, worm.getPosition().getX(), EPS);
		assertEquals(2.7250, worm.getPosition().getY(), EPS);
		assertEquals(0, worm.getActionPoints());
	}
	 
	@Test(expected = IllegalJumpException.class)
	public void testJump_NoAP() throws Exception {
		Worm worm = new Worm( world, new Position(1,1), 1, 0.25, "Test", null);
		worm.setActionPoints(0);
		worm.jump(1);
	}
	
	@Test(expected = IllegalJumpException.class)
	public void testJump_ImpassablePos() throws Exception {
		Worm worm = new Worm( world, new Position(3,4), 1, 0.25, "Test", null);
		worm.jump(1);
	}
	
	@Test
	public void testJumpStep_LegalJumpStep() throws Exception {
		Worm worm = new Worm( world, new Position(1,1), 1, 0.25, "Test", null);
		assertEquals(1.0004, worm.jumpStep(EPS).getX(), EPS);
		assertEquals(1.0006, worm.jumpStep(EPS).getY(), EPS);
	} 
	
	@Test
	public void testForce_SingleCase() {
		Worm worm = new Worm( world, new Position(1,1), 1, 0.25, "Test", null);
		assertEquals(1031.6381, worm.getForce(), EPS);
	}
	
	@Test
	public void testInitialVelocity_SingleCase() throws Exception {
		Worm worm = new Worm( world, new Position(1,1), 1, 1, "Test", null);
		assertEquals(7.4030, worm.initialVelocity(), EPS);
	}
	
	@Test
	public void testCanEat_TrueCase() {
		Food food = new Food(world, new Position(0,0));
		assertTrue(worm.canEat(food));
	}
	
	@Test
	public void testCanEat_Worm() {
		Worm wormpje = new Worm( world, new Position(1,1), 1, 1, "Test", null);
		assertFalse(worm.canEat(wormpje));
	}
	
	@Test
	public void testCanEat_Mole() {
		Mole mole = new Mole( world, new Position(1,1), 1, 0.5, "Dark JacK", null);
		assertFalse(worm.canEat(mole));
	} 
	
	@Test
	public void testCanEat_Null() {
		assertFalse(worm.canEat(null));
	}
	
	@Test
	public void testGetEffectOfEating_Legal() {
		Food food = new Food(world, new Position(0,0));
		Worm worm = new Worm( world, new Position(1,1), 1, 1, "Test", null);
		assertEquals(1.1, worm.getEffectOfEating(food), EPS);
	} 
	
	@Test(expected = IllegalTypeException.class)
	public void testGetEffectOfEating_IllegalCast() throws Exception {
		Worm wormpje = new Worm( world, new Position(2,2), 1, 1, "Test", null);
		Worm worm = new Worm( world, new Position(1,1), 1, 1, "Test", null);
		worm.getEffectOfEating(wormpje);
	}
	
	@Test
	public void testEat_OverLap() { 
		Food overlappingFood =  new Food(world, new Position(1,1));
		Worm worm = new Worm( world, new Position(1,1), 1, 2, "Test", null);
		worm.eat();
		assertFalse(overlappingFood.isAlive());
		assertEquals(2.2, worm.getRadius(), EPS);
	}
	
	@Test
	public void testEat_LegalCase() {
		Worm worm = new Worm( world, new Position(1,1), 1, 2, "Test", null);
		Food food = new Food(world, new Position(0,0));
		worm.eat(food);
		assertFalse(food.isAlive());
		assertEquals(2.2, worm.getRadius(), EPS);
	}
	
	@Test
	public void testEat_Worm() {
		Worm worm = new Worm( world, new Position(1,1), 1, 2, "Test", null);
		Worm wormpje = new Worm( world, new Position(2,2), 1, 1, "Test", null);
		worm.eat(wormpje);
		assertTrue(wormpje.isAlive());
		assertEquals(2, worm.getRadius(), EPS);
	}
	
	@Test
	public void testCanMove_TrueCase() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		assertTrue(worm.canMove());
	}
	
	@Test 
	public void testCanMove_NoAP() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		worm.setActionPoints(0);
		assertFalse(worm.canMove());
	}
	
	@Test
	public void testCanMove_ToImpassablePos() {
		Worm worm = new Worm( world, new Position(4,4), 1, 0.3, "Test", null);
		assertFalse(worm.canMove());
	}
	  
	@Test
	public void testMove_LegalCase() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		Food food = new Food(world, new Position(1,1));
		int AP = worm.getActionPoints();
		worm.move();
		assertEquals(1.25, worm.getPosition().getX(), EPS);
		assertEquals(1, worm.getPosition().getY(), EPS);
		assertEquals(AP-worm.costMove(new Position(1.25,1)), worm.getActionPoints(), EPS);
		assertFalse(food.isAlive());
	}
	
	@Test
	public void testMove_LegalCase2() {
		Worm worm = facade.createWorm(world, 1, 2, 0, 1, "Test", null);
		facade.move(worm);
		assertEquals(2, facade.getX(worm), EPS);
		assertEquals(2, facade.getY(worm), EPS);
	}
	 
	@Test
	public void testCostMove_SingleCase() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		assertEquals(1, worm.costMove(new Position(1.25,1)));
	}
	
	@Test
	public void testTakeFallDamage() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		int HP = worm.getHitPoints();
		worm.takeFallDamage(1);
		assertEquals(HP-3, worm.getHitPoints());
	}
	
	@Test
	public void testTakeFallDamage_Max() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		worm.takeFallDamage(Integer.MAX_VALUE);
		assertEquals(0, worm.getHitPoints());
	}
	
	@Test
	public void testTakeFallDamage_Min() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		worm.takeFallDamage(Integer.MIN_VALUE);
		assertEquals(worm.getMaxPoints(), worm.getHitPoints());
	}
	
	@Test
	public void testCanFall_FromPassablePos() {
		Worm worm = new Worm( world, new Position(1,1), 0, 0.25, "Test", null);
		assertTrue(worm.canFall());
	}
	
	@Test
	public void testCanFall_OutOfWorld() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		assertTrue(worm.canFall());
	}
	
	@Test
	public void testCanFall_FromAdjacentPos() {
		Worm worm = new Worm( world, world.getRandomAdjacentPosition(0.5), 0, 0.5, "Test", null);
		assertTrue(world.isAdjacent(worm.getPosition(), worm.getRadius()));
		assertFalse(worm.canFall());
	}
	
	@Test
	public void testHasAsWeapon_TrueCase() {
		assertTrue(worm.hasAsWeapon(someWeapon));
	}
	
	@Test
	public void testHasAsWeapon_FalseCase() {
		Weapon someWeapon1 = new Rifle();
		assertFalse(worm.hasAsWeapon(someWeapon1));
	}

	@Test
	public void testCanHaveAsWeapon_TrueCase() {
		assertTrue(worm.canHaveAsWeapon(someWeapon));
	}

	@Test
	public void testCanHaveAsWeapon_NoEffectiveWeapon() {
		assertFalse(worm.canHaveAsWeapon(null));
	}
	
	@Test
	public void testCanHaveAsWeapon_TerminatedWorm() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		worm.terminate();
		assertFalse(worm.canHaveAsWeapon(someWeapon));
	}
	
	@Test
	public void testHasProperWeapons_TrueCase() {
		assertTrue(worm.hasProperWeapons());
	}
	
	@Test
	public void testHasProperWeapons_TerminatedWorm() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		worm.terminate();
		assertFalse(worm.hasProperWeapons());
	}
	
	@Test
	public void testHasProperWeapons_OtherWorm() {
		Worm wormpje = new Worm( world, new Position(8,7), 0, 0.25, "Test1", null);
		worm.getAllWeapons().get(0).setWorm(wormpje);
		assertFalse(worm.hasProperWeapons());
	}
	
	@Test
	public void testAddAsWeapon_LegalCase() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		Weapon someWeapon1 = new Rifle();
		worm.addAsWeapon(someWeapon1);
		assertEquals(worm, someWeapon1.getWorm());
		assertTrue(worm.getAllWeapons().contains(someWeapon1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddAsWeapon_WeaponHasWorm() throws Exception {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		worm.addAsWeapon(someWeapon);
		assertFalse(worm.getAllWeapons().contains(someWeapon));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAsWeapon_NoEffectiveWeapon() throws Exception {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		worm.addAsWeapon(null);
	}
	
	@Test
	public void testRemoveWeapon() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		Weapon someWeapon1 = new Rifle();
		worm.addAsWeapon(someWeapon1);
		assertTrue(worm.getAllWeapons().contains(someWeapon1));
		worm.removeAsWeapon(someWeapon1);
		assertFalse(worm.getAllWeapons().contains(someWeapon1));
		assertEquals(null, someWeapon1.getWorm());
	}
	
	@Test
	public void testRemoveWeapon_HasNotAsWeapon() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		worm.removeAsWeapon(someWeapon);
		assertFalse(worm.getAllWeapons().contains(someWeapon));
		assertEquals(WormTest.worm, someWeapon.getWorm());
	}
	
	@Test
	public void testSelectNextWeapon_LegalCase() {
		assertEquals(worm.selectedWeapon(), worm.getAllWeapons().get(0));
		worm.selectNextWeapon();
		assertEquals(worm.selectedWeapon(), worm.getAllWeapons().get(1));
	}
	
	@Test
	public void testSelectedWeapon_OneWeapon() {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		assertEquals(worm.selectedWeapon(), worm.getAllWeapons().get(0));
		worm.removeAsWeapon(worm.getAllWeapons().get(1));
		worm.selectNextWeapon();
		assertEquals("Bazooka", worm.getSelectedWeapon());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSelectNextWeapon_IllegalCase() throws Exception {
		Worm worm = new Worm( world, new Position(8,7), 0, 0.25, "Test", null);
		worm.removeAsWeapon(worm.getAllWeapons().get(0));
		worm.removeAsWeapon(worm.getAllWeapons().get(0));
		assertEquals(0, worm.getAllWeapons().size());
		worm.selectNextWeapon();
	}
	
	@Test
	public void testGetSelectedWeapon_OneWeapon() {
		assertEquals("Bazooka", worm.getSelectedWeapon());
	}
	
	@Test
	public void testGetSelectedWeapon_FalseCase() {
		assertFalse("Rifle" == worm.getSelectedWeapon());
	}
	
	@Test
	public void testCanShoot_TrueCase() {
		assertTrue(worm.canShoot(worm.selectedWeapon()));
	}
	
	@Test
	public void testCanShoot_NoAP() {
		Worm worm = new Worm( world, new Position(1.05,1.05), 0, 0.25, "Test", null);
		worm.setActionPoints(worm.selectedWeapon().costOfShooting()-1);
		assertFalse(worm.canShoot(worm.selectedWeapon()));
	}
	
	@Test
	public void testCanShoot_OnImpassablePos() {
		Worm worm = new Worm( world, new Position(4,4), 0, 0.25, "Test", null);
		assertTrue(world.isImpassable(worm.getPosition(), 0.25));
		assertFalse(worm.canShoot(worm.selectedWeapon()));
	}
	
	@Test
	public void testShoot_LegalCase() {
		Worm worm = new Worm( world, new Position(1.05,1.05), 0, 0.25, "Test", null);
		worm.shoot(10);
		assertEquals(20, worm.getActionPoints());
	}
	
	@Test(expected = IllegalShootException.class)
	public void testShoot_NoAP() throws Exception {
		Worm worm = new Worm( world, new Position(1.05,1.05), 0, 0.25, "Test", null);
		worm.setActionPoints(worm.selectedWeapon().costOfShooting()-1);
		worm.shoot(10);
	}
	
	@Test(expected = IllegalShootException.class)
	public void testShoot_OnImpassablePos() throws Exception {
		Worm worm = new Worm( world, new Position(4,4), 0, 0.25, "Test", null);
		worm.shoot(10);
	}
	
	@Test
	public void testProgram() {
		IActionHandler handler = new SimpleActionHandler(facade);
		World world = facade.createWorld(100.0, 100.0, new boolean[][] { {true}, {false} }, random);
		ParseOutcome<?> outcome = facade.parseProgram("double x; while (x < 1.5) do {\nx := x + 0.1;\n}\n turn x;", handler);
		assertTrue(outcome.isSuccess());
		Program program = ((Success)outcome).getResult();
		Worm worm = facade.createWorm(world, 50.0, 50.51, 0, 0.5, "Test", program);
		facade.addNewWorm(world, null); // add another worm
		double oldOrientation = facade.getOrientation(worm);
		facade.startGame(world); // this will run the program
		double newOrientation = facade.getOrientation(worm);
		assertEquals(oldOrientation + 1.5, newOrientation, EPS);
		assertNotEquals(worm, facade.getCurrentWorm(world)); // turn must end after executing program
	}
}