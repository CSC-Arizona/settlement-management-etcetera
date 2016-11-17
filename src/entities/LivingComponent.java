package entities;

public class LivingComponent extends Component {
  public LivingComponent(){
    super(LIVING);
    hydration = 10.0f;
    hp = 10.0f;
  }

  float hydration;
  float hp;
}

