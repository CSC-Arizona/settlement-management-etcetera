/**
 * Abstract Structure class which all of the structure objects are 
 * based on. 
 * 
 * @author Tanner Bernth
 */
package world;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import utility.Sprite;
import utility.Tileset;

public abstract class Structure implements Serializable {
	
	protected Structure[][] formation;
	private boolean passable;
	private int id;
	BufferedImage texture;
	Sprite s;
	
	/*
	 * Constructor
	 */
	public Structure(int row, int col, Sprite s) {
		formation = new Structure[row][col];
		passable = true; // Structures are passable by default
		this.s = s;
		id = -1;
    Tileset tileset = Tileset.instance();
    texture = tileset.getSprite(s);
	}
	
	public Sprite getType() {
		return s;
	}
	
	/*
	 * Updates the passable flag with the given boolean value.
	 */
	public void setPassable(boolean passable) {
		this.passable = passable;
	}
	
	/*
	 * Returns the formation array.
	 */
	public Structure[][] getFormation() {
		return formation;
	}
	
	/*
	 * Sets the id of the object.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/*
	 * Returns the id of the object.
	 */
	public int getId() {
		return id;
	}
	
	/*
	 * Toggles the passable boolean flag.
	 * if (true) passable = false;
	 * else passable = true;
	 */
	public void togglePassable() {
		passable = passable ? false : true;
	}

  public boolean isPassable(){
    return passable;
  }

	/*
	 * Initializes the formation array.
	 */
	protected abstract void setFormation();
	
	/*
	 * Inserts the formation array into the given board.
	 */
	public abstract Structure[][] insertFormation(Structure[][] board, int row, int col);
	
}
