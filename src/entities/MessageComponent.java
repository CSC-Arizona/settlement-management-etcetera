package entities;

import java.util.Stack;

public class MessageComponent extends Component {
  public MessageComponent(){
<<<<<<< HEAD
	// TODO: this was set as MESSAGE but I changed it to NAME during merge. correct? -rob
=======
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264
    super(MESSAGE);
    messages = new Stack<String>();
  }

  Stack<String> messages;
}

