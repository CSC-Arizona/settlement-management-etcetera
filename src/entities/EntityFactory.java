package entities;

public class EntityFactory {

	public static Entity makeNewAlien(float x, float y) {
		EntityManager eMan = EntityManager.INSTANCE;
		Entity alien = eMan.genNewEntity();
		eMan.addComponent(new PositionComponent(x, y, 0.0f), alien);
		eMan.addComponent(new RenderComponent("alien"), alien);
		eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), alien);
		eMan.addComponent(new EnemyAIComponent(), alien);
		return alien;
	}
	
	public static Entity makeNewGodzilla(float x, float y) {
		EntityManager eMan = EntityManager.INSTANCE;
		Entity godzilla = eMan.genNewEntity();
		eMan.addComponent(new PositionComponent(x, y, 0.0f), godzilla);
		eMan.addComponent(new RenderComponent("godzilla"), godzilla);
		eMan.addComponent(new MobilityComponent(new Vec2f(0.0f, 0.0f), 80.0f), godzilla);
		eMan.addComponent(new EnemyAIComponent(), godzilla);
		return godzilla;
	}
	
	public static Entity makeNewBandage(float x, float y) {
		EntityManager eMan = EntityManager.INSTANCE;
		Entity bandage = eMan.genNewEntity();
		eMan.addComponent(new PositionComponent(x, y, 0.0f),  bandage);
		eMan.addComponent(new RenderComponent("bandage"), bandage);
		return bandage;
	}
	
	public static Entity makeNewAmmo(float x, float y) {
		EntityManager eMan = EntityManager.INSTANCE;
		Entity ammo = eMan.genNewEntity();
		eMan.addComponent(new PositionComponent(x, y, 0.0f), ammo);
		eMan.addComponent(new RenderComponent("ammo"), ammo);
		return ammo;
	}

	// We don't want it to be instantiated.
	private EntityFactory(){}

	private EntityFactory(EntityFactory that){}
}
