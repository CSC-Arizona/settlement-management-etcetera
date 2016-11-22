/*
 * Randomly selects a destination and goes there.
 */
package entities;

import java.nio.file.Path;
//import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import utility.Vec2f;
import world.World;

public class AISystem extends Systems {

  public AISystem() {
    super(Component.AI | Component.POSITION | Component.MOBILITY);
  }

  public void tick() {
    updateEntityVector();
    for(Entity e : entitiesToProcess){
      /*PositionComponent pc =
        (PositionComponent)eManager.getComponent(Component.POSITION, e);
      AIComponent ac =
        (AIComponent)eManager.getComponent(Component.AI, e);
      CommandableComponent cc =
        (CommandableComponent)eManager.getComponent(Component.COMMANDABLE, e);
      MobilityComponent mc =
        (MobilityComponent)eManager.getComponent(Component.MOBILITY, e);
      
      if(cc != null && !cc.commands.isEmpty() && ac.state.priority < 16){
        proccessCommands(ac, cc, pc);
      }else{
        LivingComponent lc =
          (LivingComponent)eManager.getComponent(Component.LIVING, e);

        
        // Change the state TODO: should be it's own method
        if(lc != null){
          java.lang.System.out.println(lc.HP + " w: " + lc.hydration);

          if(lc.hydration < 30.0f)
            ac.state = AIComponent.State.FIND_WATER;
          else if(lc.hydration >= 100.0f)
            ac.state = AIComponent.State.WANDER;
        }
        
        switch(ac.state){
          case WANDER:
            if(r.nextInt(100) == 42){
              ac.destination = pc.pos.sub(new Vec2f((r.nextFloat() - 1.0f) * 3,
                  (r.nextFloat() - 1.0f) * 3));
            }
            break;
          case FIND_WATER:
            ac.destination = find(/*TileType.WATER);
            if(ac.destination.sub(pc.pos).getMag() <= CLOSE_ENOUGH)
              lc.hydration += 5.0f;
            break;
        }
      }
      goStraight(mc, pc, ac);
      */
      
      PositionComponent ps = (PositionComponent) eManager.getComponent(Component.POSITION, e);
      Vec2f location = new Vec2f(ps.pos.x, ps.pos.y);
      AIComponent ac = ((AIComponent) eManager.getComponent(Component.AI, e));
      MobilityComponent mc = (MobilityComponent) eManager.getComponent(Component.MOBILITY, e);

      // If we are close enough to our destination, terminate the velocity of the component.
      if(isReallyClose(location, ac.destination)){
        mc.velocity = new Vec2f(0.0f, 0.0f);
        // For the purpose of testing, keep generating new destinations
        ac.destination = new Vec2f(r.nextFloat() * 20, r.nextFloat() * 20);
        //ac.destination = new Vec2f(r.nextFloat() % 20, r.nextFloat() % 20);
      // Get the location super close to the destination
      }else if(isPrettyClose(location, ac.destination)){
    	  
        mc.velocity = getVelocity(location, ac.destination);
      // If there is a desired destination but no path to get there, or if the destination has changed, generate a path
      }else if(ac.path == null){
        ac.path = getPath(roundVector(location), roundVector(ac.destination));
      }else{
        if(isReallyClose(location, ac.path.get(1)))
          ac.path.remove(0);
        if(ac.path.size() != 1){
        mc.velocity = getVelocity(location, ac.path.get(1));
        } else{
        ac.path = null;
        }
      }
      
      if(location.x < -0.1 || location.y < -0.1 || location.x > World.WORLD_SIZE+0.1 || location.y > World.WORLD_SIZE+0.1){
    	  System.out.println("ID: " + e.getID() + "   Location: (" + location.x + ", " + location.y + ")   Velocity: (" + 
        	      mc.velocity.x + ", " + mc.velocity.y + ")   Destination: (" + ac.destination.x + ", " + ac.destination.y + ")");
      }
    }
  }  

