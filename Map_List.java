import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Map_List {
	public Player Actor;
	public void set_actor_once(Player Actor){
		this.Actor = Actor;
	}

	public GameItems GI; //WHY IS THIS HERE
	/**Each column represents the things to draw on the upcoming frame.
	 * That column will then be removed and if one of the arrays get
	 * empty, it's then removed.*/
	public ArrayList<ArrayList<Integer>> image_sequences = new ArrayList<ArrayList<Integer>>();
	public void add_sequence(int[] seq){
		image_sequences.add(new ArrayList<Integer>());
		for (int i = 0; i < seq.length; i++) {
			image_sequences.get(image_sequences.size() - 1).add(seq[i]);
		}
	}

	public Maps map_list[];
	public int map_index;//Should be interacting later on with saves/loads

	public Map_List(Settings config, GameItems GI) {
		this.GI = GI;

		map_index = 0;

		String mapstr = config.get("mapseq", "M2.bmp");
		String maps_folder = config.get("mapfolder", "maps");
		String[] maps = mapstr.split(",");

		map_list = new Maps[maps.length/2];//There're two ','s for every map.
		int fetch = 0;
		for (int i = 0; i < map_list.length; i++) {
			map_list[i] = new Maps(String.format("%s/%s", maps_folder, maps[fetch++]), Integer.parseInt(maps[fetch++]));
		}
	}

	public void new_level(){
		if(map_list[map_index].bonus) map_index++;//THIS MIGHT NEED AN UPDATE WHEN WORKING ON BONUS LEVELS. ALSO CAPS LOCK OP.
		else map_index = map_list[map_index].next_map_index;//case where next map is -1 not used yet

		Point p = map_list[map_index].player_starting_coords;
		Actor.shape = new Rectangle(p.x, p.y, Actor.width, Actor.height);
		Actor.y_coord = p.y - 300;//magical numbers from a Diablo3 rainbow unicorn
		Actor.x_coord = p.x - 300;//deal with it.

		initialize_monsters();
	}

	/** monster objects to paint and act with */
	public void initialize_monsters(){
		Maps M = map_list[map_index];

		int mobs_amount = M.monster_coords.size();
		M.mobs_in_map = new ArrayList<Monster>();

		for (int i = 0; i < mobs_amount; i++) {
			//each i stands for a different mob in the current map.
			M.mobs_in_map.add(new Monster(this, new Char_stats("monsta", 100, 30, 0, 1), i));	
		}
	}

	//can be used as a save point later on
	/*public ... load_map(...){...}*/
	/*public ... save_map(...){...}*/
}
