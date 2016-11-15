package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tileset {

	private static final String TILESET_PATH = "gfx/tiles.png";
	private static Tileset instance = null;
	private static BufferedImage tileset;

	public static Tileset instance() {
		if(instance == null) {
			instance = new Tileset();
		}
		return instance;
	}

	protected Tileset() {
		try {
			tileset = ImageIO.read(new File(TILESET_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage tileset(){
		return tileset;
	}

}
