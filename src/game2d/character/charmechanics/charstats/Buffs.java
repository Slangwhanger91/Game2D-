package game2d.character.charmechanics.charstats;

public class Buffs{

	/**Called every frame*/
	public void tick(){
		immunity--;
	}

	private int immunity;
	//TODO: add poisons, power-ups, health extensions, speed etc..

	public Buffs(){
		immunity = 0;//amount of frames where you're immune
	}

	public void defaultImmunityOnHit(){//reset
		immunity = 30;
	}

	public boolean isImmune(){
		return immunity > 0;
	}
}
