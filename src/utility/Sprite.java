package utility;

public enum Sprite {
  ALIEN(232, 12),
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
  SLEEPHOUSE(65, 14),
  REPRODUCTIONHOUSE(69, 1),
  STORAGEUNIT(70, 10),
  SHIP(164, 1),
	
  DEER(248, 11);

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
  
  private int x;
  private int y;
  public static final int WIDTH = 32;
  public static final int HEIGHT = 32;
}

