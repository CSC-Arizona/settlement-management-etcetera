/**
 * Randomly generates the world using the Random java class.
 * Each structure has a different chance of being placed
 * on the board.
 * 
 * @author Tanner Bernth
 */
package world;

import java.util.Random;

public class WorldGenerator {

	private Random rng;
	private static StructureManager sMan;

	/*
	 * Constructor
	 */
	public WorldGenerator() {
		rng = new Random();
	}

	/*
	 * Initializes the board to all Dirt objects.
	 */
	private static Structure[][] initializeBoard(Structure[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = sMan.Dirt.getStructure();
			}
		}
		return board;
	}

	/*
	 * Randomly generates the world by inserting objects into the
	 * board based on the randomly generated number.
	 */
	public static Structure[][] generateWorld(Structure[][] board) {
		Random rng = new Random();
		board = initializeBoard(board);
		int r = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				r = rng.nextInt(4 * 25);
				if(r > 1 && r <= 6){
					board[i][j] = sMan.Hole.getStructure();
				}else if (r > 6 && r <= 11) {
					board[i][j] = sMan.Rock.getStructure();
				} else if (r > 11 && r <= 31) {
					board[i][j] = sMan.Grass.getStructure(); // 10%
				} else if (r > 31 && r <= 41) {
					board[i][j] = sMan.Tree.getStructure(); // 20%
				} else if (r > 41 && r <= 46) {
					board[i][j] = sMan.Bush.getStructure(); // 5%
				} else {
					board[i][j] = sMan.Dirt.getStructure(); // 65%
				}
			}
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				r = rng.nextInt(1000);
				if (r == 0) {
					board = sMan.Crater.insertFormation(board, i, j);
				} else if (r > 0 && r < 30) {
					board = sMan.Lake.insertFormation(board, i, j);
				}
			}
		}
		return board;
	}
	
	public static void generateCrater(Structure[][] board) {
		Random rng = new Random();
		int r = 0,
			craterCount = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				r = rng.nextInt(300);
				if (r == 0) {
					board = sMan.Crater.insertFormation(board, i, j);
					craterCount++;
				}
				if (craterCount == 2) {
					return;
				}
			}
		}
	}

}
