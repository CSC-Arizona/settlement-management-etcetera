package utility;

public enum Sprite {
  ALIEN(15, 7),
  AMMO(1, 1),
  GODZILLA(19, 4),
  BANDAGE(13, 0),
  BLOOD_POOL(47, 0),
  
  BUSH(137, 5),
  CRATER(130, 1),
  DIRT(40, 0),
  GRASS(128, 2),
  LAKE(141, 15),
  ROCK(139, 11),
  TREE(135, 15);

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

