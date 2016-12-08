package entities;

public class AnimalComponent extends Component {
  public AnimalComponent(){
    super(ANIMAL);
    HP = 100.0f;
    maxHP = 100.0f;
    hit = false;
    dead = false;
  }
  
  public void toggleHit() {
	  hit = hit ? false : true;
  }

  public float HP;
  public float maxHP;
  public boolean hit;
  public boolean dead;

}