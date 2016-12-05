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
<<<<<<< HEAD

  // TODO: Which line is correct? -Rob
  //public Vector<Item> items;
=======
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264
  EnumMap<Item, Integer> items;
}

