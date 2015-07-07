import javafx.scene.input.KeyCode;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("restriction")
public class CharMechanics {}

/**Prevents a character from getting stuck.*/
class PreviousStepNode{
	public Queue<Integer> x;
	public Queue<Integer> y;
	public boolean bad_step;
	public PreviousStepNode(){
		x = new LinkedList<Integer>();
		x.add(0);
		x.add(0);

		y = new LinkedList<Integer>();
		y.add(0);
		y.add(0);

		bad_step = false;
	}
}

@SuppressWarnings("restriction")
abstract class NPC{
	public Rectangle shape;
	protected int speed;//movement speed
	protected int velocity;//falling speed
	protected int stacked_velocity;//falling damage
	protected int height;
	protected int width;
	protected SharedDataLists sharedDataLists;
	protected PreviousStepNode previous_step;//unstuck method
	protected CharStats charStats;

	/**<u>initializes:</u> <br>speed, <br>velocity, <br>stacked_velocity, <br>height,
	 * <br>width, <br>previous_step(PSN).*/
	public abstract void init();
	public abstract void movement(KeyCode key);
	public abstract void gravity(KeyCode key);

	public boolean isFlying(){
		for (int i = 0; i <= width; i+=3) {
			if(sharedDataLists.map_list[sharedDataLists.map_index].map[shape.y + height +1][shape.x + i].type != 'A')
				return false;
		}
		return true;
	}

	/**returns false if blocked to the left, true otherwise.*/
	protected boolean leftBump(int hill_tolerance){
		int i = 6;
		if(speed == 0) 
			return false;
		if(hill_tolerance > 0)
			i +=4;
		for (; i <= height; i+=2) {
			if(sharedDataLists.map_list[sharedDataLists.map_index].map[shape.y + height - i -1][shape.x - speed -1].type != 'A'){
				return false;
			}
		}
		return true;
	}

