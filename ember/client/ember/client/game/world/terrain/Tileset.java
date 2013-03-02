package ember.client.game.world.terrain;

import ember.client.Client;

public class Tileset {

	private int w;
	private int h;
	public Tile[][] tiles;

	/**
	 * Create a new, blank terrain tile set.
	 * @param width
	 * @param height
	 */
	public Tileset(int width, int height){
		w=width;
		h=height;
		
		tiles = new Tile[w][h];
		
	}
	
	public static int width(){
		
		return Client.getInstance().game.world.getTileset().w;
	}
	
	public static int height(){
		
		return Client.getInstance().game.world.getTileset().h;
	}
	
	
	
	public Tile getTile(int x, int y){
		//TODO bounds checking. W and H against X and Y
		return tiles[x][y];
	}
	
}
