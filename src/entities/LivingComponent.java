package entities;

public class LivingComponent extends Component {
  public LivingComponent(){
    super(LIVING);
    hydration = 40.0f;
    HP = 100.0f;
    maxHP = 100;
  }

  float hydration;
  public float HP;
  public float maxHP;
}

