package worms.model.weapons;
import be.kuleuven.cs.som.annotate.Basic;
import worms.exceptions.IllegalNameException;
import worms.model.*;
import worms.model.projectiles.*;

/**
 * A class of Rifles involving a worm.
 * 
 * @author Delphine
 *
 */
public class Rifle extends Weapon {

	/**
	 * Initialize this new weapon.               
	 */
	public Rifle() throws IllegalNameException {
	}

	/**
	 * Returns the name of the weapon.
	 * 
	 * @return result == "Rifle"
	 */
	@Override
	public String getName() {
		return "Rifle";
	}
	
	/**
	 * Returns the cost of shooting this weapon on the action points of the shooter.
	 * 
	 * @return result == 10
	 */
	@Override
	public int costOfShooting() {
		return 10;
	}

	/**
	 * Creates and returns a rifle projectile with given yield.
	 * 
	 * @param  yield
	 *         The yield used to shoot the projectile.
	 * @return result == new RifleProjectile(getWorm(), yield)
	 */
	@Override
	public Projectile getProjectile(int yield) {
		return new RifleProjectile(getWorm(), yield);
	}

	/**
	 * Returns the number of projectiles to shoot this weapon with.
	 */
	@Override @Basic
	public int getNbProjectiles() {
		return this.nbProjectiles;
	}
	
	/**
	 * Sets the number of projectiles of this weapon.
	 * 
	 * @param  nbProjectiles
	 *         The new nb of projectiles of this weapon.
	 * @post   The nb of projectiles of this weapon is equal to the given nb of projectiles.
     *       | new.getNbProjectiles() == nbProjectiles
	 */
	public void setNbProjectiles(int nbProjectiles) {
		this.nbProjectiles = nbProjectiles;
	}
	
	/**
	 * Variable registering the nb of projectiles (ammunition). (-1 means unlimited supply).
	 */
	private int nbProjectiles = -1;

	/**
	 * Decreases the number of projectiles (ammunition) to shoot this weapon with by the given amount of projectiles.
	 * 
	 * @param  amount
	 *         The nr of projectiles decrease the current nr of projectile by.
	 * @effect if (getNbProjectiles() > 0)	
			      then (setNbProjectiles( getNbProjectiles() - amount ) )
	 */
	@Override
	public void decreaseNbProjectilesWith(int amount) {
		if (getNbProjectiles() > 0)		
			setNbProjectiles(getNbProjectiles()-amount);		
	}

}
