import java.awt.Point;
import java.awt.Rectangle;


public class Monster_types_and_mechanics {}

class Monster extends NPC{

	private int direction = -1;

	/**
	 * @param SDL - connection with the map
	 * @param CS - stats
	 * @param i - retrieves index from <b>monster_coords</b> ArrayList
	 */
	Monster(SharedDataLists SDL, Char_stats CS, int i){
		init();
		this.CS = CS; //create a new one with relevance to the level
		this.SDL = SDL;
		//mob coordinates
		Point mc = SDL.map_list[SDL.map_index].monster_coords.get(i);//to reduce code size...
		shape = new Rectangle(mc.x, mc.y, width, height);
	}

	public void AI_movement(){
		Rectangle c = SDL.Actor.shape;
		boolean move = true;
		if(c.x + c.width < shape.x){
			if(!isFlying()){
				if(shape.x - (c.x + c.width) > 350) move = false;
				else direction = 65;//'a'
			}
		}
		else if(c.x > shape.x + shape.width){
			if(!isFlying()){
				if(c.x - (shape.x + shape.width) > 350) move = false;
				else direction = 68;//'d'
			}
		}
		else if((c.y > shape.y && c.y < shape.y + shape.height)
				|| (c.y < shape.y && c.y + c.height > shape.y)){
			//inside the x range of the player's rectangle
			SDL.Actor.CS.taking_damage(this.CS);
		}

		if(c.y + c.height < shape.y){//player above monster
			if(shape.y - (c.y + c.height) > 300) move = false;
		}
		else{//player below monster
			if(c.y - (shape.y + shape.height) > 300) move = false;
		}

		//====================================================
		if(move) movement(direction);//try to reach the player
	}

	public void AI_gravity(){
		double j = Math.random();
		int key;

		if(j > 0.90) key = 32;//10% per frame to jump
		else key = -1;

		gravity(key);//gravity mechanics + option to jump
	}

	@Override
	public void movement(int direction) {

		final int SPEED = speed;
		boolean climb = false;
		int hill_tolerance = 0;
		MapNode[][] MN = SDL.map_list[SDL.map_index].map;//reducing code size...
		switch(direction){
		case 65://'a'	
			while(!climb && speed > 0 && MN[shape.y + height-1][shape.x - speed].type != 'A'){
				for (hill_tolerance = 2; !climb && hill_tolerance < 6; hill_tolerance++) {
					if(MN[shape.y + height - hill_tolerance -1][shape.x - speed].type == 'A'){
						climb = true;
					}
				}
				if(!climb) speed--;
			}

			boolean LB = left_bump(hill_tolerance); 
			if(LB){
				shape.y -= hill_tolerance;
				shape.x -= speed;
				
				previousStepsStack();
			}
			else if(!LB && !right_bump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;

		case 68://'d'
			while(!climb && speed > 0 && MN[shape.y + height-1][shape.x + speed + width].type != 'A'){
				for (hill_tolerance = 2; !climb && hill_tolerance < 6; hill_tolerance++) {
					if(MN[shape.y + height - hill_tolerance -1][shape.x + speed + width].type == 'A'){
						climb = true;
					}
				}
				if(!climb) speed--;
			}
			boolean RB = right_bump(hill_tolerance);
			if(RB){
				shape.y -= hill_tolerance;
				shape.x += speed;

				previousStepsStack();
			}
			else if(!RB && !left_bump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;
		}//Receives -1 when no action is taken.
		speed = SPEED;
	}

	@Override
	public void gravity(int key) {
		boolean jumped = false;
		//System.out.println("space:"+(key==' ') + ", isFlying:"+isFlying());
		if((!isFlying() || downhill()) && key == 32){//32 == space
			velocity = -14;
			jumped = true;
		}
		if(isFlying() || jumped){
			int g = 0;
			MapNode[][] MN = SDL.map_list[SDL.map_index].map;//to reduce code size...
			if(velocity > 0){//Falling.
				boolean ground_below;
				while(g < velocity){
					ground_below = false; 
					for (int i = 0; i <= width && !ground_below; i+=3) {
						if(MN[shape.y + height + g][shape.x + i].type != 'A')
							ground_below = true;
					}
					if(ground_below) velocity = g;
					//else g +=4; //buggy but efficient, needs a fix
					else g++;
				}
			}else{//Flying.
				boolean ceiling_above;
				while(g > velocity){
					ceiling_above = false; 
					for (int i = 0; i <= width && !ceiling_above; i+=3) {
						if(MN[shape.y + g][shape.x + i].type != 'A')
							ceiling_above = true;
					}
					if(ceiling_above) velocity = g;
					//else g -=4; //buggy but efficient, needs a fix
					else g--;
				}
			}

			shape.y += velocity;
			if(velocity < 14)
				velocity += 2;

		}else{
			velocity = 6;
		}
		//System.out.println("health: " + CS.getHealth()); //SHOW HEALTH
		//System.out.println("y:"+(shape.y + height) + ", type:" + Map.map[shape.y + height][shape.x].type);
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