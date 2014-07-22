package worms.model.weapons;
import worms.exceptions.IllegalNameException;
import worms.model.*;
import worms.model.projectiles.*;

public class Rifle extends Weapon {

	public Rifle(Worm worm) throws IllegalNameException {
		super(worm);
	}

	@Override
	public String getName() {
		return "Rifle";
	}
	
	@Override
	public int costOfShooting() {
		return 10;
	}

	
	@Override
	public Projectile getProjectile(int yield) {
		return new RifleProjectile(getWorm(), yield);
	}

	@Override
	public int getNbProjectiles() {
		return this.nbProjectiles;
	}
	
	public void setNbProjectiles(int nbProjectiles) {
		this.nbProjectiles = nbProjectiles;
	}
	
	private int nbProjectiles = -1;

	@Override
	public void decreaseNbProjectilesWith(int amount) {
		if (getNbProjectiles() > 0)		
			setNbProjectiles(getNbProjectiles()-amount);		
	}

}
