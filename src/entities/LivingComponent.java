package entities;

public class LivingComponent extends Component {
  public LivingComponent(){
    super(LIVING);
    hydration = 100.0f;
    maxHydration = 100.0f;
    poorHydration = 30.0f;
    criticalHydration = 25.0f;
    drinkHydration = poorHydration + 10.0f;
    
    restVal = 100.0f;
    maxRestVal = 100.0f;
    poorRestVal = 30.0f;
    criticalRestVal = 25.0f;
    sleepRestVal = poorRestVal + 10.0f;
    
    hungerVal = 100.0f;
    maxHungerVal = 100.0f;
    poorHungerVal = 30.0f;
    criticalHungerVal = 25.0f;
    eatHunger = poorRestVal + 10.0f;

    
    HP = 100.0f;
    maxHP = 100.0f;
  }

  public float HP;
  public float maxHP;
  float goodHP = 90;
  
  public float hydration;
  public float maxHydration;
  float poorHydration;
  float criticalHydration;
  float drinkHydration;				// The value at which the unit should go drink
  float healHydration;				// The value the unit needs to heal
  
  public float restVal;
  public float maxRestVal;
  float poorRestVal;
  float criticalRestVal;
  float sleepRestVal;				// The value at which the unit should go sleep
  float healRestVal;				// The value the unit needs to heal
  
  public float hungerVal;
  public float maxHungerVal;
  float poorHungerVal;
  float criticalHungerVal;
  float eatHunger;				// The value at which the unit should go eat
  float healHungerVal;			// The value the unit needs to heal
}