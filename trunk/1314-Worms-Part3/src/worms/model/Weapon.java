package worms.model;
import worms.exceptions.*;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of weapons involving a worm.
 *                  
 * @author   Delphine Vandamme 
 */
public abstract class Weapon {
	
	/**
	 * Initialize this new weapon.               
	 */
	public Weapon() {
	}
	
	/**
	 * Returns the name of the weapon.
	 */
	@Basic @Raw
	public String getName() {
		return this.name;
	}
	
	/**
	 * Variable registering the name of the weapon.
	 */
	private String name;
	
	/**
	 * Return the worm to which this weapon is attached.
	 * A null reference is returned if this weapon is not attached to any worm.
	 */
	@Basic @Raw
	public Worm getWorm() {
		return this.worm;
	}

	/**
	 * Check whether this weapon can be attached to the given worm.
	 * 
	 * @param  worm
	 * 		   The worm to check.
	 * @return result == ( (worm == null) || worm.canHaveAsWeapon(this) )
	 */
	@Raw
	public boolean canHaveAsWorm(Worm worm) {
		return ( (worm == null) || worm.canHaveAsWeapon(this) );
	}
	
	/**
	 * Check whether this weapon has a proper worm to which it is attached.
	 * 
	 * @return result == ( canHaveAsWorm(getWorm()) && ( (getWorm() == null)
	 *                     || getWorm().hasAsWeapon(this)))
	 */
	@Raw
	public boolean hasProperWorm() {
		return ( canHaveAsWorm(getWorm()) && ( (getWorm() == null) 
				 || getWorm().hasAsWeapon(this)));
	}
	
	/**
	 * Set the worm to which this weapon is attached to the given worm.
	 * 
	 * @param worm
	 * 		  The worm to attach this weapon to.
	 * @pre   if (worm != null) 
	 *          then worm.hasAsWeapon(this)
	 * @pre   if ( (worm == null) && (getWorm() != null) )
	 *          then !getWorm().hasAsWeapon(this)
	 * @post  new.getWorm() == worm
	 */
	public void setWorm(@Raw Worm worm) {
		assert ( (worm == null) || worm.hasAsWeapon(this) );
		assert ( (worm != null) || (getWorm() == null) || (! getWorm().hasAsWeapon(this)));
		this.worm = worm;
		
	}
	
	/**
	 * Variable registering the worm to which this weapon is attached.
	 */
	private Worm worm = null;
	
	/**
	 * Shoot a projectile with this weapon.
	 * 
	 * @param  yield
	 *         The yield used to shoot the weapon.
	 * @effect getProjectile(yield)
	 * @effect decreaseNbProjectilsWith(1)
	 * @throws IllegalShootException
	 *         ! canShoot()
	 */
	@SuppressWarnings("unused")
	public void shoot(int yield) 
	  throws IllegalShootException {
		if (! canShoot())
			throw new IllegalShootException();
		Projectile projectile = getProjectile(yield);
		decreaseNbProjectilesWith(1);
	}

	/**
	 * Returns whether or not this weapon can shoot, depending on the ammunition left.
	 * 
	 * @return result == (getNbProjectiles() > 0) || (getNbProjectiles() == -1)
	 */
	private boolean canShoot() {
		return (getNbProjectiles() > 0) || (getNbProjectiles() == -1);
	}
	
	/**
	 * Returns the cost of shooting this weapon on the action points of the shooter.
	 */
	public abstract int costOfShooting();
	
	/**
	 * Returns the number of projectiles to shoot this weapon with.
	 */
	public abstract int getNbProjectiles();

	/**
	 * Decreases the number of projectiles (ammunition) to shoot this weapon with by the given amount of projectiles.
	 * 
	 * @param amount
	 *        The nr of projectiles decrease the current nr of projectile by.
	 */
	public abstract void decreaseNbProjectilesWith(int amount);

	/**
	 * Returns the projectile.
	 * 
	 * @param yield
	 *        the yield used to shoot the projectile.
	 */
	public abstract Projectile getProjectile(int yield);
		 
	/**
	 * Terminate this weapon.
	 * @post new.isTerminated()
	 */
	public void terminate() {
		this.isTerminated = true;
	}
	
	/**
	 * Check whether this weapon is terminated.
	 */
	@Basic @Raw
	public boolean isTerminated() {
		return isTerminated;
	}
	
	/**
	 * Variable registering whether or not this weapon is terminated.
	 */
	private boolean isTerminated;
	
}
