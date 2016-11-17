package entities;

public class LivingComponent extends Component {
  public LivingComponent(){
    super(LIVING);
    hydration = 10.0f;
    HP = 100.0f;
    maxHP = 150;
  }

  float hydration;
  float HP;
  float maxHP;
}

