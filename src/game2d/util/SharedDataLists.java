package game2d.util;

import game2d.character.charmechanics.charstats.CharStats;
import game2d.character.charmechanics.charstats.GameItems;
import game2d.character.charmechanics.monsters.Monster;
import game2d.core.Map;
import game2d.character.charmechanics.Player;
import game2d.util.shapes.Point;
import game2d.util.shapes.Rectangle;

import java.util.ArrayList;

public class SharedDataLists {
	public Player Actor;
	public void set_actor_once(Player Actor){
		this.Actor = Actor;
	}

	public GameItems gameItems;

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

	public Map map_list[];
	public int map_index;//Should be interacting later on with saves/loads

	public SharedDataLists(Settings config, GameItems GI) {
		this.gameItems = GI;

		map_index = 0;

		String mapstr = config.get("mapseq", "M2.bmp");
		String maps_folder = config.get("mapfolder", "maps");
		String[] maps = mapstr.split(",");

		map_list = new Map[maps.length/2];//There're two ','s for every map.
		int fetch = 0;
		for (int i = 0; i < map_list.length; i++) {
			map_list[i] = new Map(String.format("%s/%s", maps_folder, maps[fetch++]), Integer.parseInt(maps[fetch++]));
		}
	}

	public void new_level(){
		if(map_list[map_index].bonus) map_index++;//THIS MIGHT NEED AN UPDATE WHEN WORKING ON BONUS LEVELS. ALSO CAPS LOCK OP.
		else map_index = map_list[map_index].next_map_index;//case where next map is -1 not used yet

		Point p = map_list[map_index].player_starting_coords;
		Actor.shape = new Rectangle(p.x, p.y, Actor.width, Actor.height);
		Actor.setCoords(p.x - 300, p.y - 300);

		initialize_monsters();
	}

	/** monster objects to paint and act with */
	public void initialize_monsters(){
		Map map = map_list[map_index];

		int mobs_amount = map.monster_coords.size();
		map.mobs_in_map = new ArrayList<Monster>();

		for (int i = 0; i < mobs_amount; i++) {
			//each i stands for a different mob in the current map.
			map.mobs_in_map.add(new Monster(this,
					new CharStats("monsta", 100, 30, 0, 1, false), i));
		}
	}

	//can be used as a save point later on
	/*public ... load_map(...){...}*/
	/*public ... save_map(...){...}*/
}


