/*
 * Assumes all entity textures are perfect squares of same size. Assumes
 * entity coordinates start at (0,0) at the top left corner.
 */
package entities;

import java.awt.Graphics;
import java.awt.Color;

public class RenderSystem extends System{
  public RenderSystem(Graphics g) {
    super(Component.POSITION | Component.RENDER);
    this.g = g;
  }

  public void tick() {
    updateEntityVector();
    for (Entity e : entitiesToProcess) {
      RenderComponent rc;
      PositionComponent pc;
      LivingComponent lc;
      rc = (RenderComponent) eManager.getComponent(Component.RENDER, e);
      pc = (PositionComponent) eManager.getComponent(Component.POSITION, e);
      lc = (LivingComponent) eManager.getComponent(Component.LIVING, e);

      // ppm = pixels per meter
      final int ppm = rc.width;
      int x = (int)(pc.pos.x * ppm);// - (rc.width / 2);
      int y = (int)(pc.pos.y * ppm);// - (rc.height / 2);
      g.drawImage(/*rc.texture*/rc.getTexture(), x, y, rc.width, rc.height, null);

      // Draw HP bar.
      if(lc != null){
        g.setColor(Color.BLACK);
        g.drawRect(x + 1, y - 2, rc.width, 5);
        int hpBarLength = (int)((lc.HP / lc.maxHP) * 32.0f);
        g.setColor(Color.RED);
        g.fillRect(x + 2, y - 1, hpBarLength - 2, 3);
      }
    }
  }

  private Graphics g;
}
