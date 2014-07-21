package worms.model.projectiles;
import worms.exceptions.*;
import worms.model.*;

public class BazookaProjectile extends Projectile {

	public BazookaProjectile(Worm worm, int yield) 
	  throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException {
		super(worm, yield);	
	}

	@Override
	public double getMass() {
		return 0.3;
	}

	@Override
	public void setForce(int yield) {
		double force = (9.5-2.5)*yield/100;
		this.force = force;
	}


	@Override
	public int costInHitPoints() {
		return 80;
	}

}
