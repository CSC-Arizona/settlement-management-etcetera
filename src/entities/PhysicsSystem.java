/*
 * This is still under construction. Don't try to follow the code, certain
 * things were wedged into the system to make it appear to be functional.
 *
 * Units used:
 * Mass: kg
 * Time: s
 * Distance: m
 * Velocity: m/s
 */
package entities;

import java.util.Collections;
import java.util.Vector;

import utility.Vec2f;

public class PhysicsSystem extends System {
  /*
   * This will be called with the world instance as an argument when that is
   * done.
   */
  public PhysicsSystem() {
    super(Component.POSITION | Component.MOBILITY);
  }

  public void tick() {
    updateEntityVector();
    processCollisions();
    processMovements();
  }

  private void processMovements() {
    for(Entity e : entitiesToProcess) {
      PositionComponent posComp =
        (PositionComponent) eManager.getComponent(Component.POSITION, e);
      MobilityComponent mobComp =
        (MobilityComponent) eManager.getComponent(Component.MOBILITY, e);
      posComp.pos = posComp.pos.add(mobComp.velocity.mul(1.0f/TICKS_PER_SECOND));

      float xmod = 0;
      float ymod = 0;
      final float mod = 0.5f;
      if(mobComp.velocity.x > 0)
        xmod = -mod;
      else
        xmod = mod;
      if(mobComp.velocity.y > 0)
        ymod = -mod;
      else
        ymod = mod;
      // mobComp.velocity = mobComp.velocity.mul(0.2f);
      Vec2f oldVel = mobComp.velocity;
      Vec2f newVel = mobComp.velocity.sub(new Vec2f(xmod, ymod));
      if(oldVel.x >= 0 && newVel.x <= 0 || oldVel.x <= 0 && newVel.x >= 0 ||
         oldVel.y >= 0 && newVel.y <= 0 || oldVel.y <= 0 && newVel.y >= 0)
        mobComp.velocity = new Vec2f(0.0f, 0.0f);
      else
        mobComp.velocity = newVel;
    }
  }

    private void processCollisions() {
    Vector<Entity> collidables =
      eManager.getMatchingEntities(Component.POSITION | Component.COLLISION);
    Vector<Collision> collisions = new Vector<Collision>();
    for(Entity e : entitiesToProcess) {
      if(eManager.hasComponents(Component.COLLISION, e)) {
        // In here: all e have: COLLISION, POSITION, MOBILITY
        // all f have: COLLISION, POSITION
        for(Entity f : collidables) {
          if(f.getID() != e.getID()) {

            Collision efCollision = new Collision(e, f);
            if(efCollision.occured)
              collisions.add(efCollision);
          }
        }
      }
    }

    // Remove duplicates.
    Collections.sort(collisions);
    Vector<Collision> potColls = collisions;
    collisions = new Vector<Collision>();
    for(int i = 0; i < potColls.size(); ++i) {
      collisions.add(potColls.get(i));
      for(int k = i + 1; k < potColls.size() && potColls.get(k).equals(potColls.get(i)); ++i, ++k);
    }

    for(Collision c : collisions) {
      // If f doesn't have MOBILITY treat it as if all of its MOBILITY
      // components were 0.
      MobilityComponent fMobComp =
        (MobilityComponent)eManager.getComponent(Component.MOBILITY, c.b);
      CollisionComponent fColComp =
        (CollisionComponent)eManager.getComponent(Component.COLLISION, c.b);
      Vec2f fVelocity = new Vec2f(0.0f, 0.0f);
      float fInvMass = 1; // 1kg
      float fRest = fColComp.restitution;

      if(fMobComp != null) {
        fVelocity = fMobComp.velocity;
        fInvMass = fColComp.invMass;
      }

      MobilityComponent eMobComp =
        (MobilityComponent)eManager.getComponent(Component.MOBILITY, c.a);
      CollisionComponent eColComp =
        (CollisionComponent)eManager.getComponent(Component.COLLISION, c.a);

      Vec2f eVelocity = eMobComp.velocity;
      float eInvMass = eColComp.invMass;
      float eRest = ((CollisionComponent)eManager.getComponent(
            Component.COLLISION, c.a)).restitution;

      Vec2f relVel = fVelocity.sub(eVelocity);
      float velAlongNormal = relVel.dot(c.normal);
      // Do not resolve if the entities are separating.
      if(velAlongNormal < 0) {
        // Min restitution
        float ep = eRest < fRest ? eRest : fRest;
        float j = -(1 + ep) * velAlongNormal;
        j /= eInvMass + fInvMass;

        // Apply impulse
        Vec2f impulse = c.normal.mul(j);
        eMobComp.velocity = eMobComp.velocity.sub(impulse.mul(eInvMass));
        if(fMobComp != null) {
          fMobComp.velocity = fMobComp.velocity.add(impulse.mul(fInvMass));
        }
      }
    }

  }

  private class Collision implements Comparable<Collision> {
    public Collision(Entity a, Entity b) {
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
      Vec2f fromAtoB = new Vec2f(bPos.pos.x - aPos.pos.x, bPos.pos.y - aPos.pos.y);

      float aHalfWidth = aCol.width / 2;
      float bHalfWidth = bCol.width / 2;

      float xOverlap = aHalfWidth + bHalfWidth - Math.abs(fromAtoB.x);

      // If there is an overlap on the x axis, the collision is possible, but
      // first we need to check if there is an overlap on y.
      if(xOverlap > 0) {
        float aHalfHeight = aCol.height / 2;
        float bHalfHeight = bCol.height / 2;

        float yOverlap = aHalfHeight + bHalfHeight - Math.abs(fromAtoB.y);

        if(yOverlap > 0) {
          // X is the axis of least penetration
          if(xOverlap < yOverlap) {
            // Make sure the normal points in the right direction.
            if(fromAtoB.x < 0)
              normal = new Vec2f(-1.0f, 0.0f);
            else
              normal = new Vec2f(1.0f, 0.0f);
            penetration = xOverlap;
          } else {
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

    public int compareTo(Collision that) {
      if((this.a.getID() == that.a.getID() && this.b.getID() == that.b.getID()) ||
         (this.a.getID() == that.b.getID() && this.b.getID() == that.a.getID()))
        return 0;
      else
        return 1;
    }

    public boolean equals(Collision that) {
      return compareTo(that) == 0;
    }

    Entity a;
    Entity b;
    boolean occured;
    float penetration;
    Vec2f normal;
  }
}

