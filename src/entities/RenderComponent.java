package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utility.Sprite;
import utility.Tileset;

public class RenderComponent extends Component {

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

  public RenderComponent(Sprite s){
    super(RENDER);
    Tileset tileset = Tileset.instance();
    texture = tileset.getSprite(s);
    this.width = s.getWidth();
    this.height = s.getHeight();
  }

  BufferedImage texture;
  int width;
  int height;
}
