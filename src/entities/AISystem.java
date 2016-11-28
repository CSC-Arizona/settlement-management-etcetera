/*
 * Randomly selects a destination and goes there.
 */
package entities;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Random;
import java.util.Vector;

import utility.Sprite;
import utility.Vec2f;
import world.Dirt;
import world.World;

public class AISystem extends System {

  public AISystem() {
    super(Component.AI | Component.POSITION | Component.MOBILITY);
  }

  public void tick() {
    updateEntityVector();
    for(Entity e : entitiesToProcess){
    	PositionComponent pc =
        (PositionComponent)eManager.getComponent(Component.POSITION, e);
      AIComponent ac =
        (AIComponent)eManager.getComponent(Component.AI, e);
      MobilityComponent mc =
        (MobilityComponent)eManager.getComponent(Component.MOBILITY, e);
      LivingComponent lc =
        (LivingComponent)eManager.getComponent(Component.LIVING, e);
      ContainerComponent conc =
        (ContainerComponent)eManager.getComponent(Component.CONTAINER, e);
      MessageComponent mssc =
        (MessageComponent)eManager.getComponent(Component.MESSAGE, e);
      
      /* DEBUG */
      mssc.messages.add(" (Alien) has: " + conc.items.get(Item.WOOD) + " wood");
      
      // This handles dwarf's needs
      if(lc != null)
        handleLivingInterrupts(ac, lc, pc);

      // If we don't have items required for the current command -- go get them.
      if(ac.states.peek() instanceof Command && ((Command)ac.states.peek()).reqItems != null){
        Command c = (Command)ac.states.peek();
        EnumMap<Item, Integer> desiredItems = new EnumMap<Item, Integer>(Item.class);
        for(Item item : c.reqItems.keySet()){
          int num = c.reqItems.get(item);
          if(!conc.items.containsKey(item))
            desiredItems.put(item, num);
          else if(conc.items.get(item) < num)
            desiredItems.put(item, num - conc.items.get(item));
          /*
          if(!conc.items.containsKey(item) || conc.items.get(item) < num){
            ac.states.add(new Command(
                  State.Type.FETCH, new Vec2f(1.0f, 1.0f), Item.WOOD));
                  */
        }
        
        Entity ship = eManager.getFirstMatching(Component.SHIP);
        PositionComponent shipPC =
          (PositionComponent)eManager.getComponent(Component.POSITION, ship);
        ac.states.add(new Command(
              State.Type.FETCH_ITEMS, shipPC.pos, desiredItems));
      }

      switch(ac.states.peek().type){
        case WANDER:
          handleWanderState(ac, pc);
          break;
        case FIND_WATER:
          handleFindWaterState(ac, pc);
          break;
        case RELOCATE:
          handleRelocateState(ac, pc);
          break;
        case FETCH_ITEMS:
          handleFetchItemsState(ac, pc, conc);
          break;
        case BUILD_HOUSE:
          handleBuildHouseState();
          break;
        case DEPOSIT_ITEMS:
          handleDepositItemsState(ac, pc, conc);
          break;
        case CHOP_TREE:
          handleChopTree(ac, pc, conc);
          break;
      }
      
/******************************************************************************/
      /*
      if(cc != null && !cc.commands.isEmpty() && ac.state.priority < 16)
      	proccessCommands(ac, cc, pc, conc);
      
      // Handle hydration
      if(ac.state != AIComponent.State.FIND_WATER && lc != null && lc.hydration < 30.0f){
      	ac.state = AIComponent.State.FIND_WATER;
      	ac.path = null;
      }else if(lc.hydration >= 100.0f){
      	ac.state = AIComponent.State.WANDER;
      }
      
      World w = World.getWorld();
      switch(ac.state){
      	case WANDER:
      		if(ac.path == null){
	      		if(r.nextInt(100) == 42){
	      			Vec2f dest;
	      			do{
	      				dest = pc.pos.sub(new Vec2f((r.nextFloat() - 0.5f) * 3, (r.nextFloat() - 0.5f) * 3));
	      			}while(dest.y < 0.0f || dest.x < 0.0f || dest.y >= World.WORLD_SIZE ||
	      						 dest.x >= World.WORLD_SIZE || !w.getTile((int)dest.y, (int)dest.x).isPassable());
	      			ac.path = getPath(roundVector(pc.pos), roundVector(dest));
	      		}
      		}
      		break;
      	case FIND_WATER:
          if(mssc != null)
            mssc.messages.push("is thursty");
      		if(ac.path == null){
      			Vec2f water = findClosest(Sprite.LAKE, pc.pos);
      			if(water != null)
      				ac.path = getPath(roundVector(pc.pos), roundVector(water));
      		}
      		if(w.getTile(Math.round(pc.pos.y), Math.round(pc.pos.x)).getType() == Sprite.LAKE)
      			lc.hydration += 30.0f / TICKS_PER_SECOND;
      		break;
      }
      
    */
      // Shouldn't happen, but does non the less.
      if(ac.path != null && ac.path.size() == 0)
      	ac.path = null;

      if(ac.path == null)
        mc.velocity = new Vec2f(0.0f, 0.0f);

      if(ac.path != null){
      	//java.lang.System.out.println(ac.path.size());
      	/*
        if(ac.path.lastElement().sub(pc.pos).getMag() < CLOSE_ENOUGH){
          mc.velocity = new Vec2f(0.0f, 0.0f);
          ac.path = null;
        }else */
      	if(ac.path.get(0).sub(pc.pos).getMag() < 0.2f){
          ac.path.remove(0);
          if(!ac.path.isEmpty())
          	mc.velocity = getVelocity(pc.pos, ac.path.get(0));
        }else{
          mc.velocity = getVelocity(pc.pos, ac.path.get(0));
        }
      }
    }
/******************************************************************************/
  }  

