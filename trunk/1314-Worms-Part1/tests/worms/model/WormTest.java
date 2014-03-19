package worms.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import worms.model.Worm;
import worms.util.Util;

public class WormTest {
	
	private static Worm worm;
	
	private static final double EPS = Util.DEFAULT_EPSILON;
	
	@Before
	public void setUp() throws Exception {
		worm = new Worm(0, 0, 0, 1, "Test");
	}
	
	@Test
	public void extendedConstructor_LegalCase() throws Exception {
		Worm wor = new Worm(21, -10, 3, 2, "Bob");
		assertEquals(21, wor.getPosition().getX(), EPS);
		assertEquals(-10, wor.getPosition().getY(), EPS);
		assertEquals(3, wor.getDirection(), EPS);
		assertEquals(2, wor.getRadius(), EPS);
		assertEquals("Bob", wor.getName());
	}
	
	@Test(expected = IllegalDirectionException.class)
	public void extendedConstructor_InvalidDirection() throws Exception {
		new Worm(0,0,-2,1,"Rob");
	}
	
	@Test(expected = IllegalRadiusException.class)
	public void extendedConstructor_InvalidRadius() throws Exception {
		new Worm(399, -133, 0.4 , 0.1, "Cora");
	}
	
	@Test(expected = IllegalNameException.class)
	public final void extendedConstructor_InvalidName() throws Exception {
		new Worm(-1000, 233, 3, 4, "t");
	}
	
	@Test
	public void testGetPostition_SingleCase() {
		assertEquals(0, worm.getPosition().getX(), EPS);
		assertEquals(0, worm.getPosition().getY(), EPS);
	}
	
	@Test
	public void testSetPosition_SingleCase() {
		worm.setPosition(new Position(2,4));
		assertEquals(2, worm.getPosition().getX(), EPS);
		assertEquals(4, worm.getPosition().getY(), EPS);
	}
	
	@Test
	public void testCanMove_TrueCase() {
		assertTrue(worm.canMove(3));
	}
	
	@Test
	public void testCanMove_FalseCase() {
		worm.setActionPoints(0);
		assertFalse(worm.canMove(5));
	}
	
	@Test
	public void testMove_LegalHorizontal() throws Exception {
		worm.move(1);
		assertEquals(1, worm.getPosition().getX(), EPS);
	}
	
	@Test
	public void testMove_LegalVertical() throws Exception {
		worm.setDirection(Math.PI/2);
		worm.move(1);
		assertEquals(1, worm.getPosition().getY(), EPS);
	}
	
	@Test
	public void testCostMove() {
		worm.setDirection(1);
		assertEquals(4, worm.costMove(1));
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
		assertTrue(Worm.isValidDirection(0));
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
		worm.setDirection(Math.PI);
		assertEquals(Math.PI, worm.getDirection(), EPS);
	}
	
	@Test
	public void testCanAcceptForTurn_TrueCase() {
		assertTrue(worm.canAcceptForTurn(Math.PI));
	}
	
	@Test
	public void testCanAcceptForTurn_FalseCase() {
		assertFalse(worm.canAcceptForTurn(3*Math.PI));
	}
	
	@Test
	public void testCanTurn_TrueCase() {
		assertTrue(worm.canTurn(Math.PI));
	}
	
	@Test
	public void testCanTurn_ifNoAP() {
		worm.setActionPoints(0);
		assertFalse(worm.canTurn(Math.PI));
	}
	
	@Test
	public void testTurn_SingleCase() {
		worm.turn(Math.PI);
		assertEquals(Math.PI, worm.getDirection(), EPS);
	}
	
	@Test
	public void testCostTurn_SingleCase() {
		assertEquals(30, worm.costTurn(Math.PI));
	}
	
	@Test
	public void testGetRadius_SingleCase() {
		assertEquals(1, worm.getRadius(), EPS);
	}
	
	@Test
	public void testIsValidRadius_TrueCase() {
		assertTrue(Worm.isValidRadius(2.3));
	}
	
	@Test
	public void testIsValidRadius_isBelowMinimum() {
		assertFalse(Worm.isValidRadius(0.1));
	}
	
