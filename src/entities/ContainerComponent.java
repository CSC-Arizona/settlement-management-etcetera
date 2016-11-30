package entities;

import java.util.Vector;

public class ContainerComponent extends Component {
  public ContainerComponent(int maxCapacity){
  	super(CONTAINER);
    items = new Vector<Item>();
    this.maxCapacity = maxCapacity;
  }

  int maxCapacity;
  public Vector<Item> items;
}

