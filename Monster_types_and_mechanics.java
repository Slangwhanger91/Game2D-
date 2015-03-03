import java.awt.Point;
import java.awt.Rectangle;


public class Monster_types_and_mechanics {}

class monsters extends NPC{
	Map_List map_list;
	
	monsters(Map_List M, Char_stats CS, int i){
		init();
		this.CS = CS; //create a new one with relevance to the level
		this.map_list = M;
		Point mc = M.map_list[M.map_index].monster_coords.get(i);//to reduce code size...
		shape = new Rectangle(mc.x, mc.y, width, height);
	}
	
	public void AI_movement(){
		
	}
	
	public void AI_gravity(){
		
	}
	
	@Override
	public void movement(char key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gravity(char key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		speed = 4;
		height = 18;//sides hitbox split into 5
		width = 12;//buttom/top hitbox split into 4
		velocity = 0;
		previous_step = new PSN();		
	}
	
}

//type of mobs here:
/*class mob1 extends monsters{ 
	
}*/