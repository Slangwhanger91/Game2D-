import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;


@SuppressWarnings("restriction")
public class KeyboardInteraction {}

@SuppressWarnings("restriction")
class Listener {
	private static KeyCode moveKey;
	private static KeyCode otherKey;
	private static boolean isHolding;
	public static Game2d controller;
	public static HashMap<KeyCode, KeyBindAction> keymap = new HashMap<>();

	public Listener(){
		moveKey = null;
		otherKey = null;
		isHolding = false;
	}

	public static KeyCode get_moveKey(){
		return moveKey;
	}

	public static KeyCode get_otherKey(){
		return otherKey;
	}

	public static void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyChar());
		//System.out.println(e.getCode().ordinal());
		if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.D){//'a', 'd'
			//	System.out.println("holding:"+isHolding);
			if(moveKey != null && moveKey != e.getCode())
				isHolding = true;
			moveKey = e.getCode();
		} else if (keymap.containsKey(e.getCode())) {
			KeyBindAction tmp = keymap.get(e.getCode());
			tmp.run();
		} else {
			otherKey = e.getCode();
		}

	}

	public static void keyReleased(KeyEvent e) {
		if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.D){//'a', 'd'
			if(!isHolding)
				moveKey = null;
			else isHolding = false;
		}
		if(e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.CONTROL)//space or ctrl
			otherKey = null;
	}
}

/*	Interface used to define how lambdas used in keymap are to be used
	usage: keymap.put(KeyCode.A, () -> System.out.println("'a' was pressed!"));
*/
interface KeyBindAction {
	void run();
}
