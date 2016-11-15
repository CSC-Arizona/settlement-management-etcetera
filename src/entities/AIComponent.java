/*
 * Holds the position of where we want to go
 */
package entities;

public abstract class AIComponent extends Component {

  public AIComponent() {
    super(AI);
    destination = new Vec2f(10.0f, 10.0f);
  }
  
  String AIType;

  Vec2f destination;
}
