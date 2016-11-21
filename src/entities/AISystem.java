/*
 * Randomly selects a destination and goes there.
 */
package entities;

import java.util.Random;
import utility.Vec2f;

public class AISystem extends System {

  public AISystem() {
    super(Component.AI | Component.POSITION | Component.MOBILITY);
  }

  public void tick() {
    updateEntityVector();
    for (Entity e : entitiesToProcess) {
      PositionComponent pc =
        (PositionComponent)eManager.getComponent(Component.POSITION, e);
      AIComponent ac =
        (AIComponent)eManager.getComponent(Component.AI, e);
      CommandableComponent cc =
        (CommandableComponent)eManager.getComponent(Component.COMMANDABLE, e);
      MobilityComponent mc =
        (MobilityComponent)eManager.getComponent(Component.MOBILITY, e);
      
      if(cc != null && !cc.commands.isEmpty() && ac.state.priority < 16){
        proccessCommands(ac, cc, pc);
      }else{
        LivingComponent lc =
          (LivingComponent)eManager.getComponent(Component.LIVING, e);

        
        // Change the state TODO: should be it's own method
        if(lc != null){
          java.lang.System.out.println(lc.HP + " w: " + lc.hydration);

          if(lc.hydration < 30.0f)
            ac.state = AIComponent.State.FIND_WATER;
          else if(lc.hydration >= 100.0f)
            ac.state = AIComponent.State.WANDER;
        }
        
        switch(ac.state){
          case WANDER:
            if(r.nextInt(100) == 42){
              ac.destination = pc.pos.sub(new Vec2f((r.nextFloat() - 1.0f) * 3,
                  (r.nextFloat() - 1.0f) * 3));
            }
            break;
          case FIND_WATER:
            ac.destination = find(/*TileType.WATER*/);
            if(ac.destination.sub(pc.pos).getMag() <= CLOSE_ENOUGH)
              lc.hydration += 5.0f;
            break;
        }
      }
      goStraight(mc, pc, ac);

    }
  }

  private void proccessCommands(AIComponent ac, CommandableComponent cc, PositionComponent pc){
    if(cc.commands.size() != 0){
      Command cur = cc.commands.peek();
      float distance = (cur.location.sub(pc.pos)).getMag();
      switch(cur.type){
        case RELOCATE:
          if(distance > CLOSE_ENOUGH)
            ac.destination = cur.location;
          else
            cc.commands.pop();
          break;
        case CHOP_TREE:
          if(distance > CLOSE_ENOUGH){
            ac.destination = cur.location;
          }else{
            // TODO: do the chopping
            cc.commands.pop();
          }
          break;
      }
    }
  }

  private void goStraight(MobilityComponent mc, PositionComponent pc, AIComponent ac){
    if(ac.destination.sub(pc.pos).getMag() > CLOSE_ENOUGH){
      // We first need to calculate the direction vector from where we
      // are to where we are going.
      Vec2f newVel = ac.destination.sub(pc.pos);
      // Dividing the resulting vector by it's magnitude gives us a vector
      // pointing in the right direction with
      // magnitude of 1 (m/s).
      newVel = newVel.mul(1 / newVel.getMag());
      // Here we adjust the speed to be 1.5 m/s
      newVel = newVel.mul(1.5f);
      mc.velocity = newVel;
    }else{
      mc.velocity = new Vec2f(0.0f, 0.0f);
    }
  }
  
  // TODO: Should be find(Tile t)
  private Vec2f find(){
    return new Vec2f(1.0f, 1.0f);
  }

  private static Random r = new Random();
  private static final float CLOSE_ENOUGH = 1.0f;
}
