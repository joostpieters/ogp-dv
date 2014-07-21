package worms.model;
import worms.exceptions.*;
import be.kuleuven.cs.som.annotate.*;

public abstract class Weapon {
	
	public Weapon(Worm worm) 
	  throws IllegalNameException{
	}
	
	/**
	 * Returns the name the worm.
	 */
	@Basic @Raw
	public String getName() {
		return this.name;
	}
	
	/**
	 * Variable registering the name of the worm.
	 */
	private String name;
	
	/**
	 * Return the worm to which this game object is attached.
	 *   A null reference is returned if this game object is not attached to any worm.
	 */
	@Basic @Raw
	public Worm getWorm() {
		return this.worm;
	}

	/**
	 * Check whether this game object can be attached to the given worm.
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
	 * Check whether this game object has a proper worm to which it is attached.
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
	 * Set the worm to which this game object is attached to the given worm.
	 * 
	 * @param worm
	 * 		  The worm to attach this game object to.
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
	 * Variable registering the worm to which this game object is attached.
	 */
	private Worm worm = null;
	
	/**
	 * 
	 * @param yield
	 * @throws IllegalShootException
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
	 * 
	 * @return
	 */
	private boolean canShoot() {
		return (getNbProjectiles() > 0) || (getNbProjectiles() == -1);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract int costOfShooting();
	
	/**
	 * 
	 * @return
	 */
	public abstract int getNbProjectiles();

	/**
	 * 
	 * @param amount
	 */
	public abstract void decreaseNbProjectilesWith(int amount);

	/**
	 * 
	 * @param yield
	 */
	public abstract Projectile getProjectile(int yield);
		 
	/**
	 * Terminate this weapon and its associated projectile if any.
	 * @post new.isTerminated()
	 * @post ! new.hasProjectile()
	 * @post if (hasProjectile()) then (new getProjectile()).isTerminated()
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
