package utility;

public enum Sprite {
  ALIEN(15, 7),
  AMMO(1, 1),
  GODZILLA(19, 4),
  BANDAGE(13, 0),
  BLOOD_POOL(47, 0),
  
  BUSH(68, 9),
  CRATER(134, 13),
  DIRT(40, 0),
  GRASS(128, 2),
  LAKE(141, 15),
  ROCK(225, 3),
  TREE(135, 15),
  HOUSE(70, 10),
  SHIP(164, 1);

  private Sprite(int row, int col){
    x = col * WIDTH;
    y = row * HEIGHT;
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }
  
  public String toString(){
	  if(this == Sprite.BUSH) return "bush";
	  else if(this == Sprite.CRATER) return "crater";
	  else if(this == Sprite.DIRT) return "dirt";
	  else if(this == Sprite.GRASS) return "grass";
	  else if(this == Sprite.LAKE) return "lake";
	  else if(this == Sprite.ROCK) return "rock";
	  else if(this == Sprite.TREE) return "tree";
	  else return "Something went wrong";
  }
  
  private int x;
  private int y;
  public static final int WIDTH = 32;
  public static final int HEIGHT = 32;
}

