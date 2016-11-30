
package entities;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class CommandSystem extends System {
  public CommandSystem(){
    super(Component.COMMANDABLE | Component.AI);
    commands = new ArrayDeque<Command>();
  }

  public void tick(){
  	updateEntityVector();
    for(Entity e : entitiesToProcess){
    	if(commands.isEmpty())
    		break;
      
    	AIComponent ac =
    			(AIComponent)eManager.getComponent(Component.AI, e);
    	if(!(ac.states.peek() instanceof Command))
        ac.states.add(commands.poll());
    }

    if(!commands.isEmpty()){
      for(Entity e : entitiesToProcess){
      	if(commands.isEmpty())
      		break;
      	
      	AIComponent ac =
      			(AIComponent)eManager.getComponent(Component.AI, e);
        ac.states.add(commands.poll());
      }
    }
  }

  public void addCommands(Stack<Command> commands){
    this.commands.addAll(commands);
  }

  private Queue<Command> commands;
}

