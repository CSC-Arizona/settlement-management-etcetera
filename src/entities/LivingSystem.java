package entities;

public class LivingSystem extends Systems {
  public LivingSystem(){
    //TODO
    super(Component.LIVING);
  }

  public void tick(){
    updateEntityVector();
    for(Entity e : entitiesToProcess){
      LivingComponent lc =
        (LivingComponent)eManager.getComponent(Component.LIVING, e);

      handleHydration(lc);
      if(lc.HP <= 0.0f)
        purgeTheDead(e);
    }
  }

  private void handleHydration(LivingComponent lc){
    if(lc.hydration < 30.0f)
      lc.HP -= 5.0f / TICKS_PER_SECOND;
    else if(lc.hydration < 15.0f)
      lc.HP -= 15.0f / TICKS_PER_SECOND;
    else if(lc.hydration < 10.0f)
      lc.HP -= 30.0f / TICKS_PER_SECOND;
    lc.hydration -= 1.0f / TICKS_PER_SECOND;
  }

  private void purgeTheDead(Entity e){
    PositionComponent ps =
      (PositionComponent) eManager.getComponent(Component.POSITION, e);
    EntityFactory.makeNewBloodPool(ps.x, ps.y);
    eManager.rmEntity(e);
  }
}

