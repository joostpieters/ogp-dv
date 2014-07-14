package worms.model;
import worms.exceptions.*;

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
