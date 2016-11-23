package entities;

public class Item {
  public Item(Type t){
    type = t;
  }

  public enum Type {
    WOOD;
  }

  Type type;
}

