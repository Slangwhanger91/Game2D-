
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;

@SuppressWarnings("restriction")
public class Game2d {
	private GameLoop gameLoop;
	private Canvas game_window;
	private Player Actor;
	private SharedDataLists SDL;
	private Settings config;
	private final int game_speed = 30;//fps
	private final int WINDOW_WIDTH, WINDOW_HEIGHT;

	// javafx
	public Scene scene;
	private Group root;
	private GraphicsContext g;

	/**Create the application and run it.*/
	public Game2d() {
		config = new Settings("config.properties");
		/*WINDOW_WIDTH example: Picks value from the properties file, if no such value 
		exists "800" is taken as default instead.*/
		WINDOW_WIDTH = Integer.parseInt(config.get("width", "800"));
		WINDOW_HEIGHT = Integer.parseInt(config.get("height", "600"));
		SDL = new SharedDataLists(config, new GameItems());
		SDL.initialize_monsters();
		Actor = new Player(SDL, new CharStats("Playa", 100, 30, 0, 1, true));
		SDL.set_actor_once(Actor);

		//initialize();//initializing frames and panels
		root = new Group();
		scene = new Scene(root);

		game_window = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		root.getChildren().add(game_window);
		scene.addEventHandler(KeyEvent.KEY_PRESSED,
				event -> Listener.keyPressed(event));
		scene.addEventHandler(KeyEvent.KEY_RELEASED,
				event -> Listener.keyReleased(event));

		g = game_window.getGraphicsContext2D();

		final Duration frameDuration = Duration.millis(1000/30);
		final KeyFrame keyFrame = new KeyFrame(frameDuration,
				event -> tick());

		Timeline gameLoop = new Timeline(30, keyFrame);
		gameLoop.setCycleCount(Animation.INDEFINITE);
		gameLoop.play();
				/*
		gameLoop = new GameLoop();
		gameLoop.start();
		reset();
		update();
		*/
	}

	/*
	private void initialize() {
		//game_window = new JPanel();
		game_window = new Canvas();
		//game_window.setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);

		in_panel = new paint_panel();
		//in_panel.setBackground(Color.WHITE);
		//in_panel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		//game_window.add(in_panel);
	}
	 */

	private void reset() {
		//System.out.println("reset");
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, game_window.getWidth(), game_window.getHeight());
	}

	final int portal_resize = 5;
	void update() {
		for(PaintRectNode PRN : SDL.map_list[SDL.map_index].toPaint){
			switch(PRN.type){
			case 'G':
				g.setFill(Color.GREEN);
				g.fillRect(PRN.rect.x - Actor.xCoord(),
						PRN.rect.y - Actor.yCoord(), PRN.rect.width,
						PRN.rect.height);
				break;
			case 'P':
				g.setFill(Color.YELLOW);
				g.fillOval(PRN.rect.x - Actor.xCoord() - portal_resize,
						PRN.rect.y - Actor.yCoord() - portal_resize,
						PRN.rect.width + (portal_resize*2),
						PRN.rect.height + (portal_resize*2));
				break;
			}
		}
		//
		//painting characters/creatures
		g.setFill(Color.MAGENTA);//why's purple called "magneta", who knows..
		for(Monster MOB : SDL.map_list[SDL.map_index].mobs_in_map){
			Rectangle R = MOB.shape;
			g.fillRect(R.x - Actor.xCoord(), R.y - Actor.yCoord(),
					R.width, R.height);
		}
		//
		//painting main character
		g.setFill(Color.BLACK);
		g.fillRect(Actor.shape.x - Actor.xCoord(),
				Actor.shape.y - Actor.yCoord(), Actor.width, Actor.height);
		//
		//weapon related animations:
		g.setFill(Color.BLUE);
		ArrayList<Integer> seq;
		for (int i = 0; i < SDL.image_sequences.size(); i++) {
			seq = SDL.image_sequences.get(i);

			if(Actor.getFacing() == 'd'){
				g.fillRect(Actor.shape.x - Actor.xCoord() + Actor.width,
						Actor.shape.y - Actor.yCoord() + Actor.height/2,
						seq.remove(0), 2);
			}
			else{
				int img = seq.remove(0);
				g.fillRect(Actor.shape.x - Actor.xCoord() - img,
						Actor.shape.y - Actor.yCoord() + Actor.height/2,
						img, 2);
			}

			if(seq.isEmpty())
				SDL.image_sequences.remove(i);
		}
	}

	private void tick() {
		//Actor actions:
		Actor.movement(Listener.get_moveKey());
		Actor.gravity(Listener.get_otherKey());
		Actor.actions(Listener.get_otherKey());
		//System.out.println("x coords: " + Actor.x_coord);
		//System.out.println("shape x: " + Actor.shape.x);
		//
		//NPCs actions:
		ArrayList<Monster> mobs = SDL.map_list[SDL.map_index].mobs_in_map;
		for (int i = 0; i < mobs.size(); i++) {
			if (mobs.get(i).charStats.isAlive()) {
				mobs.get(i).AI_movement();
				mobs.get(i).AI_gravity();
			} else {
				mobs.remove(i);
			}
		}
		reset();
		update();
	}

	public class GameLoop extends Thread{
		public void run() {		
			while (Listener.get_otherKey() != KeyCode.ESCAPE && Actor.charStats.isAlive()) { //27 = esc
				try {
					Thread.sleep(game_speed);
				} catch (InterruptedException e) {}
				//Actor actions:
				Actor.movement(Listener.get_moveKey());
				Actor.gravity(Listener.get_otherKey());
				Actor.actions(Listener.get_otherKey());
				//System.out.println("x coords: " + Actor.x_coord);
				//System.out.println("shape x: " + Actor.shape.x);
				//
				//NPCs actions:
				ArrayList<Monster> mobs = SDL.map_list[SDL.map_index].mobs_in_map;
				for (int i = 0; i < mobs.size(); i++) {
					if (mobs.get(i).charStats.isAlive()) {
						mobs.get(i).AI_movement();
						mobs.get(i).AI_gravity();
					} else {
						mobs.remove(i);
					}
				}
				reset();
				update();
			}//Game loop ends

			System.out.println("GAME OVER, L2P");

			while (Listener.get_otherKey() != KeyCode.ESCAPE) {
				//Waiting for player to accept defeat/stop crying
				try {
					Thread.sleep(game_speed * 10);
				} catch (InterruptedException e) {};
			}
			System.exit(0); // TODO: Change to GameWindow.close() or menu
		}
	}
}
