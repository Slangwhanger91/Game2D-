import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Game2d{
	private static Player Actor;
	private JFrame game_window;
	private final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static paint_panel in_panel;
	private static Listener KL = new Listener();

	//private static ArrayList<monsters> chars;
	static private Map_List Maps;

	public static int game_speed = 30;//fps

	/**Create the application and run it.*/
	public Game2d() {
		Maps = new Map_List();
		initialize();//initializing frames and panels
	}

	/**Initialize the contents of the frame.*/
	private void initialize() {
		game_window = new JFrame();
		game_window.setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);
		game_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game_window.getContentPane().setLayout(null);
		game_window.getContentPane().setBackground(Color.GRAY);
		game_window.setTitle("Game2d Demo");

		in_panel = new paint_panel();
		in_panel.setBackground(Color.WHITE);
		in_panel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		game_window.getContentPane().add(in_panel);

		Maps.initialize_monsters();
		
		Actor = new Player(Maps, new Char_stats("Playa", 100, 30, 0, 15));
		game_window.addKeyListener(KL);
	}

	/**Costume panel class for overriding the paintComponent method of a JPanel.*/
	@SuppressWarnings("serial")
	class paint_panel extends JPanel{
		paint_panel(){
			super();
		}

		private final int portal_resize = 5;

		/**Overriding method to paint on <b>this JPanel object</b>.*/
		public void paintComponent(final Graphics g){
			super.paintComponent(g);

			//paints map every frame
			for(PaintRectNode PRN : Maps.map_list[Maps.map_index].toPaint){
				//if(PRN.type == 'C')
				//System.out.println("paintsize: " + Maps.map_list[Maps.map_index].toPaint.size());
				//System.out.println(PRN.type);
				switch(PRN.type){
				case 'G':
					g.setColor(Color.GREEN);
					g.fillRect(PRN.rect.x - Actor.x_coord, PRN.rect.y - Actor.y_coord, PRN.rect.width, PRN.rect.height);
					break;
				case 'P':
					g.setColor(Color.YELLOW);
					g.fillOval(PRN.rect.x - Actor.x_coord - portal_resize, PRN.rect.y - Actor.y_coord - portal_resize, PRN.rect.width + (portal_resize*2), PRN.rect.height + (portal_resize*2));
					break;
				}
			}

			//painting characters/creatures
			g.setColor(Color.MAGENTA);//why's purple called "magneta", who knows..
			for(monsters MOB : Maps.map_list[Maps.map_index].mobs_in_map){
				Rectangle R = MOB.shape;
				g.fillRect(R.x - Actor.x_coord, R.y - Actor.y_coord, R.width, R.height);
			}
			
			//painting main character
			g.setColor(Color.BLACK);
			g.fillRect(Actor.shape.x - Actor.x_coord, Actor.shape.y - Actor.y_coord, Actor.width, Actor.height);
			
		}//end of paintComponent.
	}//end of paint panel.

	static class GameLoop extends Thread{
		public void run(){
			while((int)KL.get_otherKey() != 27 && Actor.CS.getisAlive()){ //27 = esc
				try{Thread.sleep(game_speed);}catch(InterruptedException e){};
				//Actor actions:
				Actor.movement(KL.get_moveKey());
				Actor.gravity(KL.get_otherKey());
				//NPCs actions:
				for (monsters MOB : Maps.map_list[Maps.map_index].mobs_in_map){
					MOB.AI_movement();
					MOB.AI_gravity();
				}
				in_panel.repaint();
			}
			while((int)KL.get_otherKey() != 27 && !Actor.CS.getisAlive()){
				try{Thread.sleep(game_speed);}catch(InterruptedException e){};
			}
			System.exit(0);
		}
	}


	/**Launch the application.*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game2d window = new Game2d();
					window.game_window.setVisible(true);
					GameLoop game_loop = new GameLoop();
					game_loop.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
