package entities;

import java.util.EnumMap;

import utility.Vec2f;


/***************************************
 * Command extends State
 */

public class Command extends State {
  public Command(Type cType, Vec2f location){
    super(cType);
    this.location = location;
    desiredItems = null;
    reqItems = null;
    setReqItems();
  }

  public Command(Type cType, Vec2f location, EnumMap<Item, Integer> desiredItems){
    super(cType);
    this.location = location;
    this.desiredItems = desiredItems;
    reqItems = null;
    setReqItems();
  }

  private void setReqItems(){
    switch(type){
      case BUILD_HOUSE:
        reqItems = new EnumMap<Item, Integer>(Item.class);
        reqItems.put(Item.WOOD, 3);
        reqItems.put(Item.AXE, 1);
        break;
      default:
      	break;
    }
  }
  
  Vec2f location;
  EnumMap<Item, Integer> reqItems;
  EnumMap<Item, Integer>  desiredItems;
}

