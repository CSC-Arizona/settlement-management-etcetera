package entities;

import java.util.EnumMap;

public class ContainerComponent extends Component {
  public ContainerComponent(int maxCapacity){
  	super(CONTAINER);
    items = new EnumMap<Item, Integer>(Item.class);
    this.maxCapacity = maxCapacity;
    items.put(Item.AXE, 1);
  }

  int maxCapacity;
  EnumMap<Item, Integer> items;
}