  private void proccessCommands(AIComponent ac, CommandableComponent cc, PositionComponent pc){
    if(cc.commands.size() != 0){
      Command cur = cc.commands.peek();
      float distance = (cur.location.sub(pc.pos)).getMag();
      switch(cur.type){
        case RELOCATE:
          if(distance > CLOSE_ENOUGH)
            ac.destination = cur.location;
          else
            cc.commands.pop();
          break;
        case CHOP_TREE:
          if(distance > CLOSE_ENOUGH){
            ac.destination = cur.location;
          }else{
            // TODO: do the chopping
            cc.commands.pop();
          }
          break;
      }
    }
  }

  // Returns the rounded integer version of a given Vec2f
  private Vec2f roundVector(Vec2f initial){
    float x = (int)(initial.x);
    float y = (int)(initial.y);
    if(x == -1)
      x = 0;
    if( y == -1)
      y = 0;
    if(x == World.WORLD_SIZE)
      x = World.WORLD_SIZE - 1;
    if(y == World.WORLD_SIZE)
      y = World.WORLD_SIZE - 1;
    //return new Vec2f(Math.round(initial.x), Math.round(initial.y));
    return new Vec2f(x, y);
  }
  
  // Decides if the current location is pretty close to the destination
  private boolean isPrettyClose(Vec2f location, Vec2f destination){
    return Math.abs(location.sub(destination).getMag()) < 1.0f;
  } 
  
  //Decides if the current location is pretty close to the destination
  private boolean isReallyClose(Vec2f location, Vec2f destination){
    return Math.abs(location.sub(destination).getMag()) < 0.1f;
  } 
  
  //Generates the graph for getPath to use
  public Node[][] getGraph(){
	  Node[][] graph = new Node[World.WORLD_SIZE][World.WORLD_SIZE];
    World w = World.getWorld();
	  // Put a Vec2f in each spot for getPath to use later
	  for(int y = 0; y < graph.length; y++){
	    for(int x = 0; x < graph[y].length; x++){
	      graph[y][x] = new Node(new Vec2f(x, y));
        graph[y][x].isBlocked = !w.getTile(y, x).isPassable();
	    }
	  }

    /*
	  // Make the locations that have a CollisionComponent and a PositionComponent but no MobilityComponent null
	  Vector<Entity> entitiesToAvoid = eManager.getMatchingEntities(Component.POSITION | Component.COLLISION, Component.MOBILITY);
	  for(Entity e: entitiesToAvoid){
	    PositionComponent ps = (PositionComponent) eManager.getComponent(Component.POSITION, e);
	    //System.out.println("(" + ps.x + ", " + ps.y + ") ");
	    int x = Math.round(ps.pos.x);
	    int y = Math.round(ps.pos.y);
	    if(x == World.WORLD_SIZE)
	      x = World.WORLD_SIZE - 1;
	    if( x == -1)
	      x = 0;
	    if(y == World.WORLD_SIZE)
	      y = World.WORLD_SIZE - 1;
	    if(y == -1)
	      y = 0;
	    graph[y][x].isBlocked = true;
	  }
    */
	  
  return graph;
 }
  
  // Calculates the quickest path from the current location to the desired location. I implement Djikstra's algorithm
  private Vector<Vec2f> getPath(Vec2f start, Vec2f end){
    Node[][] graph = getGraph();              // Get the graph that represents the board
    Vector<Vec2f> path = new Vector<Vec2f>();        // Initialize the path vector
    Vector<Node> unvisitedNodes = new Vector<Node>();    // Create a list of unvisitedNodes
    for(int i = 0; i < graph.length; i++){
      for(int j = 0; j < graph[i].length; j++){
        if(!graph[i][j].isBlocked)
          unvisitedNodes.add(graph[i][j]);
      }
    }
    Node first = graph[(int) start.y][(int) start.x];
    first.distanceFromStart = 0;
    Node current = first;
  
    while(true){
      int yless = (int) current.location.y - 1;
      int y = (int) current.location.y;
      int ymore = (int) current.location.y + 1;
      int xless = (int) current.location.x - 1;
      int x = (int) current.location.x;
      int xmore = (int) current.location.x + 1;
      
      float longIncrement = (float) Math.sqrt(2);
      float shortIncrement = 1;
      
      changeDistance(yless, xless, graph, current, longIncrement);
      changeDistance(yless, x, graph, current, shortIncrement);
      changeDistance(yless, xmore, graph, current, longIncrement);
      changeDistance(y, xless, graph, current, shortIncrement);
      changeDistance(y, xmore, graph, current, shortIncrement);
      changeDistance(ymore, xless, graph, current, longIncrement);
      changeDistance(ymore, x, graph, current, shortIncrement);
      changeDistance(ymore, xmore, graph, current, longIncrement);
      
      current.isVisited = true;
      unvisitedNodes.remove(current);
      
      if(isPrettyClose(current.location, end))
        break;
      
      current = getNextNode(unvisitedNodes);
        
      if(current == null)
      return null;
    }
  
    while(true){
      path.add(0, current.location);
      if(current == first)
        break;
      current = current.prev;
    }
    return path; 
  }
  
