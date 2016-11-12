package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RenderComponent extends Component {
	public RenderComponent(BufferedImage texture, int width, int height){
		super(RENDER);
		this.texture = texture;
		this.width = width;
		this.height = height;
	}
	
	public RenderComponent(String path, int width, int height) throws IOException{
		super(RENDER);
		this.texture = ImageIO.read(new File(path));
		this.width = width;
		this.height = height;
	}

	BufferedImage texture;
	int width;
	int height;
}
