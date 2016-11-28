package entities;

import java.util.EnumMap;

public class ContainerComponent extends Component {
  public ContainerComponent(int maxCapacity){
  	super(CONTAINER);
    items = new EnumMap<Item, Integer>(Item.class);
    this.maxCapacity = maxCapacity;
  }

  int maxCapacity;
  EnumMap<Item, Integer> items;
}

