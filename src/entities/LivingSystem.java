package entities;

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
      if(lc.HP <= 0.0f)
        purgeTheDead(e);
    }
  }
  
  public void earthquake() {
	  for(Entity e : entitiesToProcess){
	      LivingComponent lc =
	        (LivingComponent)eManager.getComponent(Component.LIVING, e);
	      lc.HP -= 75.0f;

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
    lc.hydration -= 1.0f / TICKS_PER_SECOND;
    
    if(lc.hydration >= 80.0f)
      lc.HP += 10.0f / TICKS_PER_SECOND;
    if(lc.HP > lc.maxHP)
      lc.HP = lc.maxHP;
    
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
	      lc.HP -= 5.0f / TICKS_PER_SECOND;
	    else if(lc.restVal < 15.0f)
	      lc.HP -= 15.0f / TICKS_PER_SECOND;
	    else if(lc.restVal < 10.0f)
	      lc.HP -= 30.0f / TICKS_PER_SECOND;
	    lc.restVal -= 0.1f / TICKS_PER_SECOND;
	    
	    if(lc.hydration >= 80.0f)
	      lc.HP += 10.0f / TICKS_PER_SECOND;
	    if(lc.HP > lc.maxHP)
	      lc.HP = lc.maxHP;
	    
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

  private void purgeTheDead(Entity e){
    PositionComponent ps =
      (PositionComponent) eManager.getComponent(Component.POSITION, e);
    EntityFactory.makeNewBloodPool(ps.pos.x, ps.pos.y);
    eManager.rmEntity(e);
  }
}

