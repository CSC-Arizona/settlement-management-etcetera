package entities;

import java.util.Stack;

public class MessageComponent extends Component {
  public MessageComponent(){
    super(MESSAGE);
    messages = new Stack<String>();
  }

  Stack<String> messages;
}

