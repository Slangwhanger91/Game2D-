package game2d.charmechanics;

import java.util.ArrayList;

/**creates all the items in the game*/
public class GameItems{

	private ArrayList<Weapon> all_weapons = new ArrayList<Weapon>();
	//equipment
	@SuppressWarnings("unchecked")
	private ArrayList<Equipment>[] all_equipment = new ArrayList[6];

	public Weapon getWeaponIndex(int weapon_index){
		return all_weapons.get(weapon_index);
	}

	/**
	 *     0: head
	 * <br>1: arms
	 * <br>2: chest
	 * <br>3: legs
	 * <br>4: neck
	 * <br>5: ring*/
	public Equipment getEquipmentIndex(int equipment_index, int slot){
		return all_equipment[slot].get(equipment_index);
	}

	public GameItems(){
		for (int i = 0; i < all_equipment.length; i++) {
			all_equipment[i] = new ArrayList<Equipment>();
		}
		//=================================================================
		//add weapons:
		all_weapons.add(new Weapon("Slayer of Nothing", 200, 50, 10, 0f, 0f,
				new int[]{15, 35, 50, 45, 25}));
		all_weapons.add(new Weapon("Illuminati Slasher", 200, 50, 10, 0.5f, -0.5f,
				new int[]{50, 50, 50, 50, 50}));//sequence needs to be animated with images
		//System.out.println("w1: "+all_weapons.get(0));
		//=================================================================
		//add equipment:
	}
}
