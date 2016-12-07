/**
 *  Specifies the position of the entity.
 *  @author Artyom Perov
 */

package entities;

import utility.Vec2f;

public class PositionComponent extends Component {
  public PositionComponent() {
    super(POSITION);
    pos = new Vec2f(0.0f, 0.0f);
    z = 0.0f;
  }

  public PositionComponent(float x, float y, float z) {
    super(POSITION);
    pos = new Vec2f(x, y);
    this.z = z;
  }

  // Default access modifier: seen within package
  public Vec2f pos;
  float z;
}
