package run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

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
import entities.CommandSystem;
import entities.State;
import entities.AIComponent;

import entities.RenderSystem;
import entities.PhysicsSystem;
import entities.AISystem;
import entities.LivingSystem;
import entities.MessageSystem;
import utility.Sprite;
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
  	commands = new Stack<Command>();
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
  	Graphics g = renderDest.getGraphics();
    RenderSystem rs = new RenderSystem(g);
    PhysicsSystem ps = new PhysicsSystem();
    AISystem as = new AISystem();
    LivingSystem ls = new LivingSystem();
    CommandSystem cs = new CommandSystem();
    MessageSystem ms = new MessageSystem();

  	EntityFactory.makeNewShip(1,1);
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
      cs.addCommands(commands);
      commands.clear();
      cs.tick();
      as.tick();
      ls.tick();
      label.repaint();

      ms.tick();
      Vector<String> messages = ms.getMessages();
      //for(String s : messages)
        //java.lang.System.out.println(s);

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
      World w = World.getWorld();
      if(e.getButton() == MouseEvent.BUTTON1){
        // if(tile is a tree)
        if(w.getTile(e.getY() / Sprite.HEIGHT, e.getX() / Sprite.WIDTH).getType() == Sprite.TREE){
          commands.push(new Command(Command.Type.CHOP_TREE,
          		new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT), System.currentTimeMillis()));
        }
        //System.out.println("CHOP_TREE " + e.getX() / 32 + ", " + e.getY() / 32);
      }
      else if(e.getButton() == MouseEvent.BUTTON3){ // this might need to be a 2
        if(w.getTile(e.getY() / Sprite.HEIGHT, e.getX() / Sprite.WIDTH).isPassable()){
          commands.push(new Command(Command.Type.RELOCATE,
          		new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT), System.currentTimeMillis()));
        }
        //System.out.println("RELOCATE" + e.getX() / 32 + ", " + e.getY() / 32);
      }else if(e.getButton()==MouseEvent.BUTTON2){
        if(World.getWorld().getTile(e.getX()/32, e.getY()/32).getType()==Sprite.DIRT){
          commands.push(new Command(Command.Type.BUILD_HOUSE,
          		new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT), System.currentTimeMillis()));
        }
      }
    }
  }
  
  private Stack<Command> commands;
  private BufferedImage renderDest;
  private JLabel label;
}
