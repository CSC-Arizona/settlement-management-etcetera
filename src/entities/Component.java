/**
 * Abstract class for components.
 * @author Artyom Perov.
 *
 * This class holds component ID's, and ensures new components are aware of
 * their ID.
 *
 * To add a component:
 * 		1) Extend your component named NameComponent from Component
 * 		2) Add a constant representing it to this file (they should be in order)
 * 		3) Increment the TOTAL_COMPS long
 * 		4) Add it to the getComponentFromCID method in EntityManager
 * 		Optional: create a system using it.
 */

package entities;

public abstract class Component {
	/* 
	 * Default access modifier: seen only within the entities package.
	 *
	 * Might change this to an enum later (probably not though, since that would
	 * mean "getNewEntity(Component.POSITION | Component.VELOCITY | Component.AI)
	 * would have to be changed to:
	 * "getNewEntity(ComponentType.POSITION.getValue() |
	 * 							 ComponentType.VELOCITY.getValue() |
	 * 							 ComponentType.AI.getValue())", and it is already way too
	 * verbose for my taste.
	 */
	static final long POSITION			= 	1 << 0;
	static final long VELOCITY			=		1 << 1;
	static final long SOLID					=		1 << 2;
	static final long AI						=		1 << 3;
	static final long RENDER				=		1 << 4;
	static final long LIVING				=		1 << 5;
	static final long TOTAL_COMPS 	=		6;

	/*
	 * Must be called with an appropriate ComponentID taken from above.
	 */
	public Component(long ComponentID){
		ID = ComponentID;
	}

	final long ID;
}

