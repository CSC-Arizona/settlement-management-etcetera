package run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

public class Game extends Thread implements Serializable {

  public Game(BufferedImage renderDest, JFrame frame, JScrollPane scrollPane) {
    this.renderDest = renderDest;
    //this.label = label;
    this.frame = frame;
    this.infoPanel = InfoPanel.getInstance();
    eMan = EntityManager.INSTANCE;
    userClickVector = (Vec2f) null;
    this.scrollPane = scrollPane;
  }

  public void setBackground(BufferedImage renderDest, JFrame frame, JScrollPane scrollPane) {
    this.renderDest = renderDest;
    //this.label = label;
    this.frame = frame;
    this.scrollPane = scrollPane;
  }

  public void spawnAliens(int num) {
    World w = World.getWorld();
    Random r = new Random();
    for (int i = 0; i < num; ++i) {
      int x = r.nextInt(World.WORLD_SIZE - 1);
      int y = r.nextInt(World.WORLD_SIZE - 1);
      while (!w.getTile(y, x).isPassable()) {
        x = r.nextInt(World.WORLD_SIZE - 1);
        y = r.nextInt(World.WORLD_SIZE - 1);
      }
      EntityFactory.makeNewAlien(x, y);
    }
  }

  public void run() {
	scrollPane.getViewport().getView().addMouseListener(new ClickListener());
    frame.addKeyListener(new KeyboardListener());
    
    commands = new Stack<Command>();
    World w = World.getWorld();
    Random r = new Random();
    aliensToAdd = 0;

    Graphics g = renderDest.getGraphics();
    RenderSystem rs = new RenderSystem(g);
    PhysicsSystem ps = new PhysicsSystem();
    AISystem as = new AISystem();
    LivingSystem ls = new LivingSystem();
    CommandSystem cs = new CommandSystem();
    MessageSystem ms = new MessageSystem();

    EntityFactory.makeNewShip(1, 1);
    // We want to have 30 ticks/s
    int goal = 30;
    long milPerTick = (long) ((1.0f / goal) * 1000);
    for (;;) {
      long startMil = System.currentTimeMillis();
      w.render(g);
      rs.tick();

      ps.tick();
      cs.addCommands(commands);
      commands.clear();
      cs.tick();
      //ms.tick();
      as.tick();
      ls.tick();
      frame.repaint();
      ms.tick();
      if(userClickVector == null)
    	  infoPanel.updatePanel();
      else{
    	  Entity entity = eMan.getTopEntityAt(userClickVector);
    	  Sprite sprite = w.getTile((int)userClickVector.y, (int)userClickVector.x).getType();
    	  infoPanel.setModelEntitySprite(entity, sprite);
    	  userClickVector = null;
      }
      
      for(; aliensToAdd > 0; --aliensToAdd)
      	spawnAliens(1);

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
    public void mousePressed(MouseEvent e) {
      World w = World.getWorld();
      if(e.getButton() == MouseEvent.BUTTON1){
        // if(tile is a tree)
        if(w.getTile(e.getY() / Sprite.HEIGHT, e.getX() / Sprite.WIDTH).getType() == Sprite.TREE)
        	commands.push(new Command(Command.Type.CHOP_TREE,
                    new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT), System.currentTimeMillis()));
        else if(w.getTile(e.getY() / Sprite.HEIGHT, e.getX() / Sprite.WIDTH).isPassable())
        	commands.push(new Command(Command.Type.RELOCATE,
                    new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT), System.currentTimeMillis()));
      }
      else if(e.getButton() == MouseEvent.BUTTON3){ // this might need to be a 2    	  
    	  userClickVector = new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT);

      }else if(e.getButton()==MouseEvent.BUTTON2){
        if(World.getWorld().getTile(e.getX()/32, e.getY()/32).getType()==Sprite.DIRT){
        	commands.push(new Command(Command.Type.BUILD_HOUSE,
                    new Vec2f(e.getX() / Sprite.WIDTH, e.getY() / Sprite.HEIGHT), System.currentTimeMillis()));
        }
      }
    }
  }
  
  private class KeyboardListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_N){
				++aliensToAdd;
			}
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
  	
  }
  

  
  private int aliensToAdd;
  private Stack<Command> commands;
  private transient BufferedImage renderDest;
  //private JPanel frame;
  private JFrame frame;
  private InfoPanel infoPanel;
  private static EntityManager eMan;
  private Vec2f userClickVector;
  private JScrollPane scrollPane;

}