  private void handleLivingInterrupts(AIComponent ac, LivingComponent lc,
                                      PositionComponent pc){
    if(ac.states.peek().type != State.Type.FIND_WATER &&
       lc.hydration <= lc.poorHydration){
      ac.states.add(new State(State.Type.FIND_WATER));
      ac.path = null;
    }else if(ac.states.peek().type == State.Type.FIND_WATER &&
             lc.hydration >= lc.maxHydration){
      ac.states.poll();
      ac.path = null;
    }
  }

  private void handleWanderState(AIComponent ac, PositionComponent pc){
    World w = World.getWorld();
    if(ac.path == null){
      if(r.nextInt(100) == 42){
        Vec2f dest;
        do{
          dest = pc.pos.sub(new Vec2f((r.nextFloat() - 0.5f) * 3,
                                      (r.nextFloat() - 0.5f) * 3));
        }while(dest.y < 0.0f || dest.x < 0.0f || dest.y >= World.WORLD_SIZE ||
               dest.x >= World.WORLD_SIZE ||
               !w.getTile((int)dest.y, (int)dest.x).isPassable());
        ac.path = getPath(roundVector(pc.pos), roundVector(dest));
      }
    }
  }

  private void handleFindWaterState(AIComponent ac, PositionComponent pc){
    if(ac.path == null){
      Vec2f waterLoc = findClosest(Sprite.LAKE, pc.pos);
      if(waterLoc != null)
        ac.path = getPath(roundVector(pc.pos), roundVector(waterLoc));
      else
        ac.states.poll();
    }
  }

