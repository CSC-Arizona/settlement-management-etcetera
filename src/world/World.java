/**
 * The World is a singleton class that holds the 2D array for 
 * the entire game. The world is randomly generated with 
 * multiple, different structure objects.
 * 
 * @author Tanner Bernth
 */
package world;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

import utility.Sprite;

public class World implements Serializable {
	
	public static final int WORLD_SIZE = 50;//1000;
	static final int TILE_COUNT = 2;
	static final int TILE_MULTIPLIER = 5;	
	/*-- For testing purposes --*/
  /*
	public static void main(String[] args) {
		//World world = World.getWorld();
		//System.out.println(world);
	}
	*/

	public String toString() {
		String world = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null) {
					switch (board[i][j].getId()) {
					case 0:
						world += "[ ROCK ] ";
						break;
					case 1:
						world += "[GRASS ] ";
						break;
					case 2:
						world += "[ TREE ] ";
						break;
					case 3:
						world += "[ BUSH ] ";
						break;
					case 4:
						world += "[ DIRT ] ";
						break;
					case 5:
						world += "[CRATER] ";
						break;
					case 6:
						world += "[WATER ] ";
						break;
					default:
						world += "[  0   ] ";
						break;
					}
				} else {
					world += "[ null ] "; 
				}
				// world += "[ " + board[i][j][0].getId() + " ] ";
			}
			world += "\n";
		}
		return world;
	}
	/* ------------------------ */

	protected static World world;
	private Structure[][] board;

	/*
	 * Singleton Constructor
	 */
	private World() {
		board = new Structure[WORLD_SIZE][WORLD_SIZE];
		board = WorldGenerator.generateWorld(board);
	}

	/*
	 * Returns the world. Creates a new world if a world does not already exist.
	 */
	public static World getWorld() {
		if (world == null)
			world = new World();
		return world;
	}
	
	/*
	 * 
	 */
	public static void setWorld(World newWorld) {
		world = newWorld;
	}
	
	/*
	 * Returns the size of the world.
	 */
	public int getSize() {
		return WORLD_SIZE;
	}

	/*
	 * Returns the selected tile.
	 */
	public Structure getTile(int row, int col) {
		return board[row][col];
	}

	/*
	 * Sets the selected tile to the given value.
	 */
	public void setTile(int row, int col, Structure val) {
		board[row][col] = val;
	}
	
	/*
	 * Returns the board.
	 */
	public Structure[][] getBoard() {
		return board;
	}

  public void render(Graphics g){
    for(int j = 0; j < WORLD_SIZE; ++j){
      for(int k = 0; k < WORLD_SIZE; ++k){
        g.drawImage(board[j][k].getTexture(), k * Sprite.WIDTH,
                    j * Sprite.HEIGHT, null);
      }
    }
  }

}
