/*
 * This is only here to show entities in action.
 */
package entities;

public class AISystem extends System {

	public AISystem() {
		super(Component.AI | Component.POSITION | Component.MOBILITY);
	}

	public void tick(){
		updateEntityVector();
		for(Entity e : entitiesToProcess){
			((MobilityComponent)eManager.getComponent(Component.MOBILITY, e)).velocity = new Vec2f(1.0f, 0.0f);
		}
	}
}
