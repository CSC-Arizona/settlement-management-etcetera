package entities;

public class LivingComponent extends Component {
  public LivingComponent(){
    super(LIVING);
    hydration = 40.0f;
    maxHydration = 100.0f;
    poorHydration = 30.0f;
    criticalHydration = 25.0f;
    HP = 100.0f;
    maxHP = 100.0f;
  }

  float hydration;
  public float HP;
  public float maxHP;
  float maxHydration;
  float poorHydration;
  float criticalHydration;
}