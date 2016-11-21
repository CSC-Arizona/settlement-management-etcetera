package entities;

import utility.Vec2f;

public class Command {
  public Command(Type cType, Vec2f location){
    this.type = cType;
    this.location = location;
  }

  public enum Type {
    RELOCATE,
    CHOP_TREE;
  }
  
  Type type;
  Vec2f location;
}

