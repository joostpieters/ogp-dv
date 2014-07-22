package worms.model.abilities;
import worms.model.*;

/**
 * An interface for the ability of moving.
 * 
 * @author Delpine Vandamme
 * 
 */
public interface JumpAbility {
	
	public boolean canJump(double timeStep);
	
	public void jump(double timeStep);
	
	public double jumpTime(double timeStep);
	
	public boolean stopJump(Position position);
	
	public Position jumpStep(double dt);
	
	public double initialVelocity();
	
	public double getForce();
}
