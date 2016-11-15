package entities;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class EntitySystemTest {

  @Test
  public void test() {
    EntityManager eMan = EntityManager.INSTANCE;
    EntityFactory.makeNewHunter(5.0f, 5.0f);
    EntityFactory.makeNewHunter(15.0f, 5.0f);
    EntityFactory.makeNewHunter(25.0f, 15.0f);
    Entity randomHunter = EntityFactory.makeNewHunter(5.0f, 15.0f);

    EntityFactory.makeNewGhost(7.0f, 8.0f);
    EntityFactory.makeNewGhost(9.0f, 2.0f);
    EntityFactory.makeNewGhost(1.0f, 1.0f);

    EntityFactory.makeNewRock(8.0f, 9.0f);
    EntityFactory.makeNewRock(18.0f, 6.0f);
    EntityFactory.makeNewRock(3.0f, 4.0f);
    EntityFactory.makeNewRock(15.0f, 7.0f);
    Entity randomRock = EntityFactory.makeNewRock(8.0f, 14.0f);
    EntityFactory.makeNewRock(9.0f, 13.0f);

    EntityFactory.makeNewFlower(3.0f, 3.0f);
    EntityFactory.makeNewFlower(8.0f, 13.0f);
    EntityFactory.makeNewFlower(4.0f, 5.0f);
    
    Entity rock = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(), rock);
    try{
      eMan.addComponent(new RenderComponent(ImageIO.read(new File("gfx/rock.png")), 32, 32), rock);
    }catch(IOException e){
      e.printStackTrace();
    }
    eMan.addComponent(new MobilityComponent(), rock);
    eMan.addComponent(new CollisionComponent(0.5f, 0.5f, 3.1f), rock);
    
    int oldSize = eMan.getUsed();
    eMan.rmEntity(rock);
    EntityFactory.makeNewFlower(0.0f, 0.0f);
    int newSize = eMan.getUsed();
    
    assertEquals(oldSize, newSize);
    
    oldSize = newSize;
    eMan.rmEntity(randomHunter);
    newSize = eMan.getUsed();
    assertEquals(oldSize, newSize);
    EntityFactory.makeNewFlower(0.0f, 0.0f);
    newSize = eMan.getUsed();
    assertEquals(oldSize, newSize);
    
    PositionComponent rrp = (PositionComponent)eMan.getCompVec(Component.POSITION).get(randomRock.getID());
    assertEquals(rrp.x, 8.0f, 0.001);
    assertEquals(rrp.y, 14.0f, 0.001);

   // assertTrue(eMan.hasComponents(Component.RENDER, e));
    RenderSystem rs = new RenderSystem(new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB).getGraphics());
    PhysicsSystem ps = new PhysicsSystem();
    AISystem ais = new AISystem();
    for(int i = 0; i < 1000; ++i){
        rs.tick();
        ps.tick();
        ais.tick();
    }
  }

}