  // Updates the distanceFromStart value of a node based on new information
  private void changeDistance(int yVal, int xVal, Node[][]graph, Node current, float increment){
    try{
      if(graph[yVal][xVal].isBlocked){
        ;
      }else if(graph[yVal][xVal].distanceFromStart == -1){
        graph[yVal][xVal].distanceFromStart = (current.distanceFromStart + increment);
        graph[yVal][xVal].prev = current;
      }else{
        if((current.distanceFromStart + increment) < graph[yVal][xVal].distanceFromStart){
          graph[yVal][xVal].distanceFromStart = current.distanceFromStart + increment;
          graph[yVal][xVal].prev = current;
        }
      }
    }catch(IndexOutOfBoundsException e){
      //e.printStackTrace();
    }
  }
  
  // Gets the next node to check in the algorithm
  public Node getNextNode(Vector<Node> unvisitedNodes){
    float shortestDistance = -1;
    Node current = null;
    for(Node node: unvisitedNodes){                // Get the next node
      if(node.distanceFromStart == -1){
        ;  // Do nothing
      }else if(shortestDistance == -1){
        shortestDistance = node.distanceFromStart;
        current = node;
      }else if(node.distanceFromStart < shortestDistance){
        shortestDistance = node.distanceFromStart;
        current = node;
      }
    }
    return current;
  }
  
  // This class represents a node on the graph. All it really does is bind the data aspects of the node together
  private class Node{
    
    Vec2f location;
    boolean isBlocked;
    float distanceFromStart;
    boolean isVisited;
    Node prev;
    
    public Node(Vec2f location){
      this.location = location;
      isBlocked = false;
      distanceFromStart = -1;                    // -1 Represents a distance of infinity
      isVisited = false;
      prev = null;
    }
  }
  
  // Calculates the new velocity vector of the component from the two given points on the path
  private Vec2f getVelocity(Vec2f start, Vec2f end){
    // We first need to calculate the direction vector from where we
    // are to where we are going.
    Vec2f newVel = end.sub(start);
    // Dividing the resulting vector by it's magnitude gives us a vector
    // pointing in the right direction with
    // magnitude of 1 (m/s).
    newVel = newVel.mul(1 / newVel.getMag());
    // Here we adjust the speed to be 1.5 m/s
    newVel = newVel.mul(1.5f);
    return newVel;
  }

  private void goStraight(MobilityComponent mc, PositionComponent pc, AIComponent ac){
    if(ac.destination.sub(pc.pos).getMag() > CLOSE_ENOUGH){
      // We first need to calculate the direction vector from where we
      // are to where we are going.
      Vec2f newVel = ac.destination.sub(pc.pos);
      // Dividing the resulting vector by it's magnitude gives us a vector
      // pointing in the right direction with
      // magnitude of 1 (m/s).
      newVel = newVel.mul(1 / newVel.getMag());
      // Here we adjust the speed to be 1.5 m/s
      newVel = newVel.mul(1.5f);
      mc.velocity = newVel;
    }else{
      mc.velocity = new Vec2f(0.0f, 0.0f);
    }
  }
  
  // TODO: Should be find(Tile t)
  private Vec2f find(){
    return new Vec2f(1.0f, 1.0f);
  }

  private static Random r = new Random();
  private static final float CLOSE_ENOUGH = 1.0f;
}
