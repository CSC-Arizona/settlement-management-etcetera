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
    /*
    BufferedImage tileset = Tileset.instance().tileset();
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
    this.width = 32;
    this.height = 32;
    */
  }

  BufferedImage texture;
  int width;
  int height;
}
