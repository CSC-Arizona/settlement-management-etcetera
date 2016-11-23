
package entities;

import java.util.Stack;

public class CommandSystem extends System {
  public CommandSystem(){
    super(Component.COMMANDABLE);
    commands = new Stack<Command>();
  }

  public void tick(){
  	updateEntityVector();
    for(Entity e : entitiesToProcess){
    	if(commands.isEmpty())
    		break;
      CommandableComponent cc =
        (CommandableComponent)eManager.getComponent(Component.COMMANDABLE, e);
      if(cc.commands.isEmpty())
        cc.commands.push(commands.pop());
    }

    if(!commands.isEmpty()){
      for(Entity e : entitiesToProcess){
      	if(commands.isEmpty())
      		break;
        CommandableComponent cc =
          (CommandableComponent)eManager.getComponent(Component.COMMANDABLE, e);
        cc.commands.push(commands.pop());
      }
    }
  }

  public void addCommands(Stack<Command> commands){
    this.commands.addAll(commands);
  }

  private Stack<Command> commands;
}

