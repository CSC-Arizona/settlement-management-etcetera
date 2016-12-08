package entities;

import utility.Sprite;
import world.World;

public class AnimalSystem extends System {
  public AnimalSystem(){
    super(Component.ANIMAL | Component.POSITION);
  }

  public void tick(){
    updateEntityVector();
    for(Entity e : entitiesToProcess){
      AnimalComponent ac =
        (AnimalComponent)eManager.getComponent(Component.ANIMAL, e);
      PositionComponent pc =
        (PositionComponent)eManager.getComponent(Component.POSITION, e);

      regainHealth(ac);
      if (ac.hit) {
    	  damage(ac);
    	  ac.toggleHit();
      }
      if(ac.dead)
        purgeTheDead(e);
    }
  }
  
  public void earthquake() {
	 for(Entity e : entitiesToProcess){
		 AnimalComponent ac = (AnimalComponent)eManager.getComponent(Component.ANIMAL, e);
		 PositionComponent pc = (PositionComponent)eManager.getComponent(Component.POSITION, e);
		  if (!World.getWorld().getBoard()[(int)pc.getPos().y][(int)pc.getPos().x].isPassable()) {
		 	purgeTheDead(e);
	 	}
	 }
  }
  
  private void damage(AnimalComponent ac) {
	  ac.HP -= 50.0f / TICKS_PER_SECOND;
  }
  
  private void regainHealth(AnimalComponent ac){
	    ac.HP += 1.0f / TICKS_PER_SECOND;
	  if(ac.HP > ac.maxHP)
	    ac.HP = ac.maxHP;
  }

  private void purgeTheDead(Entity e){
    PositionComponent ps =
      (PositionComponent) eManager.getComponent(Component.POSITION, e);
    EntityFactory.makeNewBloodPool(ps.pos.x, ps.pos.y);
    eManager.rmEntity(e);
  }
}

