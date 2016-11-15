package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// TODO: ensure tileset is a singleton

public class RenderComponent extends Component {

	private static final String TILESET_PATH = "gfx/tiles.png";

	public RenderComponent(BufferedImage texture, int width, int height) {
		super(RENDER);
		this.texture = texture;
		this.width = width;
		this.height = height;
	}

	public RenderComponent(String path, int width, int height) {
		super(RENDER);
		try {
			this.texture = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.width = width;
		this.height = height;
	}

	public RenderComponent(String pic){
		super(RENDER);
		// TODO: parse the JSON file and get the picture coordinates
		// instead of hard-coding it
		try{
			BufferedImage tileset = ImageIO.read(new File(TILESET_PATH));
			switch(pic){
			case "alien":
				this.texture = tileset.getSubimage(7*32, 15*32, 32, 32);
				break;
			case "ammo":
				this.texture = tileset.getSubimage(32, 32, 32, 32);
				break;
			case "godzilla":
				this.texture = tileset.getSubimage(4*32, 19*32, 32, 32);
				break;
			case "bandage":
				this.texture = tileset.getSubimage(0, 13*32, 32, 32);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.width = 32;
		this.height = 32;
	}

	BufferedImage texture;
	int width;
	int height;
}
