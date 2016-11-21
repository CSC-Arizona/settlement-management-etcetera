/*
 * Holds the position of where we want to go
 */
package entities;

import utility.Vec2f;

public class AIComponent extends Component {

  public AIComponent() {
    super(AI);
    destination = new Vec2f(0.0f, 0.0f);
    state = State.WANDER;
  }
  
  public enum State {
    WANDER(-1),
    FIND_WATER(1024);
    
    private State(int priority){
      this.priority = priority;
    }
    
    public int priority;
  }

  Vec2f destination;
  State state;
}
