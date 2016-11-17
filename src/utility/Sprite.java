package utility;

public enum Sprite {
  ALIEN(15, 7),
  AMMO(1, 1),
  GODZILLA(19, 4),
  BANDAGE(13, 0),
  BLOOD_POOL(47, 0);

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

  public int getWidth(){
    return WIDTH;
  }
  
  public int getHeight(){
    return HEIGHT;
  }

  private int x;
  private int y;
  private final int WIDTH = 32;
  private final int HEIGHT = 32;
}

