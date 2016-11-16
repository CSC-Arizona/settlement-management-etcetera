/*
 * Holds the position of where we want to go
 */
package entities;

import utility.Vec2f;

public class AIComponent extends Component {

  public AIComponent() {
    super(AI);
    destination = new Vec2f(10.0f, 10.0f);
  }

  Vec2f destination;
}
