package run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.JLabel;

import entities.Entity;
import entities.EntityFactory;
import entities.EntityManager;
import entities.Component;
import entities.MobilityComponent;
import entities.PositionComponent;
import entities.RenderComponent;
import entities.CollisionComponent;
import entities.Command;
import entities.Command.Type;
import entities.AIComponent;

import entities.RenderSystem;
import entities.PhysicsSystem;
import entities.AISystem;
import entities.LivingSystem;
import utility.Vec2f;
import world.World;

public class Game extends Thread {

  public Game(BufferedImage renderDest, JLabel label) {
    this.renderDest = renderDest;
    this.label = label;
  }

  public void run() {
  	/*
    EntityFactory.makeNewGodzilla(5.0f, 5.0f);
    EntityFactory.makeNewGodzilla(15.0f, 5.0f);
    EntityFactory.makeNewGodzilla(15.0f, 15.0f);
    EntityFactory.makeNewGodzilla(5.0f, 15.0f);

    EntityFactory.makeNewAlien(7.0f, 8.0f);
    EntityFactory.makeNewAlien(9.0f, 2.0f);
    EntityFactory.makeNewAlien(1.0f, 1.0f);

    EntityFactory.makeNewAmmo(3.0f, 9.0f);
    for(int i = 4; i < 18; i++){
    	EntityFactory.makeNewAmmo(i, 9.0f);
    }
    EntityFactory.makeNewAmmo(18.0f, 6.0f);
    EntityFactory.makeNewAmmo(3.0f, 4.0f);

    EntityFactory.makeNewBandage(3.0f, 3.0f);
    EntityFactory.makeNewBandage(8.0f, 13.0f);
    EntityFactory.makeNewBandage(4.0f, 5.0f);
*/
	label.addMouseListener(new ClickListener());
    World w = World.getWorld();
    Random r = new Random();
  	for(int i = 0; i < 5; ++i){
  		int x = r.nextInt(World.WORLD_SIZE - 1);
  		int y = r.nextInt(World.WORLD_SIZE - 1);
  		while(!w.getTile(y, x).isPassable()){
  			x = r.nextInt(World.WORLD_SIZE - 1);
  			y = r.nextInt(World.WORLD_SIZE - 1);
  		}
  		EntityFactory.makeNewAlien(x, y);
  	}
  	EntityFactory.makeNewAmmo(0.0f, 0.0f);
  	Graphics g = renderDest.getGraphics();
    RenderSystem rs = new RenderSystem(g);
    PhysicsSystem ps = new PhysicsSystem();
    AISystem as = new AISystem();
    LivingSystem ls = new LivingSystem();
    
    // We want to have 30 ticks/s
    int goal = 30;
    long milPerTick = (long) ((1.0f / goal) * 1000);
    for (;;) {
      long startMil = System.currentTimeMillis();

      g.setColor(Color.GRAY);
      g.fillRect(0, 0, renderDest.getWidth(), renderDest.getHeight());
      w.render(g);
      rs.tick();
      ps.tick();
      as.tick();
      ls.tick();
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

  private class ClickListener extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			// if(tile is a tree)
			new Command(Type.CHOP_TREE, new Vec2f(e.getX()/32, e.getY()/32));
			System.out.println("you clicked a thing");
		}
		else if(e.getButton() == MouseEvent.BUTTON3){ // this might need to be a 2
			new Command(Type.RELOCATE, new Vec2f(e.getX()/32, e.getY()/32));
		}
		else if(e.getButton()==MouseEvent.BUTTON2){
			EntityFactory.makeNewHouse(e.getX()/32, e.getY()/32);
		}
	}
	
  }
  
  BufferedImage renderDest;
  JLabel label;
}
