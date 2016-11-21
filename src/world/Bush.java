/**
 * Abstract Structure Object to be used in the world creation.
 * 
 * @author Tanner Bernth
 */

package world;

public class Bush extends Structure {
		
	/*
	 * Constructor
	 */
	public Bush(int row, int col) {
		super(row, col);
		setId(3);
		togglePassable();
	}

	/*
	 * Initializes the formation array.
	 */
	@Override
	protected void setFormation() {
		for (int i = 0; i < formation.length; i++) {
			for (int j = 0; j < formation[i].length; j++) {
					formation[i][j] = this;
			}
		}
	}

	/*
	 * Inserts the formation array into the given board.
	 */
	@Override
	public Structure[][] insertFormation(Structure[][] board, int row, int col) {
		for (int i = 0; i < formation.length; i++) {
			for (int j = 0; j < formation[i].length; j++) {
					board[(i + row + board.length) % board.length][(j + col + board[i].length)
							% board[i].length] = formation[i][j];
			}
		}
		return board;
	}
}