/**
 * Abstract class for components.
 * @author Artyom Perov.
 *
 * This class holds component ID's, and ensures new components are aware of
 * their ID.
 *
 * To add a component:
 * 		1) Extend your component named NameComponent from Component
 * 		2) Add a constant representing it to list below (they should be in order)
 * 		3) Increment the TOTAL_COMPS long
 * 		4) Add it to the getComponentFromCID method
 * 		Optional: create a system using it.
 */

package entities;

public abstract class Component {
	/* 
	 * Default access modifier: seen only within the entities package.
	 *
	 * Might change this to an enum later (probably not though, since that would
	 * mean "genNewEntity(Component.POSITION | Component.VELOCITY | Component.AI)
	 * would have to be changed to:
	 * "genNewEntity(ComponentType.POSITION.getValue() |
	 * 							 ComponentType.VELOCITY.getValue() |
	 * 							 ComponentType.AI.getValue())", and it is already way too
	 * verbose for my taste.
	 */
	static final long POSITION			= 	1 << 0;
	static final long MOBILITY			=		1 << 1;
	static final long COLLISION			=		1 << 2;
	static final long RENDER				=		1 << 3;
	static final long TOTAL_COMPS 	=		4;

	/*
	 * Must be called with an appropriate ComponentID taken from above.
	 */
	public Component(long ComponentID){
		ID = ComponentID;
	}

	/*
	 * So far this method is only used by genNewEntity(componentBitSet).
	 * It might get removed in the future if genNewEntity(componentBitSet) is
	 * deemed unnecessary.
	 */
	public static Component getComponentFromCID(long cID){
		// A switch would make more sense, but java switches do not support longs.
		if(cID == POSITION)
			return new PositionComponent();
		if(cID == MOBILITY)
			return new MobilityComponent();
		if(cID == COLLISION)
			return new CollisionComponent();
		if(cID == RENDER)
			return new RenderComponent(null, 0, 0);
		return null;
	}

	final long ID;
}

