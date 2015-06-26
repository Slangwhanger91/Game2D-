import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInteraction {}

class Listener implements KeyListener{
	private int moveKey;
	private int otherKey;
	private boolean isHolding;

	Listener(){
		moveKey = -1;
		otherKey = -1;
		isHolding = false;
	}

	public int get_moveKey(){
		return moveKey;
	}

	public int get_otherKey(){
		return otherKey;
	}

	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyChar());
		//System.out.println(e.getKeyCode());
		if(e.getKeyCode() == 65 || e.getKeyCode() == 68){//'a', 'd'
			//	System.out.println("holding:"+isHolding);
			if(moveKey != -1 && moveKey != e.getKeyCode())
				isHolding = true;
			moveKey = e.getKeyCode();
		}else otherKey = e.getKeyCode();

	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 65 || e.getKeyCode() == 68){//'a', 'd'
			if(!isHolding)
				moveKey = -1;
			else isHolding = false;
		}
		if(e.getKeyCode() == 32 || e.getKeyCode() == 17)//space or ctrl
			otherKey = -1;
	}

	public void keyTyped(KeyEvent e) {}
}
