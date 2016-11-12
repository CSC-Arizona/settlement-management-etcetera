package entities;

import java.awt.image.BufferedImage;

public class RenderComponent extends Component {
	public RenderComponent(BufferedImage texture, int width, int height){
		super(RENDER);
		this.texture = texture;
		this.width = width;
		this.height = height;
	}

	BufferedImage texture;
	int width;
	int height;
}

