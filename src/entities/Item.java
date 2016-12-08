package entities;

import java.io.Serializable;
import java.util.EnumMap;

public enum Item implements Serializable {
  WOOD("WOOD",false, false),
  STONE("STONE",false,false),
  BERRY("BERRY",false,false),
  MEAT("MEAT",false,false),
  AXE("AXE",true, true),
  SWORD("SWORD",true,true);

  private Item(String name, boolean craftable, boolean tool){
    this.name = name;
	isCraftable = craftable;
    isTool = tool;
  }
  
  public String getName() {
	  return name;
  }
  
  public EnumMap<Item, Integer> getRecipe(){
  	switch(this){
  		case AXE:
  			return axeRecipe;
  		case SWORD:
  			return swordRecipe;
  		default:
  			return null;
  	}
  }
  
  final String name;
  final boolean isCraftable;
  final boolean isTool;

  static final EnumMap<Item, Integer> axeRecipe = new EnumMap<Item, Integer>(Item.class){{
    put(WOOD, 2);
  }};
  static final EnumMap<Item, Integer> swordRecipe = new EnumMap<Item, Integer>(Item.class){{
	put(STONE, 2);
  }};
}

