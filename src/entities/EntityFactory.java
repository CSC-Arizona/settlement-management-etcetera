package entities;

import utility.Sprite;
import utility.Vec2f;

public class EntityFactory {

  public static Entity makeNewAlien(float x, float y) {
    Entity alien = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), alien);
    eMan.addComponent(new RenderComponent(Sprite.ALIEN), alien);
    eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f)), alien);
    eMan.addComponent(new AIComponent(), alien);
    //eMan.addComponent(new CollisionComponent(0.4f, 0.4f, 0.5f, 80.0f), alien);
    eMan.addComponent(new LivingComponent(), alien);
    eMan.addComponent(new ContainerComponent(4, true), alien);
    eMan.addComponent(new MessageComponent(), alien);
    eMan.addComponent(new NameComponent("ALIEN"), alien);
    eMan.markAs(Component.COMMANDABLE, alien);
    return alien;
  }
  
  public static Entity makeNewGodzilla(float x, float y) {
    Entity godzilla = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), godzilla);
    eMan.addComponent(new RenderComponent(Sprite.GODZILLA), godzilla);
    eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f)), godzilla);
    eMan.addComponent(new AIComponent(), godzilla);
    return godzilla;
  }
  
  public static Entity makeNewBandage(float x, float y) {
    Entity bandage = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f),  bandage);
    eMan.addComponent(new RenderComponent(Sprite.BANDAGE), bandage);
    eMan.addComponent(new CollisionComponent(1.0f, 1.0f, 0.05f, 80.0f), bandage);
    return bandage;
  }
  
  public static Entity makeNewAmmo(float x, float y) {
    Entity ammo = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), ammo);
    eMan.addComponent(new RenderComponent(Sprite.AMMO), ammo);
    eMan.addComponent(new CollisionComponent(1.0f, 1.0f, 0.05f, 80.0f), ammo);
    return ammo;
  }
  
  public static Entity makeNewShip(float x, float y) {
    Entity ship = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), ship);
    eMan.addComponent(new RenderComponent(Sprite.SHIP), ship);
    eMan.addComponent(new CollisionComponent(1.0f, 1.0f, 0.05f, 80.0f), ship);
    eMan.addComponent(new ContainerComponent(100, false), ship);
    eMan.addComponent(new MessageComponent(), ship);
    eMan.addComponent(new NameComponent("SHIP"), ship);
    eMan.markAs(Component.SHIP, ship);
    return ship;
  }

  public static Entity makeNewBloodPool(float x, float y){
    Entity bloodPool = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), bloodPool);
    eMan.addComponent(new RenderComponent(Sprite.BLOOD_POOL), bloodPool);
    eMan.addComponent(new NameComponent("Blood"), bloodPool);
    return bloodPool;
  }

  public static Entity makeNewHouse(float x, float y){
    Entity house = eMan.genNewEntity();
	eMan.addComponent(new PositionComponent(x, y, 0.0f), house);
	eMan.addComponent(new RenderComponent(Sprite.HOUSE), house);
	//eMan.addComponent(new ContainerComponent(50), house);
	eMan.addComponent(new NameComponent("House"), house);
	//eMan.addComponent(new CollisionComponent(), house);
	eMan.markAs(Component.HOUSE, house);
	return house;
  }
  
  private static EntityManager eMan = EntityManager.INSTANCE;
  // We don't want it to be instantiated.
  private EntityFactory(){}

  private EntityFactory(EntityFactory that){}
}
