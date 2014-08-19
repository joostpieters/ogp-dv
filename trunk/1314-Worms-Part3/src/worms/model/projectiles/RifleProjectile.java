package worms.model.projectiles;
import worms.model.*;
/**
 * A class of Rifle projectiles involving a worm and yield.
 * 
 * @author Delphine
 *
 */
public class RifleProjectile extends Projectile {

	/**
	 * Initialize this new rifle projectile with given worm and yield.
	 *
	 * @param  worm
	 *         The worm that shoots this new projectile.
	 * @param  yield
	 *         The yield to shoot this new projectile with.
	 * @effect super(worm, yield)
	 */
	public RifleProjectile(Worm worm, int yield) {
		super(worm, yield);
	}

	/**
	 * Returns the mass of this projectile.
	 * 
	 * @return result == 0.01
	 */
	@Override
	public double getMass() {
		return 0.01;
	}

	/**
	 * Sets the force exerted on the projectile using the given yield.
	 * 
	 * @param yield
	 *        The yield to shoot the projectile with.
	 * @post  new.getForce() =  1.5
	 */
	@Override
	public void setForce(int yield) {
		this.force = 1.5;
	}

	/**
	 * Returns the cost in hit points of this projectile.
	 * 
	 * @return result == 20
	 */
	@Override
	public int costInHitPoints() {
		return 20;
	}
	

}
