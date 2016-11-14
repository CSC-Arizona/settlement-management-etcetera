/*
 * Assumes all entity textures are perfect squares of same size. Assumes
 * entity coordinates start at (0,0) at the top left corner.
 */
package entities;

import java.awt.Graphics;

public class RenderSystem extends System {
  public RenderSystem(Graphics g) {
    super(Component.POSITION | Component.RENDER);
    this.g = g;
  }

  public void tick() {
    updateEntityVector();
    for (Entity e : entitiesToProcess) {
      RenderComponent rc;
      PositionComponent pc;
      rc = (RenderComponent) eManager.getComponent(Component.RENDER, e);
      pc = (PositionComponent) eManager.getComponent(Component.POSITION, e);

      // ppm = pixels per meter
      final int ppm = rc.width;
      g.drawImage(rc.texture, (int) (pc.x * ppm) - (rc.width / 2), (int) (pc.y * ppm) - (rc.width / 2), rc.width,
          rc.height, null);
    }
  }

  private Graphics g;
}
