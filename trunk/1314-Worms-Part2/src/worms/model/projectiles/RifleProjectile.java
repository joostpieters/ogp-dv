package worms.model.projectiles;
import worms.exceptions.*;
import worms.model.*;

public class RifleProjectile extends Projectile {

	public RifleProjectile(Worm worm, int yield) 
	  throws IllegalDirectionException, IllegalPositionException, IllegalRadiusException {
		super(worm, yield);
	}

	@Override
	public double getMass() {
		return 0.01;
	}

	@Override
	public void setForce(int yield) {
		this.force = 1.5;
	}

	@Override
	public int costInHitPoints() {
		return 20;
	}
	

}
