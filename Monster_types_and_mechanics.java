import java.awt.Point;
import java.awt.Rectangle;


public class Monster_types_and_mechanics {}

class monsters extends NPC{
	Maps map;
	
	monsters(Maps M, Char_stats CS, int i){
		init();
		this.CS = CS; //create a new one with relevance to the level
		this.map = M;
		Point mc = M.monster_coords.get(i);//to reduce code size...
		shape = new Rectangle(mc.x, mc.y, width, height);
	}
	
	public void AI_movement(){
		
	}
	
	public void AI_gravity(){
		shape.y--;
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

/*class mob1 extends monsters{
	
}*/