  private void handleRelocateState(AIComponent ac, PositionComponent pc){
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      ac.states.poll();
      ac.path = null;
    }
  }

  private void handleFetchItemsState(AIComponent ac, PositionComponent pc,
      ContainerComponent ecc){
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      ContainerComponent scc = (ContainerComponent)eManager.getComponent(
          Component.CONTAINER, eManager.getFirstMatching(Component.SHIP));
      // scc.items -- ship inventory        | EnumMap
      // ecc.desiredItems -- desired items  | EnumMap
      boolean abortPrevCommand = false;
      for(Item item : command.desiredItems.keySet()){
        if(scc.items.containsKey(item)){
          int oldVal = ecc.items.get(item) == null ? 0 : ecc.items.get(item);
          if(scc.items.get(item) < command.desiredItems.get(item)){
            ecc.items.put(item, scc.items.get(item) + oldVal);
            scc.items.remove(item);
            abortPrevCommand = true;
          }else if(scc.items.get(item) > command.desiredItems.get(item)){
            ecc.items.put(item, command.desiredItems.get(item) + oldVal);
            scc.items.put(item, scc.items.get(item) -
                          command.desiredItems.get(item));
          }else{  // Ship has the exact amount of the item we need
            ecc.items.put(item, scc.items.remove(item) + oldVal);
          }
        }else{
          // Ship doesn't have the needed item ABORT
          abortPrevCommand = true;
        }
      }
      ac.states.poll();
      if(abortPrevCommand)
        ac.states.poll();
      ac.path = null;
    }
  }

  private void handleDepositItemsState(AIComponent ac, PositionComponent pc,
      ContainerComponent ecc){
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      ContainerComponent scc = (ContainerComponent)eManager.getComponent(
          Component.CONTAINER, eManager.getFirstMatching(Component.SHIP));
      for(Item item : command.desiredItems.keySet()){
        int oldVal = scc.items.get(item) == null ? 0 : scc.items.get(item);
        scc.items.put(item, command.desiredItems.get(item) + oldVal);
        java.lang.System.out.println(">>>> " + command.desiredItems.get(item));
        if(ecc.items.get(item) > command.desiredItems.get(item))
          ecc.items.put(item, ecc.items.get(item) - command.desiredItems.get(item));
        else
          ecc.items.remove(item);
      }
      ac.states.poll();
      ac.path = null;
    }
  }

  private void handleBuildHouseState(){
  }

  private void handleChopTree(AIComponent ac, PositionComponent pc,
      ContainerComponent ecc){
    final int NUM_LOGS_FROM_TREE = 1;
    Command command = (Command)ac.states.peek();
    World w = World.getWorld();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      w.setTile((int)command.location.y, (int)command.location.x,
                new Dirt((int)command.location.y, (int)command.location.x));
      int oldVal = ecc.items.get(Item.WOOD) == null ? 0 : ecc.items.get(Item.WOOD);
      ecc.items.put(Item.WOOD, NUM_LOGS_FROM_TREE + oldVal);

      ac.states.poll();
      ac.path = null;

      if(ecc.items.get(Item.WOOD) >= ecc.maxCapacity){
        EnumMap<Item, Integer> itemsToDeposit =
          new EnumMap<Item, Integer>(Item.class);
        itemsToDeposit.put(Item.WOOD, ecc.items.get(Item.WOOD));
        PositionComponent spc = (PositionComponent)eManager.getComponent(
            Component.POSITION, eManager.getFirstMatching(Component.SHIP));
        ac.states.add(new Command(
              State.Type.DEPOSIT_ITEMS, spc.pos, itemsToDeposit));
      }
    }
  }
  
  /*

  private void proccessCommands(AIComponent ac, CommandableComponent cc,
                                PositionComponent pc, ContainerComponent conc){
    if(cc.commands.size() != 0){
      Command cur = cc.commands.peek();
      World w = World.getWorld();
      float distance = (cur.location.sub(pc.pos)).getMag();
    	final int WOOD_REQUIRED = 2;
      switch(cur.type){
        case BUILD_HOUSE:
        	java.lang.System.out.println("House " + conc.items.size());
        	if(conc.items.size() < WOOD_REQUIRED){
        		Command tmp = cc.commands.poll();
        		cc.commands.add(
                new Command(Command.Type.GET_WOOD, new Vec2f(1.0f, 1.0f)));
        		cc.commands.add(tmp);
        	}else if(distance > CLOSE_ENOUGH && ac.path == null){
            ac.path = getPath(roundVector(pc.pos), roundVector(cur.location));
        	}else if(distance <= CLOSE_ENOUGH){
            cc.commands.poll();
            EntityFactory.makeNewHouse(cur.location.x, cur.location.y);
            conc.items.remove(0);
            conc.items.remove(0);
          }
          break;
        case RELOCATE:
          if(distance > CLOSE_ENOUGH && ac.path == null)
            ac.path = getPath(roundVector(pc.pos), roundVector(cur.location));
          else if(distance <= CLOSE_ENOUGH)
            cc.commands.poll();
          break;
        case CHOP_TREE:
          if(distance > CLOSE_ENOUGH && ac.path == null){
            ac.path = getPath(roundVector(pc.pos), roundVector(cur.location));
          }if(distance <= CLOSE_ENOUGH){
            w.setTile((int)cur.location.y, (int)cur.location.x,
                new Dirt((int)cur.location.y, (int)cur.location.x));
            //ac.path = getPath(roundVector(pc.pos), new Vec2f(0,0));
            conc.items.add(new Item(Item.Type.WOOD));
            cc.commands.poll();
            if(conc.maxCapacity <= conc.items.size())
            	cc.commands.add(new Command(
                    Command.Type.DEPOSIT_ITEMS, new Vec2f(1.0f, 1.0f)));
          }
          break;
        case DEPOSIT_ITEMS:
          if(distance > CLOSE_ENOUGH && ac.path == null){
            ac.path = getPath(roundVector(pc.pos), roundVector(cur.location));
          }else if(distance <= CLOSE_ENOUGH){
            Vector<Entity> ship = eManager.getMatchingEntities(Component.SHIP);
            ContainerComponent scc =
              (ContainerComponent)eManager.getComponent(Component.CONTAINER,
                                                        ship.get(0));
            scc.items.addAll(conc.items);
            conc.items.clear();
            cc.commands.poll();
            java.lang.System.out.println(scc.items.size());
          }
          break;
        case GET_WOOD:
        	java.lang.System.out.println("get wood");
        	if(distance > CLOSE_ENOUGH && ac.path == null){
        		ac.path = getPath(roundVector(pc.pos), roundVector(cur.location));
        	}else if(distance <= CLOSE_ENOUGH){
            Vector<Entity> ship = eManager.getMatchingEntities(Component.SHIP);
            ContainerComponent scc =
              (ContainerComponent)eManager.getComponent(Component.CONTAINER,
                                                        ship.get(0));
            if(scc.items.size() < WOOD_REQUIRED){
            	cc.commands.poll();
            	cc.commands.poll();
            }else{
	            conc.items.add(scc.items.get(0));
	            scc.items.remove(0);
	            conc.items.add(scc.items.get(0));
	            scc.items.remove(0);
	            cc.commands.poll();
	            //cc.commands.poll();
	            java.lang.System.out.println(scc.items.size());
            }
          }
        	break;
      }
    }
  }
  */

  // Returns the rounded integer version of a given Vec2f
  private Vec2f roundVector(Vec2f initial){
    float x = (int)(initial.x);
    float y = (int)(initial.y);
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
      
      try{
        if(!graph[y][xless].isBlocked || !graph[yless][x].isBlocked)
    	  changeDistance(yless, xless, graph, current, longIncrement);
      } catch(IndexOutOfBoundsException e){
    	  // Nothing
      }
      changeDistance(yless, x, graph, current, shortIncrement);
      try{
        if(!graph[yless][x].isBlocked || !graph[y][xmore].isBlocked)
    	  changeDistance(yless, xmore, graph, current, longIncrement);
      } catch(IndexOutOfBoundsException e){
    	  // Nothing
      }
      changeDistance(y, xless, graph, current, shortIncrement);
      changeDistance(y, xmore, graph, current, shortIncrement);
      try{
        if(!graph[y][xless].isBlocked || !graph[ymore][x].isBlocked)
          changeDistance(ymore, xless, graph, current, longIncrement);
      } catch(IndexOutOfBoundsException e){
    	  // Nothing
      }
      changeDistance(ymore, x, graph, current, shortIncrement);
      try{
    	if(!graph[ymore][x].isBlocked || !graph[y][xmore].isBlocked)
    	  changeDistance(ymore, xmore, graph, current, longIncrement);
      } catch(IndexOutOfBoundsException e){
    	  // Nothing
      }
      
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
    path.remove(0);
    return path; 
  }
  
  // Updates the distanceFromStart value of a node based on new information
  private void changeDistance(int yVal, int xVal, Node[][]graph,
                              Node current, float increment){
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
  
  // TODO: Should be find(Tile t)
  private Vec2f findClosest(Sprite s, Vec2f pos){
  	World w = World.getWorld();
  	for(int i = 0; i < World.WORLD_SIZE; ++i){
  		for(int j = (int)pos.x - i; j < (int)pos.x + i; ++j){
  			for(int k = (int)pos.y - i; k < (int)pos.y + i; ++k){
  				if(k >= 0 && k < World.WORLD_SIZE && j >= 0 && j < World.WORLD_SIZE &&
             w.getTile(k, j).getType() == s){
  					return new Vec2f(j, k);
          }
  			}
  		}
  	}
  	return null;
  }

  private static Random r = new Random();
  private static final float CLOSE_ENOUGH = 0.4f;
}
