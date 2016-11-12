/**
 *	Specifies the velocity of the entity.
 *	@author Artyom Perov
 */

package entities;

public class MobilityComponent extends Component {
	public MobilityComponent(){
		super(MOBILITY);
		velocity = new Vec2f(0.0f, 0.0f);
		acceleration = new Vec2f(0.0f, 0.0f);
		invMass = 1.0f;
	}

	public MobilityComponent(Vec2f velocity, Vec2f acceleration, float mass){
		super(Component.MOBILITY);
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.invMass = 1 / mass;
	}

	// Default access modifier: seen within package
	Vec2f velocity;
	Vec2f acceleration;
	float invMass;
}

