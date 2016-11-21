package entities;

import java.util.Stack;

public class CommandableComponent extends Component {
  public CommandableComponent(){
   super(COMMANDABLE);
   commands = new Stack<Command>();
  }
  
  Stack<Command> commands;
}

