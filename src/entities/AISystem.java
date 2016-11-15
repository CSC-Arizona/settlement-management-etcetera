/*
 * Ghosts randomly a destination and goes there.
 * Hunters try to hunt the ghost but need the right resources.
 * Authors: Artyom Perov and Caleb Short
 */
package entities;

import java.util.Random;

public class AISystem extends System {

  public AISystem() {
    super(Component.AI | Component.POSITION | Component.MOBILITY);
  }

  public void tick() {
    updateEntityVector();
    Random r = new Random();
    for (Entity e : entitiesToProcess) {
      PositionComponent ps = (PositionComponent) eManager.getComponent(Component.POSITION, e);
      Vec2f location = new Vec2f(ps.x, ps.y);
      AIComponent ac = ((AIComponent) eManager.getComponent(Component.AI, e));
      MobilityComponent mc = (MobilityComponent) eManager.getComponent(Component.MOBILITY, e);

      if(ac.AIType.equals("player")){
        PlayerAIComponent pc = (PlayerAIComponent) ac;
    	if(pc.timeHungry >= 10){
    		eManager.rmEntity(e);
    	}
    	else{
    		/*
    	  if(Math.abs(location.sub(pc.destination).getMag()) <= 10.5f){
    		  // TODO: Tell hunter to eat flower
    	  }
    	  else if(Math.abs(location.sub(pc.destination).getMag()) <= 10.5f){
    		  // TODO: Tell hunter to eat flower
    	  }
    	  else if(Math.abs(location.sub(pc.destination).getMag()) <= 10.5f){
    		  // TODO: Tell hunter to eat flower
    	  }
    		
    	  if(pc.timeHungry >= 5){
    	  	//eManager.getMatchingEntities()
    	  }
    	  else if(pc.numArrows <= 0){
    	  	// TODO: Tell hunter to search for a rock
    	  }
    	  else{
    	  	// TODO: Tell hunter to search for a ghost
    	  }   	*/
    	}
    	
        pc.timeHungry += 1/30f;
      }
      else if(ac.AIType.equals("enemy")){
        // If we are within 1 meter of our destination we are close enough.
        if (Math.abs(location.sub(ac.destination).getMag()) > 1.0f) {
          // If not we first need to calculate the direction vector from where we
          // are to where we are going.
          Vec2f newVel = ac.destination.sub(location);
          // Dividing the resulting vector by it's magnitude gives us a vector
          // pointing in the right direction with
          // magnitude of 1 (m/s).
          newVel = newVel.mul(1 / newVel.getMag());
          // Here we adjust the speed to be 1.5 m/s
          newVel = newVel.mul(1.5f);
          mc.velocity = newVel;
        } else {
          ac.destination = new Vec2f(r.nextFloat() * 20, r.nextFloat() * 20);
        }
      }
    }
  }
}
