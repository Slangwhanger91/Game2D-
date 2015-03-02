import java.awt.Rectangle;


public class Map_List {

	private final int ALL_MAPS = 3;
	Maps map_list[];
	public int map_index;//Should be interacting later on with saves/loads

	public Map_List() {
		map_index = 0;
		map_list = new Maps[ALL_MAPS];

		map_list[0] = new Maps("maps/M2.bmp", 2);/*which means that the map in index  
												 0 leads to the map in index 2, even
												 indexes will lead into main levels 
												 while the uneven ones (such as 1 for
												 this map) will take you to bonus 
												 levels if all the requirements are met.
												 (side levels - ??)*/ 
		map_list[1] = new Maps("maps/M1.txt");//bonus map
		map_list[1].next_map_index = 2;

		map_list[2] = new Maps("maps/M3.bmp", 0);//this is the last map (-1)
	}

    public Map_List(Settings config) {
        map_index = 0;

        String mapstr = config.get("mapseq", "M2.bmp");
        String maps_folder = config.get("mapfolder", "maps");
        String[] maps = mapstr.split(",");

        map_list = new Maps[maps.length];
        int map_magic = maps.length-1;
        for (int i = 0; i < maps.length; i++) {
            System.out.println(maps[i]);
            map_list[i] = new Maps(String.format("%s/%s", maps_folder, maps[i]), map_magic--);
        }
    }

	public void new_level(Player Actor){
		if(map_list[map_index].bonus) map_index++;
		else map_index = map_list[map_index].next_map_index;//case where next map is -1 not used yet

		int[] pc = map_list[map_index].player_coords;
		Actor.shape = new Rectangle(pc[0], pc[1], Actor.width, Actor.height);
		Actor.y_coord = pc[1] - 300;//magical numbers from wonderland
		Actor.x_coord = pc[0] - 300;

		initialize_monsters();
	}

	/** monster objects to paint and act with */
	public void initialize_monsters(){
		Maps M = map_list[map_index];
		
		int mobs_amount = M.monster_coords.size();
		M.mobs_in_map = new monsters[mobs_amount];
		
		for (int i = 0; i < M.mobs_in_map.length; i++) {
			M.mobs_in_map[i] = new monsters(M, new Char_stats("monsta", 100, 30, 0, 15), i);	
		}
	}

	//can be used as a save point later on
	/*public ... load_map(...){...}*/
	/*public ... save_map(...){...}*/
}
