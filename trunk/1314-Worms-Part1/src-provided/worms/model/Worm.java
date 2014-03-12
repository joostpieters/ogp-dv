package worms.model;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of worms involving a name, position, direction, radius, 
 * mass and action points.
 * 
 * @invar  The orientation of each worm must be a valid orientation for a worm.
 *         | isValidOrientation(getOrientation())
 * 
 * @author   Delphine Vandamme
 */
public class Worm {

	/**
	 * Initialize this new worm with given name, location, orientation and radius.
	 *
	 * @param  x
	 * 		   The x-coordinate of the position of this new worm (in meter)
	 * @param  y
	 * 	       The y-coordinate of the position of this new worm (in meter)
	 * @param  orientation
	 * 		   The orientation of the new worm (in radians)
	 * @param  radius 
	 * 		   The radius of the new worm (in meter)
	 * @param  name
	 * 		   The name of the new worm
	 * @pre	   The given orientation must be a valid orientation for a worm.
     *       | isValidOrientation(orientation)
     * @post   The orientation of this new worm is equal to the given
     * 		   orientation.
     *       | new.getOrientation() == orientation       
	 */
	@Raw 
	public Worm(double x, double y, double orientation, double radius,
		String name){
		setX(x);
		setY(y);
		setOrientation(orientation);
		setRadius(radius);
		setName(name);
		setActionPoints(getMaxActionPoints());
	}

	/**
	 * Returns the x-coordinate of the current location of the worm.
	 */
	@Basic
	public double getX(){
	return this.x;
	}
	
	/**
	 * Sets the x-coordinate of the current location of the worm to the given value.
	 * 
	 * @param  x
	 *		   The new x-coordinate for this worm.
	 */
	public void setX(double x){
		this.x = x;
	}
	
	/**
	 * Variable registering the x-coordinate of the current location of the worm.
	 */
	private double x = 0;

	/**
	 * Returns the y-coordinate of the current location of the worm.
	 */
	@Basic
	public double getY(){
		return this.y;
	}

	/**
	 * Sets the y-coordinate of the current location of the worm to the given value.
	 * 
	 * @param  y
	 *		   The new y-coordinate for this worm.
	 */
	public void setY(double y){
		this.y = y;
	}
	
	/**
	 * Variable registering the y-coordinate of the current location of the worm.
	 */
	private double y = 0;
	
	/**
	 * Returns whether or not the worm can move a given number of steps.
	 */
	public boolean canMove(int nbSteps){
		return (getActionPoints() >= costMove(nbSteps));
	}
	
	/**
	 * Moves the worm by the given number of steps.
	 */
	public void move(int nbSteps){
			setX(getX() + nbSteps*getRadius()*Math.cos(orientation));
			setY(getY() + nbSteps*getRadius()*Math.sin(orientation));
			decreaseActionPoints(costMove(nbSteps));
	}
	
	/**
	 * Returns the cost in action points of moving the worm a number of steps.
	 */
	public int costMove(int nbSteps){
		int cost = (int) (Math.abs(Math.cos(getOrientation())) + 
	    		4 * Math.abs(Math.sin(getOrientation())));
	    return (cost + 1);
	}
	
	/**
	 * Returns the current orientation (=direction) of this worm (in radians).
	 */
	@Basic 
	public double getOrientation(){
		return this.orientation;
	}
	
	/**
     * Check whether the given orientation is a valid orientation for
     * any worm.
     *  
     * @param  orientation
     * 		   The orientation to check.
     * @return True if and only if the given orientation is greater than
     *         -2*Pi and less than 2*Pi.
     *       | result ==
     *       |   (contents > -2*Math.Pi) && (contents < 2*Math.PI)	
     */
    //public static boolean isValidOrientation(double orientation) {
    //    return (orientation > -2*Math.PI) && (orientation < 2*Math.PI);
    //}
	
	/**
	 * Sets the current orientation of the worm to the given value.
	 * 
	 * @param  orientation
     * 		   The new orientation for this worm.
     * @pre    The given orientation must be a valid orientation for a worm.
     *       | isValidOrientation(orientation)
     * @post   The orientation of this worm is equal to the given
     * 		   orientation.
     *       | new.getOrientation() == orientation
	 */
    @Raw
	public void setOrientation(double orientation){
    //	assert isValidOrientation(orientation);
		this.orientation = orientation;
	}

	/**
	 * Returns whether or not the worm can turn by the given angle.
	 */
	public boolean canTurn(double angle){
		return (getActionPoints() >= costTurn(angle));
	}
	
	/**
	 * Turns the worm by the given angle.
	 */
	public void turn(double angle){
		System.out.println("ap voor:" + getActionPoints());
		//System.out.println(angle);
		
		setOrientation(getOrientation() + angle);
		decreaseActionPoints(costTurn(angle));
		
		//System.out.println("costangle:" + costTurn(angle));
		System.out.println("ap na:" + getActionPoints());
		//System.out.println("orient na:" + getOrientation());
	}
	
	/**
	 * Returns the cost in action points of turning the worm by a given angle.
	 */
	public int costTurn(double angle){
		int cost = (int) Math.abs(60/(2*Math.PI/angle));
	    return (cost + 1);
	}
	
	/**
	 * Variable registering the current orientation of this worm (in radians).
	 */
	private double orientation = 0;
	
	/**
	 * Returns the radius of the worm.
	 */
	@Basic 
	public double getRadius(){
		return this.radius;
	}
	
	/**
	 * Sets the radius of the worm to the given value.
	 * 
	 * @param  radius
	 *		   The new radius for this worm.
	 */
	public void setRadius(double radius){
		this.radius = radius;
	}
	
