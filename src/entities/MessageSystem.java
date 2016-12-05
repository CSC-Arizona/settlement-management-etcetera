package entities;

import java.util.Vector;

public class MessageSystem extends System {
  public MessageSystem(){
    super(Component.MESSAGE);
    messages = new Vector<String>();
  }

  public void tick(){
    updateEntityVector();
    messages.clear();
    for(Entity e : entitiesToProcess){
      MessageComponent eMess =
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

