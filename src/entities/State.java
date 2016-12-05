package entities;

import java.io.Serializable;

public class State implements Serializable {
  public State(Type t, long timestamp){
    type = t;
    this.timestamp = timestamp;
    switch(t){
      case WANDER:
        priority = 0;
        break;
      case FIND_WATER:
        priority = 256;
        break;
      case RELOCATE:
        priority = 8;
        break;
      case CHOP_TREE:
        priority = 8;
        break;
      case BUILD_HOUSE:
        priority = 8;
        break;
      case DEPOSIT_ITEMS:
        priority = 128;
        break;
      case FETCH_ITEMS:
        priority = 128;
        break;
      case CRAFT_ITEMS:
        priority = 128;
        break;
    }
  }

  public enum Type {
    WANDER,
    FIND_WATER,
    RELOCATE,
    CHOP_TREE,
    BUILD_HOUSE,
    DEPOSIT_ITEMS,
    FETCH_ITEMS,
    CRAFT_ITEMS;
  }
<<<<<<< HEAD
  
  public Type getType() {
	  return type;
  }
=======
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264

  short priority;
  long timestamp;
  Type type;
}

