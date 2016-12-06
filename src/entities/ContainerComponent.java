package entities;

import java.util.EnumMap;

public class ContainerComponent extends Component {
  public ContainerComponent(int maxCapacity, boolean startAxe){
  	super(CONTAINER);
    items = new EnumMap<Item, Integer>(Item.class);
    this.maxCapacity = maxCapacity;
    startAxe(startAxe);
  }
  
  public void startAxe(boolean x) {
	  if (x) items.put(Item.AXE, 1);
  }
  
  public EnumMap<Item, Integer> getItems() {
	  return items;
  }
  
  public int getMax() {
	  return maxCapacity;
  }

  int maxCapacity;
  EnumMap<Item, Integer> items;
}

