/**
 *	Specifies the position of the entity.
 *	@author Artyom Perov
 */

package entities;

public class PositionComponent extends Component {
  public PositionComponent() {
    super(POSITION);
    x = 0.0f;
    y = 0.0f;
    z = 0.0f;
  }

  public PositionComponent(float x, float y, float z) {
    super(POSITION);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  // Default access modifier: seen within package
  float x;
  float y;
  float z;
}
