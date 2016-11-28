/*
 * Holds the position of where we want to go
 */
package entities;

import utility.Vec2f;

import java.util.Comparator;
import java.util.PriorityQueue;
//import java.awt.Point;
import java.util.Vector;

public class AIComponent extends Component {

  public AIComponent() {
    super(AI);
    //destination = new Vec2f(0.0f, 0.0f);
     states = new PriorityQueue<State>(10, new Comparator<State>() {
       public int compare(State a, State b){
         return b.priority - a.priority;
       }
     });
     states.add(new State(State.Type.WANDER));
  }
  
  //Vec2f destination;
  Vector<Vec2f> path;
  PriorityQueue<State> states;
}
