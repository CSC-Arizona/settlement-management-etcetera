package run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;

import entities.Entity;
import entities.EntityFactory;
import entities.EntityManager;
import entities.MobilityComponent;
import entities.PhysicsSystem;
import entities.PositionComponent;
import entities.RenderComponent;
import entities.RenderSystem;
import entities.Vec2f;
import entities.AIComponent;
import entities.AISystem;
import entities.CollisionComponent;
import entities.Component;

public class Game extends Thread {

  public Game(BufferedImage renderDest, JLabel label) {
    this.renderDest = renderDest;
    this.label = label;
  }

  public void run() {
    EntityFactory.makeNewGodzilla(5.0f, 5.0f);
    EntityFactory.makeNewGodzilla(15.0f, 5.0f);
    EntityFactory.makeNewGodzilla(25.0f, 15.0f);
    EntityFactory.makeNewGodzilla(5.0f, 15.0f);

    EntityFactory.makeNewAlien(7.0f, 8.0f);
    EntityFactory.makeNewAlien(9.0f, 2.0f);
    EntityFactory.makeNewAlien(1.0f, 1.0f);

    EntityFactory.makeNewAmmo(8.0f, 9.0f);
    EntityFactory.makeNewAmmo(18.0f, 6.0f);
    EntityFactory.makeNewAmmo(3.0f, 4.0f);

    EntityFactory.makeNewBandage(3.0f, 3.0f);
    EntityFactory.makeNewBandage(8.0f, 13.0f);
    EntityFactory.makeNewBandage(4.0f, 5.0f);

    Graphics g = renderDest.getGraphics();
    RenderSystem rs = new RenderSystem(g);
    PhysicsSystem ps = new PhysicsSystem();
    AISystem ais = new AISystem();

    // We want to have 30 ticks/s
    int goal = 30;
    long milPerTick = (long) ((1.0f / goal) * 1000);
    for (;;) {
      long startMil = System.currentTimeMillis();

      g.setColor(Color.GRAY);
      g.fillRect(0, 0, renderDest.getWidth(), renderDest.getHeight());
      rs.tick();
      ps.tick();
      ais.tick();
      label.repaint();

      long endMil = System.currentTimeMillis();
      if (endMil - startMil < milPerTick) {
        try {
          Thread.sleep(milPerTick - (endMil - startMil));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  BufferedImage renderDest;
  JLabel label;
}
