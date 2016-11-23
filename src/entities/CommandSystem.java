
package entities;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class CommandSystem extends System {
  public CommandSystem(){
    super(Component.COMMANDABLE);
    commands = new ArrayDeque<Command>();
  }

  public void tick(){
  	updateEntityVector();
    for(Entity e : entitiesToProcess){
    	if(commands.isEmpty())
    		break;
      CommandableComponent cc =
        (CommandableComponent)eManager.getComponent(Component.COMMANDABLE, e);
      if(cc.commands.isEmpty())
        cc.commands.add(commands.poll());
    }

    if(!commands.isEmpty()){
      for(Entity e : entitiesToProcess){
      	if(commands.isEmpty())
      		break;
        CommandableComponent cc =
          (CommandableComponent)eManager.getComponent(Component.COMMANDABLE, e);
        cc.commands.add(commands.poll());
      }
    }
  }

  public void addCommands(Stack<Command> commands){
    this.commands.addAll(commands);
  }

  private Queue<Command> commands;
}

