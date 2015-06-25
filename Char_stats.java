
public class Char_stats {
	private int max_health;
	private int health;

	private int max_mana;
	private int mana;
	
	private int stacked_power_bits_required;
	private int stacked_power_bits;
	//private final int power_level_max = 3;
	private int power_level;

	private int armor;
	
	private int base_damage;

	private String name;

	private boolean alive = true;
	
	public boolean getisAlive(){
		return alive;
	}
	
	private void isAlive(){
		if(health <= 0) alive = false;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMana(){
		return mana;
	}

	public Char_stats(String name, int max_health, int max_mana, int armor, int base_damage) {
		this.name = name;
		health = this.max_health = max_health;
		mana = this.max_mana = max_mana;
		this.armor = armor;
		this.base_damage = base_damage;
		stacked_power_bits_required = 1;
		stacked_power_bits = 0;
		power_level = 0;
	}

	public void lvlUP(){
		max_health *= 1.1;
		max_mana *= 1.1;
		stacked_power_bits_required *= 1.5;
		armor += 2;
		++base_damage;
		
		health = max_health;
		mana = max_mana;
		stacked_power_bits = 0;
	}

	public void falling_damage(int stacked_velocity){
		if(stacked_velocity > 0)
			health -= ((max_health * stacked_velocity) / 100);
		isAlive();
	}

	public void taking_damage(Char_stats CS){//armor should be included
		taking_damage(CS.base_damage);
	}
	
	private void taking_damage(int raw_hit){//armor should be included
		if(armor <= raw_hit)
			health = health - (raw_hit - armor);
		isAlive();
	}
}

//place weapon classes here
//place equipment classes here
