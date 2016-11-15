package entities;

public class EntityFactory {
  public static Entity makeNewHunter(float x, float y) {
    EntityManager eMan = EntityManager.INSTANCE;
    Entity hunter = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), hunter);
    eMan.addComponent(new RenderComponent("gfx/TheHunter.png", 32, 32), hunter);
    eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), hunter);
    eMan.addComponent(new AIComponent(), hunter);
    eMan.addComponent(new CollisionComponent(), hunter);
    return hunter;
  }

  public static Entity makeNewGhost(float x, float y) {
    EntityManager eMan = EntityManager.INSTANCE;
    Entity ghost = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), ghost);
    eMan.addComponent(new RenderComponent("gfx/ghost.png", 32, 32), ghost);
    eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), ghost);
    eMan.addComponent(new AIComponent(), ghost);
    return ghost;
  }

  public static Entity makeNewRock(float x, float y) {
    EntityManager eMan = EntityManager.INSTANCE;
    Entity rock = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), rock);
    eMan.addComponent(new RenderComponent("gfx/rock.png", 32, 32), rock);
    eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 1.2f), rock);
    eMan.addComponent(new CollisionComponent(0.5f, 0.5f, 3.1f), rock);
    return rock;
  }

  public static Entity makeNewFlower(float x, float y) {
    EntityManager eMan = EntityManager.INSTANCE;
    Entity flower = eMan.genNewEntity();
    eMan.addComponent(new PositionComponent(x, y, 0.0f), flower);
    eMan.addComponent(new RenderComponent("gfx/flower.png", 32, 32), flower);
    return flower;
  }

  // We don't want it to be instantiated.
  private EntityFactory(){}

  private EntityFactory(EntityFactory that){}
}
