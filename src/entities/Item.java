package entities;

import java.io.Serializable;

public class Item implements Serializable {
  public Item(Type t){
    type = t;
  }

  public enum Type {
    WOOD;
  }

  Type type;
}

