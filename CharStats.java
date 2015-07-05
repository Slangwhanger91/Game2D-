import java.util.ArrayList;

public class CharStats {
	private ArrayList<Weapon> weapons;
	private Weapon current_weapon;
	private int weapon_index;//weapon slot

	private int max_health;
	private int health;

	private int max_mana;
	private int mana;

	private int armor;
	private int attack_damage;//mainly for mobs
	private String name;
	private boolean alive = true;

	private boolean player;

	private void isAliveCheck(){
		if(health <= 0) alive = false;
	}

	public boolean isPlayer(){ return player; }
	public boolean isAlive(){ return alive; }
	public int getHealth(){ return health; }
	public int getMana(){ return mana; }
	public int getWeaponRange(){ return current_weapon.getRange(); }
	/**'Cooldown' after every attack (measured by frames).*/
	public int getWeaponCD(){ return current_weapon.getCD(); }
	public int[] getCurrentWeaponSeq(){
		return current_weapon.getSequence();
	}

	public CharStats(String name, int max_health, int max_mana, int armor, 
			int base_damage, boolean is_player) {
		this.name = name;
		health = this.max_health = max_health;
		mana = this.max_mana = max_mana;
		this.armor = armor;
		this.attack_damage = base_damage;
		player = is_player;
		weapons = new ArrayList<Weapon>();
	}

	/*public void lvlUP(){
		max_health *= 1.1;
		max_mana *= 1.1;
		stacked_power_bits_required *= 1.5;
		armor += 2;
		++base_damage;

		health = max_health;
		mana = max_mana;
		stacked_power_bits = 0;
	}*/

	/**Percentage based hit, not effected by armor.*/
	public void fallingDamage(int stacked_velocity){
		if(stacked_velocity > 0){
			health -= ((max_health * stacked_velocity) / 100);
			System.out.println("HP: "+health);
		}
		isAliveCheck();
	}

	public void takingDamage(CharStats CS){
		takingDamage(CS.attack_damage);
		if(player)
			System.out.println("HP: "+health);
		else
			System.out.println("Mob's HP: "+health);
	}

	/**Take physical damage(effected by armor).*/
	private void takingDamage(int raw_hit){
		if(armor <= raw_hit)
			health = health - (raw_hit - armor);
		isAliveCheck();
	}

	/**<b>This CS</b> will deal its damage to the given <b>CS</b>.*/
	public void dealDamage(CharStats CS){
		//System.out.println(CS.attack_damage);
		CS.takingDamage(this);
	}

	/**Fill an empty weapon slot.*/
	public void obtainWeapon(Weapon W){
		//System.out.println("CHECK");
		if(!weapons.contains(W) && weapons.size() < 4){//amount of weapons limit
			weapons.add(W);
		}
	}

	/**Throw current weapon and replace with one on the ground.*/
	public void switchWeapon(Weapon W){
		attack_damage -= current_weapon.getDamage();

		current_weapon = W; //current weapon switched
		attack_damage += current_weapon.getDamage();
		weapons.set(weapon_index, current_weapon);

	}

	/**Select from obtained weapons. Currently limited to 4 slots*/
	public void equipWeapon(char key){//'1','2','3','4'
		Weapon W = null;
		switch(key){
		case '1':
			if(weapons.size() > 0){
				W = weapons.get(0);
				weapon_index = 0;
			}
			break;
		case '2':
			if(weapons.size() > 1){
				W = weapons.get(1);
				weapon_index = 1;
			}
			break;
		case '3':
			if(weapons.size() > 2){
				W = weapons.get(2);
				weapon_index = 2;
			}
			break;
		case '4':
			if(weapons.size() > 3){
				W = weapons.get(3);
				weapon_index = 3;
			}
			break;
		}

		if(current_weapon != null){
			attack_damage -= current_weapon.getDamage();
		}
		current_weapon = W;
		attack_damage += current_weapon.getDamage();
	}
}

class Weapon{

	private String name;
	private int dmg_bonus;
	private int range;
	private int cd;//cooldown
	private int degree_a, degree_b;//from - to: f.e 1 to 90 would be a slash from
	//above the player into a straight line where the player is facing.
	private int[] sequence;

	public Weapon(String name, int dmg_bonus, int range, int cd,
			int degree_a, int degree_b, int[] atk_seq){
		this.name = name;
		this.dmg_bonus = dmg_bonus;
		this.range = range;
		this.cd = cd;
		this.degree_a = degree_a;
		this.degree_b = degree_b;
		this.sequence = atk_seq;
	}

	public int getDamage(){ return dmg_bonus; }
	public int getRange(){ return range; }
	public int getCD(){ return cd; }
	public int getDegreeA(){ return degree_a; }
	public int getDegreeB(){ return degree_b; }
	public int[] getSequence(){ return sequence; }
	public String getName(){ return name; }
}

class Equipment{
	/**
	 * 0: head
	 * <br>1: arms
	 * <br>2: chest
	 * <br>3: legs
	 * <br>4: neck
	 * <br>5: ring*/
	private int slot;
	private String name;

	public Equipment(String name, int slot){
		this.name = name;
		this.slot = slot;
	}
}

/**creates all the items in the game*/
class GameItems{

	private ArrayList<Weapon> all_weapons = new ArrayList<Weapon>();;
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

	GameItems(){
		for (int i = 0; i < all_equipment.length; i++) {
			all_equipment[i] = new ArrayList<Equipment>();	
		}
		//=================================================================
		//add weapons:
		all_weapons.add(new Weapon("Slayer of Nothing", 200, 50, 10, 90, 90, 
				new int[]{15, 35, 50, 45, 25}));
		//System.out.println("w1: "+all_weapons.get(0));
		//=================================================================
		//add equipment:
	}
}






