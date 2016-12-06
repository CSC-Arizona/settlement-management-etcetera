/*
 * Holds the position of where we want to go
 */
package entities;

import utility.Vec2f;

import java.io.Serializable;
import java.util.Comparator;
import java.util.PriorityQueue;
//import java.awt.Point;
import java.util.Vector;

public class AIComponent extends Component {

  public AIComponent() {
    super(AI);
    //destination = new Vec2f(0.0f, 0.0f);
     states = new PriorityQueue<State>(10, new StateComparator());
  }
  
  private class StateComparator implements Comparator<State>, Serializable {
    public int compare(State a, State b){
   	 if(b.priority != a.priority)
   		 return b.priority - a.priority;
   	 else
   		 return (int)(a.timestamp - b.timestamp);
    }
  }
  
  public PriorityQueue<State> getStates() {
	  return states;
  }
  
  //Vec2f destination;
  Vector<Vec2f> path;
  PriorityQueue<State> states;
}
