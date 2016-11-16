/**
 *  Specifies the velocity of the entity.
 *  @author Artyom Perov
 */

package entities;

import utility.Vec2f;

public class MobilityComponent extends Component {
  public MobilityComponent() {
    super(MOBILITY);
    velocity = new Vec2f(0.0f, 0.0f);
  }

  public MobilityComponent(Vec2f velocity) {
    super(Component.MOBILITY);
    this.velocity = velocity;
  }

  // Default access modifier: seen within package
  Vec2f velocity;
}
