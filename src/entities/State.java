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
      case REST:
    	priority = 256;
    	break;
      case EAT:
    	priority = 256;
    	break;
      case RELOCATE:
        priority = 4;
        break;
      case CHOP_TREE:
        priority = 8;
        break;
      case GATHER_STONE:
    	priority = 8;
    	break;
      case GATHER_BERRIES:
      	priority = 8;
      	break;
      case BUILD_SLEEPHOUSE:
        priority = 8;
        break;
      case BUILD_REPRODUCTIONHOUSE:
          priority = 8;
          break;
      case BUILD_STORAGEUNIT:
          priority = 8;
          break;
      case BUILD_SHIP:
    	priority = 200;
    	break;
      case KILL:
    	priority = 252;
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
    REST,
    EAT,
    RELOCATE,
    CHOP_TREE,
    GATHER_STONE,
    GATHER_BERRIES,
    BUILD_SLEEPHOUSE,
    BUILD_REPRODUCTIONHOUSE,
    BUILD_STORAGEUNIT,
    KILL,
    DEPOSIT_ITEMS,
    FETCH_ITEMS,
    CRAFT_ITEMS,
    BUILD_SHIP;
  }
  
  public Type getType() {
	  return type;
  }

  short priority;
  long timestamp;
  Type type;
}

