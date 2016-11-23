package entities;

import java.util.ArrayDeque;
import java.util.Queue;

public class CommandableComponent extends Component {
  public CommandableComponent(){
   super(COMMANDABLE);
   commands = new ArrayDeque<Command>();
  }
  
  Queue<Command> commands;
}

