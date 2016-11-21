/**
 * Abstract Structure class which all of the structure objects are 
 * based on. 
 * 
 * @author Tanner Bernth
 */
package world;

import java.awt.Image;
import java.io.Serializable;

public abstract class Structure implements Serializable {
	
	protected Structure[][] formation;
	private boolean passable;
	private int id;
	private Image image; // Images need to be handled by the gui
	
	/*
	 * Constructor
	 */
	public Structure(int row, int col) {
		formation = new Structure[row][col];
		passable = true; // Structures are passable by default
		id = -1;
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

	/*
	 * Initializes the formation array.
	 */
	protected abstract void setFormation();
	
	/*
	 * Inserts the formation array into the given board.
	 */
	public abstract Structure[][] insertFormation(Structure[][] board, int row, int col);
	
}
