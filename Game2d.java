import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Game2d {
	public GameLoop gameLoop;
	public JPanel game_window;
	public paint_panel in_panel;
	private Player Actor;
	private SharedDataLists SDL;
	private Listener KL;
	private Settings config;
	private final int game_speed = 30;//fps
	private final int WINDOW_WIDTH, WINDOW_HEIGHT;
	
	/**Create the application and run it.*/
	public Game2d(Listener KL) {
		this.KL = KL;
		gameLoop = new GameLoop();
		config = new Settings("config.properties");
		/*WINDOW_WIDTH example: Picks value from the properties file, if no such value 
		exists "800" is taken as default instead.*/
		WINDOW_WIDTH = Integer.parseInt(config.get("width", "800"));
		WINDOW_HEIGHT = Integer.parseInt(config.get("height", "600"));
		SDL = new SharedDataLists(config, new GameItems());
		SDL.initialize_monsters();
		Actor = new Player(SDL, new Char_stats("Playa", 100, 30, 0, 1));
		SDL.set_actor_once(Actor);

		initialize();//initializing frames and panels
	}

	/**Initialize the contents of the frame.*/
	private void initialize() {
		game_window = new JPanel();
		game_window.setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);
		//game_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//game_window.getContentPane().setLayout(null);
		game_window.setBackground(Color.GRAY);
		//game_window.setTitle(config.get("title", "Game2d"));

		in_panel = new paint_panel();
		in_panel.setBackground(Color.WHITE);
		in_panel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		game_window.add(in_panel);

		//game_window.addKeyListener(KL);
	}

	/**Costume panel class for overriding the paintComponent method of a JPanel.*/
	@SuppressWarnings("serial")
	class paint_panel extends JPanel{

		private final int portal_resize = 5;

		paint_panel(){
			super();
		}

		/**Overriding method to paint on <b>this JPanel object</b>.*/
		public void paintComponent(final Graphics g){
			super.paintComponent(g);
			//
			//paints map every frame
			for(PaintRectNode PRN : SDL.map_list[SDL.map_index].toPaint){
				switch(PRN.type){
				case 'G':
					g.setColor(Color.GREEN);
					g.fillRect(PRN.rect.x - Actor.x_coord(), 
							PRN.rect.y - Actor.y_coord(), PRN.rect.width, 
							PRN.rect.height);
					break;
				case 'P':
					g.setColor(Color.YELLOW);
					g.fillOval(PRN.rect.x - Actor.x_coord() - portal_resize, 
							PRN.rect.y - Actor.y_coord() - portal_resize, 
							PRN.rect.width + (portal_resize*2), 
							PRN.rect.height + (portal_resize*2));
					break;
				}
			}
			//
			//painting characters/creatures
			g.setColor(Color.MAGENTA);//why's purple called "magneta", who knows..
			for(Monster MOB : SDL.map_list[SDL.map_index].mobs_in_map){
				Rectangle R = MOB.shape;
				g.fillRect(R.x - Actor.x_coord(), R.y - Actor.y_coord(), 
						R.width, R.height);
			}
			//
			//painting main character
			g.setColor(Color.BLACK);
			g.fillRect(Actor.shape.x - Actor.x_coord(), 
					Actor.shape.y - Actor.y_coord(), Actor.width, Actor.height);
			//
			//weapon related animations:
			g.setColor(Color.BLUE);
			ArrayList<Integer> seq;
			for (int i = 0; i < SDL.image_sequences.size(); i++) {
				seq = SDL.image_sequences.get(i);
				
				if(Actor.getFacing() == 'd'){
					g.fillRect(Actor.shape.x - Actor.x_coord() + Actor.width, 
							Actor.shape.y - Actor.y_coord() + Actor.height/2,
							seq.remove(0), 2);
				}
				else{
					int img = seq.remove(0);
					g.fillRect(Actor.shape.x - Actor.x_coord() - img, 
							Actor.shape.y - Actor.y_coord() + Actor.height/2,
							img, 2);
				}

				if(seq.isEmpty())
					SDL.image_sequences.remove(i);
			}
		}//end of paintComponent.
	}//end of paint panel.

	public class GameLoop extends Thread{
		public void run() {
			while (KL.get_otherKey() != 27 && Actor.CS.getisAlive()) { //27 = esc
				try {
					Thread.sleep(game_speed);
				} catch (InterruptedException e) {}
				//Actor actions:
				Actor.movement(KL.get_moveKey());
				Actor.gravity(KL.get_otherKey());
				Actor.actions(KL.get_otherKey());
				//System.out.println("x coords: " + Actor.x_coord);
				//System.out.println("shape x: " + Actor.shape.x);
				//
				//NPCs actions:
				ArrayList<Monster> mobs = SDL.map_list[SDL.map_index].mobs_in_map;
				for (int i = 0; i < mobs.size(); i++) {
					if (mobs.get(i).CS.getisAlive()) {
						mobs.get(i).AI_movement();
						mobs.get(i).AI_gravity();
					} else {
						mobs.remove(i);
					}
				}
				in_panel.repaint();
			}//Game loop ends

			System.out.println("GAME OVER, L2P");

			while (KL.get_otherKey() != 27) {//Waiting for player to accept defeat/stop crying
				try {
					Thread.sleep(game_speed * 10);
				} catch (InterruptedException e) {};
			}
			System.exit(0);
		}
	}
}
