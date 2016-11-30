package entities;

import java.io.Serializable;
import java.util.EnumMap;

public enum Item implements Serializable {
  WOOD(false, false),
  AXE(true, true);

  private Item(boolean craftable, boolean tool){
    isCraftable = craftable;
    isTool = tool;
  }
  
  public EnumMap<Item, Integer> getRecipe(){
  	switch(this){
  		case AXE:
  			return axeRecipe;
  		default:
  			return null;
  	}
  }
  
  final boolean isCraftable;
  final boolean isTool;

  static final EnumMap<Item, Integer> axeRecipe = new EnumMap<Item, Integer>(Item.class){{
    put(WOOD, 2);
  }};
}
