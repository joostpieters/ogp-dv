package worms.model.projectiles;
import worms.model.*;

/**
 * A class of Bazooka projectiles involving a worm and yield.
 * 
 * @author Delphine
 *
 */
public class BazookaProjectile extends Projectile {
	
	/**
	 * Initialize this new bazooka projectile with given worm and yield.
	 *
	 * @param  worm
	 *         The worm that shoots this new projectile.
	 * @param  yield
	 *         The yield to shoot this new projectile with.
	 * @effect super(worm, yield)
	 */
	public BazookaProjectile(Worm worm, int yield) {
		super(worm, yield);	
	}

	/**
	 * Returns the mass of this projectile.
	 * 
	 * @return result == 0.3
	 */
	@Override
	public double getMass() {
		return 0.3;
	}

	/**
	 * Sets the force exerted on the projectile using the given yield.
	 * 
	 * @param yield
	 *        The yield to shoot the projectile with.
	 * @post  new.getForce() =  (9.5-2.5)*yield/100
	 */
	@Override
	public void setForce(int yield) {
		double force = (9.5-2.5)*yield / 100;
		this.force = force;
	}

	/**
	 * Returns the cost in hit points of this projectile.
	 * 
	 * @return result == 80
	 */
	@Override
	public int costInHitPoints() {
		return 80;
	}

}
