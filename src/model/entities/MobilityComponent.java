/**
 *  Holds data needed for change-of-position calculations.
 *  @author Artyom Perov
 */

package entities;

public class MobilityComponent extends Component {
  public MobilityComponent(){
    super(MOBILITY);
    velocity = new Vec2f(0.0f, 0.0f);
    invMass = 0.25f;
  }

  public MobilityComponent(Vec2f velocity, float mass){
    super(Component.MOBILITY);
    this.velocity = velocity;
    this.invMass = 1 / mass;
  }

  // Default access modifier: seen within package
  Vec2f velocity;
  float invMass;
}

