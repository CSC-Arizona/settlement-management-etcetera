/**
 * Enum used for the abstract structure objects. Makes
 * it easier to access all of the structures.
 * @author Tanner Bernth
 */

package world;

public enum StructureManager {
	Rock(new Rock(1,1)),
	Grass(new Grass(1,1)),
	Tree(new Tree(1,1)),
	Bush(new Bush(1,1)),
	Dirt(new Dirt(1,1)),
	Crater(new Crater(5,5)),
	Lake(new Lake(2,3));
	
	private Structure structure;
	
	/*
	 * Constructor
	 */
	StructureManager(Structure structure) {
		this.structure = structure;
	}
	
	/*
	 * Returns the structure object
	 */
	public Structure getStructure() {
		return structure;
	}
	
	/*
	 * Calls the insertFormation method of the selected Structure.
	 */
	public Structure[][] insertFormation(Structure[][] board, int row, int col) {
		return structure.insertFormation(board, row, col);
	}

}