	/**
	 * Variable registering the radius of the worm.
	 */
	private double radius;

	/**
	 * Returns the minimal radius of the worm.
	 */
	@Basic 
	public double getMinimalRadius(){
		return minimalRadius;
	}

	/**
	 * Sets the minimal radius of the worm to the given value.
	 * 
	 * @param  minimalRadius
	 *		   The new minimal radius for this worm.
	 */
	public void setMinimalRadius(double minimalRadius){
		this.minimalRadius = minimalRadius;
	}
	
	/**
	 * Variable registering the minimal radius of the worm.
	 */
	private double minimalRadius = 0.25;
	
	/**
	 * Variable registering the density of a worm that applies to all worms.
	 */
	private static final int p = 1062;
	
	/**
	 * Returns the mass of the worm.
	 */
	@Basic 
	public Double getMass(){
		return p*4/3*Math.PI*Math.pow(getRadius(), 3);
	}

	/**
	 * Returns the current number of action points of the worm.
	 */
	@Basic 
	public int getActionPoints(){
		return this.actionPoints;
	}
	
	/**
	 * Return the lowest possible value for the action points of
	 * this worm.
	 * 
	 * @return The lowest possible value for the action points of 
	 *         this worm is a non-negative value.
	 *       | result >= 0
	 */
	@Basic
	public int getMinActionPoints() {
		return 0;
	}
	
	/**
	 * Return the highest possible value for the action points 
	 * of this worm.
	 * 
	 * @return The highest possible value for the action points
	 *         of this worm is not below the lowest possible value
	 *         for the action points of this worm.
	 *       | result >= getMinActionPoints()
	 */
	@Basic 
	public int getMaxActionPoints(){
		return (int) (getMass() + 0.5);
	}
	
	/**
	 * Returns the current number of action points of the worm.
	 * 
	 * @param  actionPoints
	 *		   The new number of action points for this worm.
	 * @post    If the given number of action points are in the range established by the minimum
	 *          and maximum number of action points for this worm, the number of action points
	 *          of this worm are equal to the given number of action points.
	 *        | if ( (action points >= getMinActionPoints()) && (actionPoints <= getMaxActionPoints()) )
	 *        |   then new.getActionPoints() == actionPoints
	 * @post   If the given number of action points exceeds the maximum number of action points for	
	 * 		   this worm, the number of action points of this worm
	 * 		   are equal to the the highest possible value for the number of action points of this worm.
	 *       | if (actionPoints> getMaxActionPoints())
	 *       |   then new.getActionPoints() == getMaxActionPoints() 
	 * @post   If the given number of action points are below the minimum number of 
	 * 		   action points for this worm, the number of action points of this worm 
	 * 		   are equal to the lowest possible value for the number of action poits of this worm.
	 *        | if (actionPoints < getMinActionPoints())
	 *        |     then new.getActionPoints() = this.getMinActionPoints()     
	 */
	public void setActionPoints(int actionPoints){
		if (actionPoints > getMaxActionPoints())
			this.actionPoints = getMaxActionPoints();
		else if (actionPoints < getMinActionPoints())
			this.actionPoints = getMinActionPoints();
		else 
			this.actionPoints = actionPoints;
	}
	
	/**
	 * Decreases the current number of action points of the worm with 
	 * the given number of points.
	 */
	public void decreaseActionPoints(int actionPoints){
		setActionPoints(getActionPoints() - actionPoints);
	}
	
	/**
	 * Variable registering the current number of action points of the worm.
	 */
	private int actionPoints;
	
	/**
	 * Returns the name the worm.
	 */
	@Basic 
	public String getName(){
		return this.name;
	}
	
	/**
	 * Sets the name of the worm to the given string.
	 * 
	 * @param  name
	 *		   The new name for this worm.
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Variable registering the name of the worm.
	 */
	private String name;
	
	/**
	 * Variable registering the Earth’s standard acceleration that applies to all worms.
	 */
	private static final double G = 9.80665;

	/**
	 * Returns whether or not the worm can jump.
	 */
	//probleem met orientation: orientation moet nu tussen -2*Pi en 2*Pi liggen, wat nu nog niet het geval is? 
	public boolean canJump(){
		return (getActionPoints() > getMinActionPoints() &&
				!(getOrientation() > Math.PI && getOrientation() < 2*Math.PI));
	}
	
	public void jump(){
		if (canJump()){
			setX(getX() + getDistanceJump());
			setActionPoints(getMinActionPoints());
		}
	}
	
	public double jumpTime(){
		return getDistanceJump()/getInitialVelocityX();
	}
	
	public double[] jumpStep(double time){
		if (canJump()){
			double xdt = getX() + (getInitialVelocityX()*time);
			double ydt = getY() + (getInitialVelocityY()*time) - 0.5*G*Math.pow(time, 2);
			double[] position = {xdt, ydt};
			return position;
		}
		else{
			double[] position = {getX(), getY()};
			return position;
		}
	}
	
	public double getForce(){
		return 5*getActionPoints() + getMass()*G;
	}
	
	public double getInitialVelocity(){
		return getForce()*0.5/getMass();
	}
	
	public double getInitialVelocityX(){
		return getInitialVelocity()*Math.cos(getOrientation());
	}
	
	public double getInitialVelocityY(){
		return getInitialVelocity()*Math.sin(getOrientation());
	}
	
	public double getDistanceJump(){
		return Math.pow(getInitialVelocity(), 2)*
				Math.sin(2*getOrientation())/G;
	}
}
