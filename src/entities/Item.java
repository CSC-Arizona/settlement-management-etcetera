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

<<<<<<< HEAD
  // TODO: which lines? -rob
  //public Type type;
=======
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264
  static final EnumMap<Item, Integer> axeRecipe = new EnumMap<Item, Integer>(Item.class){{
    put(WOOD, 2);
  }};
}

