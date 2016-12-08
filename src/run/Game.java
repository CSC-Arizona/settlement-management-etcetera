package run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

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
import entities.AnimalSystem;
import entities.LivingSystem;
import entities.MessageSystem;
import utility.Sprite;
import utility.Vec2f;
import world.World;

public class Game extends Thread implements Serializable {

	public Game(BufferedImage renderDest, JFrame frame, JScrollPane scrollPane) {
		this.renderDest = renderDest;
		// this.label = label;
		this.frame = frame;
		this.infoPanel = InfoPanel.getInstance();
		eMan = EntityManager.INSTANCE;
		userClickVector = (Vec2f) null;
		this.scrollPane = scrollPane;
		button1Char = 'A';
	}

	public void setBackground(BufferedImage renderDest, JFrame frame, JScrollPane scrollPane) {
		this.renderDest = renderDest;
		// this.label = label;
		this.frame = frame;
		this.scrollPane = scrollPane;
	}

	public void spawnAliens(int num) {
		World w = World.getWorld();
		Random r = new Random();
		for (int i = 0; i < num; ++i) {
			Point p = getRandomFreeLocation();
			EntityFactory.makeNewAlien(p.x, p.y);
		}
	}

	public void earthquake(LivingSystem ls, World w, AnimalSystem anc, Graphics g) {
		ls.earthquake();
		w.earthquake(g);
		anc.earthquake();
		Vector<Entity> structures = eMan.getMatchingEntities(Component.REPRODUCTIONHOUSE);
		  Vector<Entity> structures2 = eMan.getMatchingEntities(Component.SLEEPINGHOUSE);
		  for(Entity e : structures2){
			  structures.add(e);
		  }
		  Vector<Entity> structures3 = eMan.getMatchingEntities(Component.STORAGEUNIT);
		  for(Entity e : structures3){
			  structures.add(e);
		  }
		  Random r = new Random(3);
		  for(Entity e : structures){
			  if(r.nextInt(3) == 0)
				  eMan.rmEntity(e);
		  }
		
		frame.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void reproduce(){
		  Vector<Entity> repHouses = eMan.getMatchingEntities(Component.REPRODUCTIONHOUSE);
		  for(Entity house : repHouses){
			  PositionComponent pc = (PositionComponent)eMan.getComponent(Component.POSITION, house);
			  Random r = new Random();
			  if(r.nextInt(3000) == 0){
				  EntityFactory.makeNewAlien(pc.pos.x, pc.pos.y);
			  }
		  }
	  }
	
	int FORCEQUAKE = 6000;

	public void run() {
		help();
		scrollPane.getViewport().getView().addMouseListener(new ClickListener());
		scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
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
		AnimalSystem anc = new AnimalSystem();

		if (spawn) {
			Point p = getRandomFreeLocation();
			EntityFactory.makeNewShip(p.x, p.y);
			p = getRandomFreeLocation();
			EntityFactory.makeNewSleepHouse(p.x, p.y);
			for (int i = 0; i < 10; i++) {
				p = getRandomFreeLocation();
				EntityFactory.makeNewDeer(p.x, p.y);
			}
		}

		// We want to have 30 ticks/s
		int goal = 30;
		long milPerTick = (long) ((1.0f / goal) * 1000);
		for (;;) {
			long startMil = System.currentTimeMillis();
			if (r.nextInt(FORCEQUAKE) == 0) {
				earthquake(ls, w, anc, g);
				//infoPanel.updatePanel();
				FORCEQUAKE = 6000;
			}
			reproduce();

			w.render(g);
			ps.tick();
			cs.addCommands(commands);
			commands.clear();
			cs.tick();
			// ms.tick();
			anc.tick();
			as.tick();
			ls.tick();
			rs.tick();
			frame.repaint();
			ms.tick();
			if (userClickVector == null) {
				infoPanel.updatePanel(button1Char);
			} else {
				Entity entity = eMan.getTopEntityAt(userClickVector);
				Sprite sprite = w.getTile((int) userClickVector.y, (int) userClickVector.x).getType();
				infoPanel.setModelEntitySprite(entity, sprite);
				userClickVector = null;
			}

			spawnAliens(aliensToAdd);
			aliensToAdd = 0;

			checkYouWin();
			checkGameOver();

			long endMil = System.currentTimeMillis();
			if (endMil - startMil < milPerTick) {
				try {
					// Thread.sleep(milPerTick - (endMil - startMil));
					Thread.sleep(34);
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
			int xpixel = e.getX();
			int ypixel = e.getY();
			int x = xpixel / Sprite.WIDTH;
			int y = ypixel / Sprite.HEIGHT;
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (button1Char == 'Q' || button1Char == 'W' || button1Char == 'E' || button1Char == 'R') {
					if (!isSpotAvailable(x, y)) {
						JOptionPane.showMessageDialog(null, "That spot is not available for construction.");
					} else if (button1Char == 'Q') {
						commands.push(new Command(Command.Type.BUILD_SLEEPHOUSE, new Vec2f(x, y),
								System.currentTimeMillis()));
					} else if (button1Char == 'W') {
						commands.push(new Command(Command.Type.BUILD_REPRODUCTIONHOUSE, new Vec2f(x, y),
								System.currentTimeMillis()));
					} else if (button1Char == 'E') {
						commands.push(new Command(Command.Type.BUILD_STORAGEUNIT, new Vec2f(x, y),
								System.currentTimeMillis()));
					} else if (button1Char == 'R') {
						commands.push(
								new Command(Command.Type.BUILD_SHIP, new Vec2f(x, y), System.currentTimeMillis()));
					} else {
						System.err.println("Error in MousePressed");
						return;
					}
				} else {
					if (eMan.hasComponents(Component.ANIMAL, eMan.getTopEntityAt(new Vec2f(x,y)))) {
						commands.push(new Command(Command.Type.KILL, eMan.getTopEntityAt(new Vec2f(x ,y)), System.currentTimeMillis()));
					} else if (w.getTile(y, x).getType() == Sprite.TREE) {
						commands.push(new Command(Command.Type.CHOP_TREE, new Vec2f(x, y), System.currentTimeMillis()));
					} else if (w.getTile(y, x).isPassable()) {
						commands.push(new Command(Command.Type.RELOCATE, new Vec2f(x, y), System.currentTimeMillis()));
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) { // this might need
																// to be a 2
				userClickVector = new Vec2f(x, y);

			} else if (e.getButton() == MouseEvent.BUTTON2) {
				if (World.getWorld().getTile(x, y).getType() == Sprite.DIRT) {
					commands.push(
							new Command(Command.Type.BUILD_SLEEPHOUSE, new Vec2f(x, y), System.currentTimeMillis()));

				}
			}
		}
	}

	private class KeyboardListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_N) {
				++aliensToAdd;
			} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
				button1Char = 'A';
			} else if (arg0.getKeyCode() == KeyEvent.VK_Q) {
				button1Char = 'Q';
			} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
				button1Char = 'W';
			} else if (arg0.getKeyCode() == KeyEvent.VK_E) {
				button1Char = 'E';
			}
			if (arg0.getKeyCode() == KeyEvent.VK_R) {
				button1Char = 'R';
			} else if (arg0.getKeyCode() == KeyEvent.VK_P) {
				FORCEQUAKE = FORCEQUAKE == 6000 ? 1 : 6000;
			} else if (arg0.getKeyCode() == KeyEvent.VK_H) {
				help();
			} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
				int result = JOptionPane.showConfirmDialog(null, "Save Data?", "alert",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (result != 2) {
					// yes
					RunMe.saveData();
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}

	}

	public boolean isSpotAvailable(int x, int y) {
		World w = World.getWorld();
		if (w.getTile(y, x).getType() != Sprite.DIRT)
			return false;
		Vector<Entity> list = eMan.getMatchingEntities(Component.POSITION | Component.COLLISION);
		for (Entity e : list) {
			PositionComponent pc = (PositionComponent) eMan.getComponent(Component.POSITION, e);
			if (Math.abs(pc.pos.sub(new Vec2f(x, y)).getMag()) < 0.5f)
				return false;
		}
		return true;
	}

	private Point getRandomFreeLocation() {
		World w = World.getWorld();
		Random r = new Random();
		int x;
		int y;
		do {
			x = r.nextInt(World.WORLD_SIZE - 1);
			y = r.nextInt(World.WORLD_SIZE - 1);
		} while (w.getTile(y, x).getType() != Sprite.DIRT);
		return new Point(x, y);
	}

	public void loadGame(EntityManager eMan, InfoPanel ip) {
		this.eMan = eMan;
		this.infoPanel = ip;
		spawn = false;
	}

	public void checkGameOver() {
		if (eMan.getMatchingEntities(Component.LIVING).size() == 0) {
			int n = JOptionPane.showConfirmDialog(frame, "You lost. Try again?", "You lose!",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				// TODO: restart the game
			} else {
				System.exit(0);
			}
		}
	}

	public void checkYouWin() {
		if (eMan.getMatchingEntities(Component.SHIP).size() > 1) {
			int n = JOptionPane.showConfirmDialog(frame, "You win! Try again?", "You win!", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				// TODO: restart the game
			} else {
				System.exit(0);
			}
		}
	}

	public void help() {
		JOptionPane.showMessageDialog(frame,
				"Welcome to Escape from Earth!\n" + "You control a band of cosmonauts on Earth. There's only one"
						+ " problem:\n" + "a huge increase in seismic activity makes the planet unstabl"
						+ "e. Frequent\n" + "earthquakes threaten to destroy your colony. Collect resourc"
						+ "es to survive and\n" + "- if you can - build a ship to escape.\n\n"
						+ "Left mouse click - collect resource\n" + "Right mouse click - see more info\n"
						+ "Middle mouse click - build house\n" + "H - view this menu",
				"Help", JOptionPane.PLAIN_MESSAGE);
	}

	private char button1Char;
	private int aliensToAdd;
	private Stack<Command> commands;
	private transient BufferedImage renderDest;
	// private JPanel frame;
	private JFrame frame;
	private InfoPanel infoPanel;
	private static EntityManager eMan;
	private Vec2f userClickVector;
	private JScrollPane scrollPane;
	private boolean spawn = true;

}