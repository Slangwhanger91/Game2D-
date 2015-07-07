package game2d.character.charmechanics;

import game2d.character.charmechanics.charstats.CharStats;
import game2d.core.MapNode;
import game2d.util.SharedDataLists;
import game2d.util.shapes.Rectangle;
import javafx.scene.input.KeyCode;

@SuppressWarnings("restriction")
public abstract class NPC{
	public Rectangle shape;
	protected int speed;//movement speed
	protected int velocity;//falling speed
	protected int stacked_velocity;//falling damage
	public int height;
	public int width;
	protected SharedDataLists sharedDataLists;
	protected PreviousStepNode previous_step;//unstuck method
	public CharStats charStats;

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
		return MN[shape.y + height + 3][shape.x].type != 'A'
				|| MN[shape.y + height + 3][shape.x + width].type != 'A';
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
