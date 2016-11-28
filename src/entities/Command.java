package entities;

import java.io.Serializable;

import utility.Vec2f;

public class Command implements Serializable {
  public Command(Type cType, Vec2f location){
    this.type = cType;
    this.location = location;
  }

  public enum Type {
    RELOCATE,
    CHOP_TREE,
    BUILD_HOUSE,
    DEPOSIT_ITEMS,
    GET_WOOD;
  }
  
  Type type;
  Vec2f location;
}

