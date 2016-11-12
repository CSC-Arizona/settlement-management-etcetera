/*
 * Mass: kg
 * Time: s
 * Distance: m
 * Velocity: m/s
 */
package entities;

import java.util.Vector;

public class PhysicsSystem extends System {
	/*
	 * This will be called with the world instance as an argument when that is
	 * done.
	 */
	public PhysicsSystem(){
		super(Component.POSITION | Component.MOBILITY);
	}

	public void tick(){
		updateEntityVector();
		processCollisions();
		processMovements();
	}

	private void processMovements(){
		for(Entity e : entitiesToProcess){
			PositionComponent posComp =
				(PositionComponent)eManager.getComponent(Component.POSITION, e);
			MobilityComponent mobComp =
				(MobilityComponent)eManager.getComponent(Component.MOBILITY, e);
			if(mobComp.velocity.x != 0 && mobComp.velocity.y != 0){
				posComp.x += mobComp.velocity.x / TICKS_PER_SECOND;
				posComp.y += mobComp.velocity.y / TICKS_PER_SECOND;
				float slope = mobComp.velocity.y / mobComp.velocity.x;
				mobComp.velocity.sub(new Vec2f(slope, slope));
			}
		}
	}

	private void processCollisions(){
		Vector<Entity> collidables =
			eManager.getMatchingEntities(Component.POSITION | Component.COLLISION);
		for(Entity e : entitiesToProcess){
			if(eManager.hasComponents(Component.COLLISION, e)){
				// In here: all e have: COLLISION, POSITION, MOBILITY
				// 					all f have: COLLISION, POSITION
				for(Entity f : collidables){
					if(f.getID() != e.getID()){

						// If f doesn't have MOBILITY treat it as if all of its MOBILITY
						// components were 0.
						Vec2f fVelocity = new Vec2f(0.0f, 0.0f);
						float fInvMass = 0;
						float fRest = ((CollisionComponent)
								eManager.getComponent(Component.COLLISION, f)).restitution;
						MobilityComponent fMobComp =
							(MobilityComponent)eManager.getComponent(Component.MOBILITY, f);
						if(fMobComp != null){
							fVelocity = fMobComp.velocity;
							fInvMass = fMobComp.invMass;
						}

						MobilityComponent eMobComp =
							(MobilityComponent)eManager.getComponent(Component.MOBILITY, e);
						Vec2f eVelocity = eMobComp.velocity;
						float eInvMass = eMobComp.invMass;
						float eRest = ((CollisionComponent)
								eManager.getComponent(Component.COLLISION, e)).restitution;

						Collision efCollision = new Collision(e, f);
						if(efCollision.occured){
							Vec2f relVel = fVelocity.sub(eVelocity);
							float velAlongNormal = relVel.dot(efCollision.normal);
							// Do not resolve if the entities are separating.
							if(velAlongNormal < 0){
								// Min restitution
								float ep = eRest < fRest ? eRest : fRest;
								float j = -(1 + ep) * velAlongNormal;
								j /= eInvMass + fInvMass;

								// Apply impulse
								Vec2f impulse = efCollision.normal.mul(j);
								eMobComp.velocity.sub(impulse.mul(eInvMass));
								if(fMobComp != null)
									fMobComp.velocity.add(impulse.mul(fInvMass));
							}
						}
					}
				}
			}
		}
	}

	private class Collision {
		public Collision(Entity a, Entity b){
			this.a = a;
			this.b = b;
			occured = false;
			PositionComponent aPos =
				(PositionComponent)eManager.getComponent(Component.POSITION, a);
			PositionComponent bPos =
				(PositionComponent)eManager.getComponent(Component.POSITION, b);
			CollisionComponent aCol =
				(CollisionComponent)eManager.getComponent(Component.COLLISION, a);
			CollisionComponent bCol =
				(CollisionComponent)eManager.getComponent(Component.COLLISION, b);

			// Vector pointing from a to b
			Vec2f fromAtoB = new Vec2f(bPos.x - aPos.x, bPos.y - aPos.y);

			float aHalfWidth = aCol.width / 2;
			float bHalfWidth = bCol.width / 2;

			float xOverlap = aHalfWidth + bHalfWidth - Math.abs(fromAtoB.x);

			// If there is an overlap on the x axis, the collision is possible, but
			// first we need to check if there is an overlap on y.
			if(xOverlap > 0){
				float aHalfHeight = aCol.height / 2;
				float bHalfHeight = bCol.height / 2;

				float yOverlap = aHalfHeight + bHalfHeight - Math.abs(fromAtoB.y);

				if(yOverlap > 0){
					// X is the axis of least penetration
					if(xOverlap < yOverlap){
						// Make sure the normal points in the right direction.
						if(fromAtoB.x < 0)
							normal = new Vec2f(-1.0f, 0.0f);
						else
							normal = new Vec2f(1.0f, 0.0f);
						penetration = xOverlap;
					}else{
						if(fromAtoB.y < 0)
							normal = new Vec2f(0.0f, -1.0f);
						else
							normal = new Vec2f(0.0f, 1.0f);
						penetration = yOverlap;
					}
					occured = true;
				}
			}
		}

		Entity a;
		Entity b;
		boolean occured;
		float penetration;
		Vec2f normal;
	}
	
	static final int TICKS_PER_SECOND = 30;
}

