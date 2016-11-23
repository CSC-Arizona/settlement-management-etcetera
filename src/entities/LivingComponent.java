package entities;

public class LivingComponent extends Component {
  public LivingComponent(){
    super(LIVING);
    hydration = 1000.0f;
    HP = 100.0f;
    maxHP = 100;
  }

  float hydration;
  float HP;
  float maxHP;
}