	protected boolean rightBump(int hill_tolerance){
		int i = 6; 
		//System.out.println(hill_tolerance + ", speed:" + speed);
		if(speed == 0) 
			return false;
		if(hill_tolerance > 0)
			i +=4;
		for (; i <= height; i+=2) {
			if(sharedDataLists.map_list[sharedDataLists.map_index].map[shape.y + height - i -1][shape.x + speed + width +1].type != 'A'){
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

	protected int climbRight(MapNode[][] MN){//movement method
		boolean climb = false;
		int hill_tolerance = 0;
		while(!climb && speed > 0 && MN[shape.y + height-1][shape.x + speed + width].type != 'A'){
			for (hill_tolerance = 2; !climb && hill_tolerance < 6; hill_tolerance++) {
				if(MN[shape.y + height - hill_tolerance -1][shape.x + speed + width].type == 'A'){
					climb = true;
				}
			}
			if(!climb) speed--;
		}
		return hill_tolerance;
	}

	protected int climbLeft(MapNode[][] MN){//movement method
		boolean climb = false;
		int hill_tolerance = 0;
		while(!climb && speed > 0 && MN[shape.y + height-1][shape.x - speed].type != 'A'){
			for (hill_tolerance = 2; !climb && hill_tolerance < 6; hill_tolerance++) {
				if(MN[shape.y + height - hill_tolerance -1][shape.x - speed].type == 'A'){
					climb = true;
				}
			}
			if(!climb) speed--;
		}
		return hill_tolerance;
	}

	protected boolean downhill(){//gravity method
		MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;//to reduce code size...
		if(MN[shape.y + height + 3][shape.x].type != 'A'
				|| MN[shape.y + height + 3][shape.x + width].type != 'A'){
			return true;
		}
		return false;	
	}
	
	protected boolean gravityMethod(KeyCode key){
		boolean jumped = false;
		//System.out.println("space:"+(key==' ') + ", isFlying:"+isFlying());
		if((!isFlying() || downhill()) && key == KeyCode.SPACE){
			velocity = -14;
			stacked_velocity = -44;
			jumped = true;
		}
		
		boolean flying_or_jumped = false;
		if(isFlying() || jumped){
			flying_or_jumped = true;
			int g = 0;
			MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;//to reduce code size...
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
		}
	
		return flying_or_jumped;
	}
}

@SuppressWarnings("restriction")
class Player extends NPC{
	SoundController soundController;

	private int y_coord;//camera related
	private int x_coord;//camera related

	private int attack_delay = 0;
	/**related to attack moves*/
	private char facing;

	public char getFacing(){ return facing; }

	/**Camera related*/
	public int yCoord(){ return y_coord; }
	/**Camera related*/
	public int xCoord(){ return x_coord; }
	/**Camera related*/
	public void setCoords(int x, int y){
		x_coord = x;
		y_coord = y;
	}

	public void init(){
		speed = 4;
		height = 18;//sides hitbox split into 5
		width = 12;//buttom/top hitbox split into 4
		velocity = 0;
		previous_step = new PreviousStepNode();
		stacked_velocity = -44;
	}

	Player(SharedDataLists SDL, CharStats CS, SoundController soundController){
		init();
		charStats = CS;
		sharedDataLists = SDL;
		this.soundController = soundController;

		charStats.obtainWeapon(sharedDataLists.gameItems.getWeaponIndex(0));
		charStats.obtainWeapon(sharedDataLists.gameItems.getWeaponIndex(1));
		charStats.equipWeapon('1');

		Point p = sharedDataLists.map_list[sharedDataLists.map_index].player_starting_coords;
		shape = new Rectangle(p.x, p.y, width, height);
		//camera:
		x_coord = p.x - 300;
		y_coord = p.y - 300;
	}

	private boolean xCameraPos(){
		//System.out.println((x_coord - shape.x) + ", " + (y_coord - shape.y));
		if(x_coord - shape.x > -300 || x_coord - shape.x < -500) return true;
		return false;
	}

	private boolean yCameraPos(){
		//System.out.println((y_coord - shape.y));// + ", " + (y_coord - shape.y));
		if(y_coord - shape.y > -200 || y_coord - shape.y < -400) return true;
		return false;
	}

	private void stabRight(Monster M){
		if(M.shape.x + M.shape.width > this.shape.x + this.shape.width //checking weapon base
				&& M.shape.x <= //checking weapon edge
				this.shape.x + this.shape.width + this.charStats.getWeapon().getRange()){
			this.charStats.dealDamage(M.charStats);
			//System.out.println("HIT (right)");
		}
	}

	private void stabLeft(Monster M){
		if(M.shape.x < this.shape.x //checking weapon base
				&& M.shape.x + M.shape.width >= //checking weapon edge
				this.shape.x - this.charStats.getWeapon().getRange()){
			this.charStats.dealDamage(M.charStats);
			//System.out.println("HIT (left)");
		}
	}

	private void attack(){
		float degree_a = charStats.getWeapon().getDegreeA();
		float degree_b = charStats.getWeapon().getDegreeB();
		soundController.playSound("beep"); // TODO: Replace with attack sound, put into stableft instead if we want spesific sounds per attack (we do)

		if(degree_a - degree_b == 0){//stab
			for (Monster M : sharedDataLists.map_list[sharedDataLists.map_index].mobs_in_map) {
				int weapon_y_axis = this.shape.y + this.height/2;
				boolean within_y = (M.shape.y <= weapon_y_axis) 
						&& (M.shape.y + M.shape.height >= weapon_y_axis);
				if(within_y){
					if(facing == 'd')
						stabRight(M);
					else 
						stabLeft(M);
				}
			}
		}else{//slash
			Triangle triangular_area;
			Weapon W = charStats.getWeapon();
			int actor_x, actor_y = shape.y + shape.height/2;

			for (Monster M : sharedDataLists.map_list[sharedDataLists.map_index].mobs_in_map) {
				if(facing == 'd'){//right
					actor_x = shape.x + shape.width;

					triangular_area = W.setTriangle(actor_x, actor_y, 
							actor_x + W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeA()), 
							actor_x + W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeB()));
				}else{//left
					actor_x = shape.x;
					triangular_area = W.setTriangle(actor_x, actor_y, 
							actor_x - W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeA()), 
							actor_x - W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeB()));
				}
				//===================================================================================
				if(M.shape.isIntersectingTriangle(triangular_area)){
					charStats.dealDamage(M.charStats);

					/*System.out.println("actor_y "+actor_y+
							", top_reach "+ (-(int)(W.getRange() * W.getDegreeA()))+ 
							", bottom_reach "+ (-(int)(W.getRange() * W.getDegreeB())));
					System.out.println("mob_y top "+M.shape.y+"mob_y bottom "+(M.shape.y + M.shape.height));*/
					//System.out.println("HIT");
				}
			}
		}
	}

	public void actions(KeyCode key){
		if(attack_delay > 0) attack_delay--;
		else if(key == KeyCode.CONTROL){ //ctrl (both sides)
			sharedDataLists.add_sequence(charStats.getWeapon().getSequence());
			attack_delay = charStats.getWeapon().getCD();
			attack();
		}

		if(attack_delay == charStats.getWeapon().getCD() -3)
			attack(); //consistency with animation
	}


	@SuppressWarnings("incomplete-switch")
	@Override
	public void movement(KeyCode key) {
		if (key == null) return;

		final int SPEED = speed;
		int hill_tolerance;
		MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;

		switch(key){
		case A:
			facing = 'a';
			hill_tolerance = climbLeft(MN);
			boolean LB = leftBump(hill_tolerance);
			if(LB){
				shape.y -= hill_tolerance;
				shape.x -= speed;

				//player stuff
				if(xCameraPos()) x_coord -= speed;
				if(yCameraPos()) y_coord -= hill_tolerance;

				previousStepsStack();
			}
			else if(!LB && !rightBump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;

		case D:
			facing = 'd';
			hill_tolerance = climbRight(MN);
			boolean RB = rightBump(hill_tolerance);
			if(RB){
				shape.y -= hill_tolerance;
				shape.x += speed;

				//player stuff
				if(xCameraPos()) x_coord += speed;
				if(yCameraPos()) y_coord -= hill_tolerance;

				previousStepsStack();
			}
			else if(!RB && !leftBump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;
		}
		speed = SPEED;
	}

	@Override
	public void gravity(KeyCode key) {
		if(gravityMethod(key)){
			shape.y += velocity;
			if(yCameraPos()) 
				y_coord += velocity;
			if(velocity < 14) 
				velocity += 2;
			if(velocity > 0)
				stacked_velocity += 2;
		}else{
			velocity = 6;//constant falling speed
			charStats.fallingDamage(stacked_velocity);
			stacked_velocity = -44;
		}
		someNextLevelCheckWow();//much wow very next level
		//System.out.println("health: " + CharStats.getHealth()); //SHOW HEALTH
		//System.out.println("y:"+(shape.y + height) + ", type:" + Map.map[shape.y + height][shape.x].type);
	}
	
	public void buffDurations() {
		charStats.buffsTick();
	}

	private void someNextLevelCheckWow(){	
		MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;//fucking code size
		if(MN[shape.y - 3][shape.x - 3].type == 'P'
				|| MN[shape.y - 3][shape.x + width + 3].type == 'P'
				|| MN[shape.y + height + 3][shape.x - 3].type == 'P'
				|| MN[shape.y + height + 3][shape.x + width + 3].type == 'P'
				|| MN[shape.y + (height / 2)][shape.x + width + 5].type == 'P'
				|| MN[shape.y + (height / 2)][shape.x - 5].type == 'P')
			sharedDataLists.new_level();
	}


}










