import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyboardInteraction {

}

class Listener implements KeyListener{
	private char moveKey;
	private char otherKey;
	private boolean isHolding;
	
	Listener(){
		moveKey = '?';
		otherKey = '?';
		isHolding = false;
		//System.out.println("done");
	}
	
	public char get_moveKey(){
		return moveKey;
	}
	
	public char get_otherKey(){
		return otherKey;
	}
	
	//@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyChar());
		if(e.getKeyChar() == 'a' || e.getKeyChar() == 'd'){
			if(moveKey != '?' && moveKey != e.getKeyChar())
				isHolding = true;
			moveKey = e.getKeyChar();
		}else otherKey = e.getKeyChar();

	}

	//@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar() == 'a' || e.getKeyChar() == 'd'){
			if(!isHolding)
				moveKey = '?';
			else isHolding = false;
		}
		if(e.getKeyChar() == ' ')
			otherKey = '?';
	}

	//@Override
	public void keyTyped(KeyEvent e) {}

}
