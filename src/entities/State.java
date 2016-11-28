package entities;

public class State {
  public State(Type t){
    type = t;
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
        priority = 8;
        break;
      case FETCH_ITEMS:
        priority = 8;
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
    FETCH_ITEMS;
  }

  short priority;
  Type type;
}

