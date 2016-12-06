package utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tileset {

  private static final String TILESET_PATH = "gfx/tiles.png";
  private static Tileset instance = null;
  private transient static BufferedImage tileset;

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

  public BufferedImage getSprite(Sprite s){
	if(s==Sprite.ALIEN){
		return tileset.getSubimage(s.getX(), s.getY(), Sprite.WIDTH, Sprite.HEIGHT);
	}
	// load source images
	BufferedImage image = tileset.getSubimage(0, 40*32, Sprite.WIDTH, Sprite.HEIGHT);
	BufferedImage overlay = tileset.getSubimage(s.getX(), s.getY(), Sprite.WIDTH, Sprite.HEIGHT);

	// create the new image, canvas size is the max. of both image sizes
	int w = Math.max(image.getWidth(), overlay.getWidth());
	int h = Math.max(image.getHeight(), overlay.getHeight());
	BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	// paint both images, preserving the alpha channels
	Graphics g = combined.getGraphics();
	g.drawImage(image, 0, 0, null);
	g.drawImage(overlay, 0, 0, null);
	
	g.dispose();
	  
    return combined;
  }

}