	@Test
	public void testSetRadius_LegalRadius() throws Exception {
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
	public void testGetActionPoints_AfterMovingThreeSteps() {
		worm.move(3);
		assertEquals(4445, worm.getActionPoints());
	}
	
	@Test
	public void testGetActionPoints_AfterJump() {
		worm.jump();
		assertEquals(0, worm.getActionPoints());
	}
	
	@Test
	public void testGetMaxActionPoints_SingleCase() {
		assertEquals(4448, worm.getMaxActionPoints());
	}
	
	@Test
	public void testSetActionPoints_ValidCase() {
		worm.setActionPoints(5);
		assertEquals(5, worm.getActionPoints());
	}
	
	@Test
	public void testSetActionPoints_AboveMax() {
		worm.setActionPoints(5000);
		assertEquals(4448, worm.getActionPoints());
	}
	
	@Test
	public void testSetActionPoints_BelowMin() {
		worm.setActionPoints(-40);
		assertEquals(0, worm.getActionPoints());
	}
	
	@Test
	public void testDecreaseActionPoints_CasePositiveAP() {
		worm.decreaseActionPoints(1120);
		assertEquals(3328, worm.getActionPoints());
	}
	
	@Test
	public void testDecreaseActionPoints_CaseNegativeAP() {
		worm.decreaseActionPoints(-1120);
		assertEquals(4448, worm.getActionPoints());
	}
	
	@Test
	public void testDecreaseActionPoints_CaseUnderMin() {
		worm.decreaseActionPoints(5000);
		assertEquals(0, worm.getActionPoints());
	}
	
	@Test
	public void testGetName_SingleCase() {
		assertEquals("Test", worm.getName());
	}
	
	@Test
	public void testIsValidName_TrueCase() {
		assertTrue(Worm.isValidName("John o'Connor"));
		assertTrue(Worm.isValidName("Pieter"));
	}
	
	@Test
	public void testIsValidName_NoCapital() {
		assertFalse(Worm.isValidName("pieter"));
		assertEquals(false, Worm.isValidName("*"));
	}
	
	@Test
	public void testIsValidName_OneCharacter() {
		assertFalse(Worm.isValidName("P"));
	}
	
	@Test
	public void testIsValidName_InvalidSign() {
		assertFalse(Worm.isValidName("*"));
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
		worm.setDirection(Math.PI/2);
		assertTrue(worm.canJump());
	}
	
	@Test
	public void testCanJump_InvalidRange() {
		worm.setDirection(3*Math.PI/2);
		assertFalse(worm.canJump());
	}
	
	@Test
	public void testCanJump_ZeroAP() {
		worm.setActionPoints(0);
		assertFalse(worm.canJump());
	}
	
	@Test
	public void testJump_LegalJump() throws Exception {
		worm.setDirection(1);
		worm.jump();
		assertEquals(5.0817, worm.getPosition().getX(), EPS);
		assertEquals(0, worm.getActionPoints());
	}
	
	@Test(expected = IllegalJumpException.class)
	public void testJump_NoAP() throws Exception {
		worm.setActionPoints(0);
		worm.jump();
	}
	
	@Test(expected = IllegalJumpException.class)
	public void testJump_InvalidDirection() throws Exception {
		worm.setDirection(3*Math.PI/2);
		worm.jump();
	}
	
	@Test
	public void testJumpTime_ValidDivision() throws Exception {
		worm.setDirection(1);
		assertEquals(1.2705, worm.jumpTime(), EPS);
	}
	
	@Test
	public void testJumpStep_LegalJumpStep() throws Exception {
		worm.setDirection(1);
		assertEquals(19.9995, worm.jumpStep(5)[0], EPS);
		assertEquals(-91.4359, worm.jumpStep(5)[1], EPS);
	}
	
	@Test(expected = IllegalJumpException.class)
	public void testJumpStep_IllegalJumpStep() throws Exception {
		worm.setActionPoints(0);
		worm.jumpStep(5);
	}
	
	@Test
	public void testForce_SingleCase() {
		worm.setDirection(1);
		assertEquals(65864.8354, worm.force(), EPS);
	}
	
	@Test
	public void testInitialVelocity_SingleCase() throws Exception {
		assertEquals(7.4030, worm.initialVelocity(), EPS);
	}
	
	@Test
	public void testInitialVelocityX_SingleCase() {
		worm.setDirection(1);
		assertEquals(3.9999, worm.initialVelocityX(), EPS);
	}
	
	@Test
	public void testInitialVelocityY_SingleCase() {
		worm.setDirection(1);
		assertEquals(6.2294, worm.initialVelocityY(), EPS);
	}
	
	@Test
	public void testDistanceJump_SingleCase() {
		worm.setDirection(1);
		assertEquals(5.0817, worm.distanceJump(), EPS);
	}
}