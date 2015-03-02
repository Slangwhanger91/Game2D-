import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Queue;


public class Char_mechanics {}

class PSN{
	public Queue<Integer> x;
	public Queue<Integer> y;
	public boolean bad_step;
	public PSN(){
		x = new LinkedList<Integer>();
		x.add(0);
		x.add(0);

		y = new LinkedList<Integer>();
		y.add(0);
		y.add(0);

		bad_step = false;
	}
}

abstract class NPC{
	public Rectangle shape;
	protected int speed;//movement speed
	protected int velocity;//falling speed
	protected int stacked_velocity;//falling damage
	protected int height;
	protected int width;
	protected Map_List Maps;
	protected PSN previous_step;//unstuck method
	protected Char_stats CS;
	
	/**<u>initializes:</u> <br>speed, <br>velocity, <br>stacked_velocity, <br>height,
	 * <br>width, <br>Maps(Map_List), <br>previous_step(PSN), <br>CS(Char_stats).*/
	public abstract void init();
	public abstract void movement(char key);
	public abstract void gravity(char key);
	
	public boolean isFlying(){
		for (int i = 0; i <= width; i+=3) {
			if(Maps.map_list[Maps.map_index].map[shape.y + height +1][shape.x + i].type != 'A')
				return false;
		}
		return true;
	}

	/**returns false if blocked to the left, true otherwise.*/
	protected boolean left_bump(int hill_tolerance){
		int i = 6;
		if(speed == 0) 
			return false;
		if(hill_tolerance > 0)
			i +=4;
		for (; i <= height; i+=2) {
			if(Maps.map_list[Maps.map_index].map[shape.y + height - i -1][shape.x - speed -1].type != 'A'){
				return false;
			}
		}
		return true;
	}

	protected boolean right_bump(int hill_tolerance){
		int i = 6; 
		//System.out.println(hill_tolerance + ", speed:" + speed);
		if(speed == 0) 
			return false;
		if(hill_tolerance > 0)
			i +=4;
		for (; i <= height; i+=2) {
			if(Maps.map_list[Maps.map_index].map[shape.y + height - i -1][shape.x + speed + width +1].type != 'A'){
				return false;
			}
		}
		return true;
	}

	protected void previousStepsStack(){//movement method
		if(!previous_step.bad_step){
			previous_step.x.add(shape.x);
			previous_step.x.poll();

			previous_step.y.add(shape.y);
			previous_step.y.poll();
		}
		previous_step.bad_step = false;
	}

	protected void stepReturn(){//movement method
		shape.x = previous_step.x.peek();
		shape.y = previous_step.y.peek();
		previous_step.bad_step = true;
	}

	protected boolean downhill(){//gravity method
		MapNode[][] MN = Maps.map_list[Maps.map_index].map;//to reduce code size...
		if(MN[shape.y + height + 3][shape.x].type != 'A'
				|| MN[shape.y + height + 3][shape.x + width].type != 'A'){
			return true;
		}
		return false;	
	}
}

class Player extends NPC{
	public int y_coord;//camera shit
	public int x_coord;//camera shit

	public void init(){
		speed = 4;
		height = 18;//sides hitbox split into 5
		width = 12;//buttom/top hitbox split into 4
		velocity = 0;
		previous_step = new PSN();
		stacked_velocity = -44;
	}

	Player(Map_List M, Char_stats CS){
		init();
		this.CS = CS;
		this.Maps = M;
		int[] pc = M.map_list[Maps.map_index].player_coords;//to reduce code size...
		shape = new Rectangle(pc[0], pc[1], width, height);
		
		y_coord = M.map_list[Maps.map_index].player_coords[1] - 300;
		x_coord = M.map_list[Maps.map_index].player_coords[0] - 300;
	}

	private boolean x_camera_pos(){
		//System.out.println((x_coord - shape.x) + ", " + (y_coord - shape.y));
		if(x_coord - shape.x > -300 || x_coord - shape.x < -500) return true;
		return false;
	}

	private boolean y_camera_pos(){
		//System.out.println((y_coord - shape.y));// + ", " + (y_coord - shape.y));
		if(y_coord - shape.y > -200 || y_coord - shape.y < -400) return true;
		return false;
	}

	@Override
	public void movement(char key) {
		final int SPEED = speed;
		boolean climb = false;
		int hill_tolerance = 0;
		MapNode[][] MN = Maps.map_list[Maps.map_index].map;//reducing code size...
		switch(key){
		case 'a':	
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
				if(y_camera_pos())
					y_coord -= hill_tolerance;

				shape.x -= speed;
				if(x_camera_pos())
					x_coord -= speed;

				previousStepsStack();
			}
			else if(!LB && !right_bump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;

		case 'd':
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
				if(y_camera_pos())
					y_coord -= hill_tolerance;

				shape.x += speed;
				if(x_camera_pos())
					x_coord += speed;

				previousStepsStack();
			}
			else if(!RB && !left_bump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;
		}//Receives '?' when no action is taken.
		speed = SPEED;
	}

	@Override
	public void gravity(char key) {
		boolean jumped = false;
		//System.out.println("space:"+(key==' ') + ", isFlying:"+isFlying());
		if((!isFlying() || downhill()) && key == ' '){
			velocity = -14;
			stacked_velocity = -44;
			jumped = true;
		}
		if(isFlying() || jumped){
			int g = 0;
			MapNode[][] MN = Maps.map_list[Maps.map_index].map;//to reduce code size...
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
			if(y_camera_pos())
				y_coord += velocity;

			if(velocity < 14)
				velocity += 2;
			if(velocity > 0)
				stacked_velocity += 2;

		}else{
			velocity = 6;//constant falling speed
			CS.falling_damage(stacked_velocity);
			stacked_velocity = -44;
		}
		System.out.println("health: " + CS.getHealth());
		//CS.lvlUP();
		someNextLevelCheckWow();//much wow very next level
		//System.out.println("y:"+(shape.y + height) + ", type:" + Map.map[shape.y + height][shape.x].type);
	}

	private void someNextLevelCheckWow(){	
		MapNode[][] MN = Maps.map_list[Maps.map_index].map;//fucking code size
		if(MN[shape.y - 3][shape.x - 3].type == 'P'
				|| MN[shape.y - 3][shape.x + width + 3].type == 'P'
				|| MN[shape.y + height + 3][shape.x - 3].type == 'P'
				|| MN[shape.y + height + 3][shape.x + width + 3].type == 'P'
				|| MN[shape.y + (height / 2)][shape.x + width + 5].type == 'P'
				|| MN[shape.y + (height / 2)][shape.x - 5].type == 'P')
			Maps.new_level(this);
	}
}










