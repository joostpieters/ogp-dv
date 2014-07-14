package worms.model;

import worms.exceptions.IllegalNameException;

public class Bazooka extends Weapon {

	public Bazooka(Worm worm) throws IllegalNameException {
		super(worm);
	}
	
	@Override
	public String getName() {
		return "Bazooka";
	}
	
	
	@Override
	public int costOfShooting() {
		return 50;
	}

	@Override
	public Projectile getProjectile(int yield) {
		return new BazookaProjectile(getWorm(), yield);
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
