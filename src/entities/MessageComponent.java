package entities;

import java.util.Stack;

public class MessageComponent extends Component {
  public MessageComponent(){
	// TODO: this was set as MESSAGE but I changed it to NAME during merge. correct? -rob
    super(MESSAGE);
    messages = new Stack<String>();
  }

  Stack<String> messages;
}

