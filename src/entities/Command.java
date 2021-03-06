package entities;

import java.io.Serializable;
import java.util.EnumMap;

import utility.Vec2f;

public class Command extends State implements Serializable {
  public Command(Type cType, Vec2f location, long timestamp){
    super(cType, timestamp);
    this.location = location;
    desiredItems = null;
    reqItems = null;
    type = getType();
    setReqItems();
  }

  public Command(Type cType, Vec2f location, long timestamp, EnumMap<Item, Integer> desiredItems){
    super(cType, timestamp);
    this.location = location;
    this.desiredItems = desiredItems;
    reqItems = null;
    type = getType();
    setReqItems();
  }
  
  public Command(Type cType, Entity target, long timestamp){
	super(cType, timestamp);
	this.desiredItems = desiredItems;
	this.target = target;
	reqItems = null;
	type = getType();
	setReqItems();
  }

  private void setReqItems(){
    switch(type){
      case KILL:
    	reqItems = new EnumMap<Item, Integer>(Item.class);
        reqItems.put(Item.SWORD, 1);
    	break;
      case BUILD_SLEEPHOUSE:
        reqItems = new EnumMap<Item, Integer>(Item.class);
        reqItems.put(Item.WOOD, 3);
        reqItems.put(Item.AXE, 1);
        break;
      case BUILD_REPRODUCTIONHOUSE:
          reqItems = new EnumMap<Item, Integer>(Item.class);
          reqItems.put(Item.WOOD, 3);
          reqItems.put(Item.AXE, 1);
          break;
      case BUILD_STORAGEUNIT:
          reqItems = new EnumMap<Item, Integer>(Item.class);
          reqItems.put(Item.WOOD, 3);
          reqItems.put(Item.AXE, 1);
          break;
      case BUILD_SHIP:
    	reqItems = new EnumMap<Item, Integer>(Item.class);
        reqItems.put(Item.WOOD, 3);
        reqItems.put(Item.AXE, 1);    	  
        break;
      case CRAFT_ITEMS:
      	// TODO: Since putAll overwrites the previous values, we might have to
        // take care of same items being required in the future.
        reqItems = new EnumMap<Item, Integer>(Item.class);
        for(Item i : desiredItems.keySet())
          reqItems.putAll(i.getRecipe());
        break;
      default:
      	break;
    }
  }
  
  public Type type;
  Vec2f location;
  public Entity target;
  EnumMap<Item, Integer> reqItems;
  EnumMap<Item, Integer>  desiredItems;
}

