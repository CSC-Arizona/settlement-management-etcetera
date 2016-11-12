/**
 * Abstract class for systems.
 * @author Artyom Perov.
 *
 * On construction this class populates the entitiesToProcess vector based
 * on the components you specified during construction.
 *
 * To add a system:
 * 		1) Decide what components your system will need to do what it has to do,
 * 			 if some of the data you will need is not there, create another
 * 			 component that provides that data (or just add to an existing component
 * 			 if that makes more sense).
 * 		2) Extend your system named NameSystem from System calling the super
 * 			 constructor with the component bitset of the required components.
 * 			 For example: If your system needs position and render components you
 * 			 would do "super(Component.POSITION | Component.RENDER)".
 * 		3) Override the tick() method. A system is responsible for processing
 * 			 all entities that satisfy it's requirements (requiredComponentBitSet),
 * 			 so you would iterate through the entitiesToProcess (IMPORTANT: entities
 * 			 may gain/loose components during the game, so the first line in your
 * 			 tick() method should be: "updateEntityVector()"), and do what you must
 * 			 with each entity's data (which is returned by
 * 			 eManager.getComponent(Component.SOMETHING, someEntity) ).
 */
package entities;

import java.util.Vector;

public abstract class System {
	public System(long requiredComponentBitSet){
		cbs = requiredComponentBitSet;
		eManager = EntityManager.INSTANCE;
		updateEntityVector();
	}

	/*
	 * Called every game tick. All the processing goes on in here (of course
	 * helper methods are encouraged). Don't forget to call updateEntityVector()!
	 */
	public abstract void tick();


	public void updateEntityVector(){
		entitiesToProcess = eManager.getMatchingEntities(cbs);
	}

	/*
	 * These are the entities your system needs to process. They are determined
	 * based on the requiredComponentBitSet.
	 */
	protected Vector<Entity> entitiesToProcess;

	protected EntityManager eManager;
	private long cbs;
}

