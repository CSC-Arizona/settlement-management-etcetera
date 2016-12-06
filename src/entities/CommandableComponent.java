// TODO: delete this file if we dont need it

package entities;

import java.util.ArrayDeque;
import java.util.Queue;

public class CommandableComponent extends Component {
  public CommandableComponent(){
   super(COMMANDABLE);
   commands = new ArrayDeque<Command>();
  }
  
  public Queue<Command> commands;
}