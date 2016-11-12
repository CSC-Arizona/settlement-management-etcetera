package entities;

public class CollisionComponent extends Component {
  public CollisionComponent(){
    super(Component.COLLISION);
    width = 0.7f;
    height = 0.7f;
    restitution = 0.5f;
  }

  public CollisionComponent(float width, float height, float restitution){
    super(Component.COLLISION);
    this.width = width;
    this.height = height;
    this.restitution = restitution;
  }

  float width;
  float height;
  float restitution;
}

