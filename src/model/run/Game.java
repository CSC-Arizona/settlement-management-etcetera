package run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;

import entities.Entity;
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
	
	public void run(){
		/*
		 * All entities should be created using the EntityFactory (Factory design
		 * pattern!) class, since it doesn't exist yet (because we have no idea what
		 * entities we want) I just plopped all of this here.
		 */
		EntityManager eMan = EntityManager.INSTANCE;
		Entity hunter = eMan.genNewEntity();
		Entity rock = eMan.genNewEntity();
		
		Entity hunter2 = eMan.genNewEntity();
		Entity rock2 = eMan.genNewEntity();
		
		Entity hunter3 = eMan.genNewEntity();
		Entity rock3 = eMan.genNewEntity();
		try{
			eMan.addComponent(new PositionComponent(5.0f, 10.0f, 0.0f), hunter);
			eMan.addComponent(new RenderComponent("gfx/TheHunter.png", 32, 32), hunter);
			eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), hunter);
			eMan.addComponent(new AIComponent(), hunter);
			eMan.addComponent(new CollisionComponent(), hunter);
			
			eMan.addComponent(new PositionComponent(10.0f, 10.0f, 0.0f), rock);
			eMan.addComponent(new RenderComponent("gfx/Ground.png", 32, 32), rock);
			eMan.addComponent(new MobilityComponent(), rock);
			eMan.addComponent(new CollisionComponent(), rock);
			
			eMan.addComponent(new PositionComponent(5.0f, 20.0f, 0.0f), hunter2);
			eMan.addComponent(new RenderComponent("gfx/TheHunter.png", 32, 32), hunter2);
			eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), hunter2);
			eMan.addComponent(new AIComponent(), hunter2);
			eMan.addComponent(new CollisionComponent(), hunter2);
			
			eMan.addComponent(new PositionComponent(10.0f, 20.0f, 0.0f), rock2);
			eMan.addComponent(new RenderComponent("gfx/Ground.png", 32, 32), rock2);
			eMan.addComponent(new CollisionComponent(), rock2);
			
			eMan.addComponent(new PositionComponent(5.0f, 15.0f, 0.0f), hunter3);
			eMan.addComponent(new RenderComponent("gfx/TheHunter.png", 32, 32), hunter3);
			eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), hunter3);
			eMan.addComponent(new AIComponent(), hunter3);
			eMan.addComponent(new CollisionComponent(), hunter3);
			
			eMan.addComponent(new PositionComponent(10.0f, 15.0f, 0.0f), rock3);
			eMan.addComponent(new RenderComponent("gfx/Ground.png", 32, 32), rock3);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Graphics g = renderDest.getGraphics();
		RenderSystem rs = new RenderSystem(g);
		PhysicsSystem ps = new PhysicsSystem();
		AISystem ais = new AISystem();
		
		// We want to have 30 ticks/s
		int goal = 30;
		long milPerTick = (long)((1.0f/goal) * 1000);
		for(;;){
			long startMil = System.currentTimeMillis();
			
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, renderDest.getWidth(), renderDest.getHeight());
			rs.tick();
			ps.tick();
			ais.tick();
			label.repaint();
			
			long endMil = System.currentTimeMillis();
			if(endMil - startMil < milPerTick){
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
