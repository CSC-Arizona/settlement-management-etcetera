package world;

import java.io.Serializable;
import java.util.Random;

public class World implements Serializable {
	
	public static final int WORLD_SIZE = 20;//1000;
	static final int TILE_COUNT = 2;
	static final int TILE_MULTIPLIER = 5;
	
	/*-- For testing purposes --*/
	public static void main(String [] args) {
		World world = World.getWorld();
		System.out.println(world);
	}
	
	
	public String toString() {
		String world = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				/*switch(board[i][j]) {
				case 0:
					world += "[10%] ";
					break;
				case 1:
					world += "[   ] ";
					break;
				case 2:
					world += "[20%] ";
					break;
				default:
					world += "[50%] ";
					break;
					
				}*/
				world += "[ " + board[i][j] + " ] ";
			}
			world += "\n";
		}
		return world;
	}
	/* ------------------------ */
	
	protected static World world;
	private int[][] board;
	
	/*
	 * Singleton Constructor
	 */
	private World() {
		board = new int[WORLD_SIZE][WORLD_SIZE];
		this.generateTerrain();
	}
	
	/*
	 * Returns the world. Creates a new world if a world does
	 * not already exist.
	 */
	public static World getWorld() {
		if (world == null) 
			world = new World();
		return world;
	}
	
	/*
	 * Fills the world with randomly generated Tiles.
	 * Currently:
	 * 10% chance for 0
	 * 20% chance for 1
	 * 20% chance for 2
	 * 50% chance for 3
	 */
	private void generateTerrain() {
		Random rng = new Random();
		int r = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				r = rng.nextInt(TILE_COUNT*TILE_MULTIPLIER);
				if (r == 0) board[i][j] = 0;
				else if (r == 1 || r == 2) board[i][j] = 1;
				else if (r == 3 || r == 4) board[i][j] = 2;
				else board[i][j] = 3;
			}
		}
	}
	
	/*
	 * Returns the selected tile.
	 */
	public int getTile(int x, int y) {
		return board[y][x];
	}
	
	/*
	 * Sets the selected tile to the given value.
	 */
	public void setTile(int x, int y, int val) {
		board[y][x] = val;
	}
	
}
