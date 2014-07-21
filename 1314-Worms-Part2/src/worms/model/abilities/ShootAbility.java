package worms.model.abilities;
import worms.model.*;

/**
 * An interface for the ability of shooting weapons.
 * 
 * @author Delpine Vandamme
 * 
 */
public interface ShootAbility {
	
	public void shoot(int yield);
	
	public boolean canShoot(Weapon weapon);
	
	public Weapon selectedWeapon();
	
	public String getSelectedWeapon();
	
	public void selectNextWeapon();
}
