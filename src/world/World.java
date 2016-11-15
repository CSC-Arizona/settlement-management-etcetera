package world;

import java.io.Serializable;
import java.util.Random;

public class World implements Serializable {
	
	static final int WORLD_SIZE = 20;//1000;
	static final int TILE_COUNT = 2;
	static final int TILE_MULTIPLIER = 5;
	
	/*-- For testing purposes --*/
	public static void main(String [] args) {
		World world = World.getWorld();
		world.printWorld();
	}
	
	private void printWorld() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print("[ " + board[i][j] + " ] ");
			}
			System.out.println();
		}
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
	 * 90% chance for 1
	 */
	private void generateTerrain() {
		Random rng = new Random();
		int random = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				random = rng.nextInt(TILE_COUNT*TILE_MULTIPLIER);
				if (random == 0) board[i][j] = 0;
				else board[i][j] = 1;
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
