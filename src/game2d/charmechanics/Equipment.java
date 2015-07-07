package game2d.charmechanics;

public class Equipment{
	/**
	 * 0: head
	 * <br>1: arms
	 * <br>2: chest
	 * <br>3: legs
	 * <br>4: neck
	 * <br>5: ring*/
	private int slot;
	private String name;

	public Equipment(String name, int slot){
		this.name = name;
		this.slot = slot;
	}
}
