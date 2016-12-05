package entities;

import java.util.Vector;

public class MessageSystem extends System {
  public MessageSystem(){
<<<<<<< HEAD
	  // TODO: rob changed this from message to name
=======
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264
    super(Component.MESSAGE);
    messages = new Vector<String>();
  }

  public void tick(){
    updateEntityVector();
    messages.clear();
    for(Entity e : entitiesToProcess){
      MessageComponent eMess =
<<<<<<< HEAD
    		  // TODO: ditto here
=======
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264
        (MessageComponent)eManager.getComponent(Component.MESSAGE, e);
      while(!eMess.messages.isEmpty())
        messages.add(e.getID() + " " + eMess.messages.pop());
    }
  }

  public Vector<String> getMessages(){
    return messages;
  }

  private Vector<String> messages;
}

