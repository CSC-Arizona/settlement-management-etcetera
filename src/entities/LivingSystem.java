package entities;

import java.util.Random;

import utility.Sprite;
import world.World;

public class LivingSystem extends System {
  public LivingSystem(){
    //TODO
    super(Component.LIVING | Component.POSITION);
  }

  public void tick(){
    updateEntityVector();
    for(Entity e : entitiesToProcess){
      LivingComponent lc =
        (LivingComponent)eManager.getComponent(Component.LIVING, e);
      PositionComponent pc =
        (PositionComponent)eManager.getComponent(Component.POSITION, e);

      handleHydration(lc, pc);
      handleFatigue(lc, pc);
      regainHealth(lc);
      if(lc.HP <= 0.0f)
        purgeTheDead(e);
    }
  }
  
  public void earthquake() {
	  for(Entity e : entitiesToProcess){
	      LivingComponent lc =
	        (LivingComponent)eManager.getComponent(Component.LIVING, e);
	      Random r = new Random();
	      int loss = r.nextInt(50);
	      loss += 25;
	      lc.HP -= loss;

	      if(lc.HP <= 0.0f)
	        purgeTheDead(e);
	    }
  }

  private void handleHydration(LivingComponent lc, PositionComponent pc){
    if(lc.hydration < 30.0f)
      lc.HP -= 5.0f / TICKS_PER_SECOND;
    else if(lc.hydration < 15.0f)
      lc.HP -= 15.0f / TICKS_PER_SECOND;
    else if(lc.hydration < 10.0f)
      lc.HP -= 30.0f / TICKS_PER_SECOND;
    lc.hydration -= 0.5f / TICKS_PER_SECOND;
    
    World w = World.getWorld();
    int y = Math.round(pc.pos.y);
    int x = Math.round(pc.pos.x);
    y = y < 0 ? 0 : y > w.getSize() ? w.getSize() : y;
    x = x < 0 ? 0 : x > w.getSize() ? w.getSize() : x;
    if(w.getTile(y, x).getType() == Sprite.LAKE)
      lc.hydration += 30.0f / TICKS_PER_SECOND;

    if(lc.hydration > lc.maxHydration)
      lc.hydration = lc.maxHydration;
    //java.lang.System.out.println(lc.hydration);
  }
  
  private void handleFatigue(LivingComponent lc, PositionComponent pc){
	    if(lc.restVal < 30.0f)
	      lc.HP -= 0.5f / TICKS_PER_SECOND;
	    else if(lc.restVal < 15.0f)
	      lc.HP -= 2.0f / TICKS_PER_SECOND;
	    else if(lc.restVal < 10.0f)
	      lc.HP -= 4.0f / TICKS_PER_SECOND;
	    lc.restVal -= 0.05f / TICKS_PER_SECOND;
	    
	    World w = World.getWorld();
	    int y = Math.round(pc.pos.y);
	    int x = Math.round(pc.pos.x);
	    y = y < 0 ? 0 : y > w.getSize() ? w.getSize() : y;
	    x = x < 0 ? 0 : x > w.getSize() ? w.getSize() : x;
	    Entity sleephouse = eManager.getTopEntityAt(pc.pos);
	    RenderComponent rc = (RenderComponent)eManager.getComponent(Component.RENDER, sleephouse);
	    if(rc.s == Sprite.SLEEPHOUSE)
	      lc.restVal += 3.0f / TICKS_PER_SECOND;

	    if(lc.restVal > lc.maxRestVal)
	      lc.restVal = lc.maxRestVal;
	    //java.lang.System.out.println(lc.restVal);
  }
  
  private void regainHealth(LivingComponent lc){
	  if(lc.hydration >= 80.0f && lc.restVal >= 80.0f)
	    lc.HP += 1.0f / TICKS_PER_SECOND;
	  if(lc.HP > lc.maxHP)
	    lc.HP = lc.maxHP;
  }

  private void purgeTheDead(Entity e){
    PositionComponent ps =
      (PositionComponent) eManager.getComponent(Component.POSITION, e);
    EntityFactory.makeNewBloodPool(ps.pos.x, ps.pos.y);
    eManager.rmEntity(e);
  }
}

