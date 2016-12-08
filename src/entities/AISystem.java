/*
 * Randomly selects a destination and goes there.
 */
package entities;

import java.nio.file.Path;	
import java.util.EnumMap;
import java.util.Random;
import java.util.Vector;

//import com.sun.glass.ui.EventLoop.State;

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
      eMan = EntityManager.INSTANCE;
      pc = (PositionComponent)eManager.getComponent(Component.POSITION, e);
      ac = (AIComponent)eManager.getComponent(Component.AI, e);
      mc = (MobilityComponent)eManager.getComponent(Component.MOBILITY, e);
      lc = (LivingComponent)eManager.getComponent(Component.LIVING, e);
      cc = (ContainerComponent)eManager.getComponent(Component.CONTAINER, e);
      msc = (MessageComponent)eManager.getComponent(Component.MESSAGE, e);
      anc = (AnimalComponent)eManager.getComponent(Component.ANIMAL, e);
      
      if(ac.states.isEmpty())
      	ac.states.add(new State(State.Type.WANDER, getTime()));
      
      /* DEBUG */
      //msc.messages.add(" (Alien) has: " + conc.items.get(Item.WOOD) + " wood");
      
      /*
      java.lang.System.out.println("*******************************");
      java.lang.System.out.println("top: " + ac.states.peek().type + " " + ac.path);
      for(State s : ac.states)
      	java.lang.System.out.println(s.type);
      java.lang.System.out.println("*******************************");
      */

      
      // This handles dwarf's needs
      if(lc != null)
        handleLivingInterrupts();

      // If we don't have items required for the current command -- go get them.
      if(ac.states.peek() instanceof Command && ((Command)ac.states.peek()).reqItems != null){
        Command c = (Command)ac.states.peek();
        EnumMap<Item, Integer> desiredItems = new EnumMap<Item, Integer>(Item.class);
        for(Item item : c.reqItems.keySet()){
          int num = c.reqItems.get(item);
          if(!cc.items.containsKey(item))
            desiredItems.put(item, num);
          else if(cc.items.get(item) < num)
            desiredItems.put(item, num - cc.items.get(item));
        }
        if(!desiredItems.isEmpty()){
        	Entity ship = eManager.getClosestMatching(Component.CONTAINER, Component.MOBILITY, c.location);
	        PositionComponent shipPC =
	          (PositionComponent)eManager.getComponent(Component.POSITION, ship);
	        ac.states.add(new Command(
	              State.Type.FETCH_ITEMS, shipPC.pos, getTime(), desiredItems));
        }
      }

      switch(ac.states.peek().type){
        case WANDER:
          handleWanderState();
          break;
        case FIND_WATER:
          handleFindWaterState();
          break;
        case REST:
          handleRestState();
          break;
        case EAT:
          handleEatState();
          break;
        case RELOCATE:
          handleRelocateState();
          break;
        case FETCH_ITEMS:
          handleFetchItemsState();
          break;
        case BUILD_SHIP:
          handleBuildShipState();
          break;
        case BUILD_SLEEPHOUSE:
            handleBuildState(State.Type.BUILD_SLEEPHOUSE);
            break;
          case BUILD_REPRODUCTIONHOUSE:
            handleBuildState(State.Type.BUILD_REPRODUCTIONHOUSE);
            break;
          case BUILD_STORAGEUNIT:
            handleBuildState(State.Type.BUILD_STORAGEUNIT);
            break;
        case DEPOSIT_ITEMS:
          handleDepositItemsState();
          break;
        case CRAFT_ITEMS:
          handleCraftItemsState();
          break;
        case CHOP_TREE:
          handleGather(Item.WOOD, 1);
          break;
        case GATHER_STONE:
          handleGather(Item.STONE, 1);
          break;  
        case GATHER_BERRIES:
            handleGather(Item.BERRY, 1);
            break;   
        case KILL:
          handleKill();
          break;
        default:
        	break;
      }
      
      // Shouldn't happen, but does non the less.
      if(ac.path != null && ac.path.size() == 0)
      	ac.path = null;

      if(ac.path == null)
        mc.velocity = new Vec2f(0.0f, 0.0f);

      if(ac.path != null){
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

  private void handleLivingInterrupts(){
    if(ac.states.peek().type != State.Type.FIND_WATER &&
       (lc.hydration <= lc.drinkHydration || 
          (lc.hydration <= lc.healHydration+10 && lc.HP < lc.goodHP))){
      ac.states.add(new State(State.Type.FIND_WATER, getTime()));
      ac.path = null;
    }		// Here I check to make sure that both FIND_WATER and REST are not in the top
    else if(ac.states.peek().type != State.Type.REST && ac.states.peek().type != State.Type.FIND_WATER
       && (lc.restVal <= lc.sleepRestVal) ||
       		(lc.restVal <= lc.healRestVal+10 && lc.HP < lc.goodHP)){
      ac.states.add(new State(State.Type.REST, getTime()));
  	  ac.path = null;
  	  /*
      State temp = ac.states.remove();
      if(temp != null){
        if(ac.states.peek() != null && ac.states.peek().type != State.Type.REST){
    	    ac.states.add(new State(State.Type.REST, getTime()));
    	    ac.path = null;
        }
        ac.states.add(temp);
      }
      else{
    	ac.states.add(new State(State.Type.REST, getTime()));
  	    ac.path = null;
      }     
      */ 
    }
    else if (ac.states.peek().type != State.Type.EAT
       && ((lc.hungerVal <= lc.eatHunger) ||
       		(lc.hungerVal <= lc.healHungerVal+10 && lc.HP < lc.goodHP))) {
    	ac.states.add(new State(State.Type.EAT, getTime()));
    	ac.path = null;
    }
    else if((ac.states.peek().type == State.Type.FIND_WATER && lc.hydration >= lc.maxHydration) 
    	  || (ac.states.peek().type == State.Type.REST && lc.restVal >= lc.maxRestVal)
    	  || (ac.states.peek().type == State.Type.EAT && lc.hungerVal >= lc.maxHungerVal)){
      ac.states.poll();
      ac.path = null;
    }
    else if((ac.states.peek().type == State.Type.WANDER)){
    	if(lc.hydration < lc.healHydration)
    		ac.states.add(new State(State.Type.FIND_WATER, getTime()));
    	else if(lc.restVal < lc.healRestVal)
    		ac.states.add(new State(State.Type.REST, getTime()));
    	ac.path = null;
    }
  }

  private void handleWanderState(){
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
  private void handleFindWaterState(){
    if(ac.path == null){
      Vec2f waterLoc = findClosest(Sprite.LAKE, pc.pos);
      if(waterLoc != null){
        ac.path = getPath(roundVector(pc.pos), roundVector(waterLoc));
      }else{
        ac.states.poll();
        ac.path = null;
      }
    }
  }
  private void handleRestState(){
    if(ac.path == null){
	  Vec2f houseLoc = findClosest(Sprite.SLEEPHOUSE, pc.pos);
	  if(houseLoc != null){
	    ac.path = getPath(roundVector(pc.pos), roundVector(houseLoc));
	  }else{
	    ac.states.poll();
	    ac.path = null;
	  }
	}
  }
  
  private void handleEatState() {
	  if(ac.path == null){
		  Vec2f shipLoc = findClosest(Sprite.SHIP, pc.pos);
		  if(shipLoc != null){
		    ac.path = getPath(roundVector(pc.pos), roundVector(shipLoc));
		  }else{
		    ac.states.poll();
		    ac.path = null;
		  }
		}
  }

  private void handleRelocateState(){
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      ac.states.poll();
      ac.path = null;
    }
  }

  private void handleFetchItemsState(){
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      ContainerComponent scc = (ContainerComponent)eManager.getComponent(
    		  Component.CONTAINER, eManager.getClosestMatching(Component.CONTAINER, Component.MOBILITY,
            		  command.location));
      ContainerComponent ecc = cc;
      ac.states.poll();
      ac.path = null;
      // scc.items -- ship inventory        | EnumMap
      // ecc.desiredItems -- desired items  | EnumMap
      boolean abortPrevCommand = false;
      for(Item item : command.desiredItems.keySet()){
        int oldVal = ecc.items.get(item) == null ? 0 : ecc.items.get(item);
        // TODO: right now crafting only works if the ship doesn't have any
        // items of that type.
        int shiphas = scc.items.get(item) == null ? 0 : scc.items.get(item);
        if(scc.items.get(item) == null ||
           shiphas < command.desiredItems.get(item)){
          ecc.items.put(item, shiphas + oldVal);
          scc.items.remove(item);

          /*
          if(item.isCraftable){
            PositionComponent spc =
              (PositionComponent)eManager.getComponent(Component.POSITION,
                  eManager.getFirstMatching(Component.SHIP));
            EnumMap<Item, Integer> itemsToCraft =
              new EnumMap<Item, Integer>(Item.class);
            itemsToCraft.put(item, command.desiredItems.get(item) - oldVal);
            ac.states.add(new Command(
                  State.Type.CRAFT_ITEMS, spc.pos, getTime(), itemsToCraft));
          }
          */

          abortPrevCommand = true;
        }else if(scc.items.get(item) > command.desiredItems.get(item)){
          ecc.items.put(item, command.desiredItems.get(item) + oldVal);
          scc.items.put(item, scc.items.get(item) -
                        command.desiredItems.get(item));
        }else{  // Ship has the exact amount of the item we need
          ecc.items.put(item, scc.items.remove(item) + oldVal);
        }
      }
      if(abortPrevCommand){
        ac.states.poll();
        ac.path = null;
      }
    }
  }

  private void handleDepositItemsState(){
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
    	ContainerComponent scc = (ContainerComponent)eManager.getComponent(
    	          Component.CONTAINER, eManager.getClosestMatching(Component.CONTAINER, Component.MOBILITY, 
    	        		  command.location));
      ContainerComponent ecc = cc;
      for(Item item : command.desiredItems.keySet()){
        int oldVal = scc.items.get(item) == null ? 0 : scc.items.get(item);
        scc.items.put(item, command.desiredItems.get(item) + oldVal);
        if(ecc.items.get(item) > command.desiredItems.get(item))
          ecc.items.put(item, ecc.items.get(item) - command.desiredItems.get(item));
        else
          ecc.items.remove(item);
      }
      ac.states.poll();
      ac.path = null;
    }
  }

  private void handleBuildState(State.Type type){
	    Command command = (Command)ac.states.peek();
	    float distance = (command.location.sub(pc.pos)).getMag();
	    if(distance > CLOSE_ENOUGH && ac.path == null){
	      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
	    }else if(distance <= CLOSE_ENOUGH){
	      if(type == State.Type.BUILD_SLEEPHOUSE)
	    	  EntityFactory.makeNewSleepHouse(command.location.x, command.location.y);
	      else if(type == State.Type.BUILD_REPRODUCTIONHOUSE)
	    	  EntityFactory.makeNewReproductionHouse(command.location.x, command.location.y);
	      else if(type == State.Type.BUILD_STORAGEUNIT)
	    	  EntityFactory.makeNewStorageUnit(command.location.x, command.location.y);
	      else if(type == State.Type.BUILD_SHIP)
	    	  EntityFactory.makeNewShip(command.location.x, command.location.y);
	      else{
	    	  java.lang.System.err.println("Error with handleBuildState. This should never run.");
	      }
	      for(Item item : command.reqItems.keySet()){
	        if(!item.isTool){
	          cc.items.put(item, cc.items.get(item) - command.reqItems.get(item));
	        }
	      }
	      ac.states.poll();
	      ac.path = null;
	    }
	  }
  
  private void handleBuildShipState(){
	    Command command = (Command)ac.states.peek();
	    float distance = (command.location.sub(pc.pos)).getMag();
	    if(distance > CLOSE_ENOUGH && ac.path == null){
	      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
	    }else if(distance <= CLOSE_ENOUGH){
	      EntityFactory.makeNewShip(command.location.x, command.location.y);
	      for(Item item : command.reqItems.keySet()){
	        if(!item.isTool){
	          // TODO: without these next two lines, the next line causes 
	          //       null pointer exceptions. magic?
	          command.reqItems.size();
	          cc.items.size();
	          // next line causes null pointer exceptions
	          cc.items.put(item, cc.items.get(item) - command.reqItems.get(item));
	        }
	      }
	      ac.states.poll();
	      ac.path = null;
	    }
	  }

  private void handleCraftItemsState(){
    /*
    Command command = (Command)ac.states.peek();
    float distance = (command.location.sub(pc.pos)).getMag();
    if(distance > CLOSE_ENOUGH && ac.path == null){
      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
    }else if(distance <= CLOSE_ENOUGH){
      ContainerComponent scc = (ContainerComponent)eManager.getComponent(
          Component.CONTAINER, eManager.getFirstMatching(Component.SHIP));
      ContainerComponent ecc = cc;
      boolean success = true;
      for(Item item : command.reqItems.keySet()){
        int have = ecc.items.get(item);
        int needed = command.reqItems.get(item) - have;
        int shipHas = scc.items.get(item) == null ? 0 : scc.items.get(item);
        if(shipHas < needed){
          ecc.items.put(item, shipHas + have);
          scc.items.remove(item);
          success = false;
          break;
        }else if(shipHas > needed){
          ecc.items.put(item, needed + have);
          scc.items.put(item, shipHas - needed);
        }else{  // Ship has the exact amount of the item we need
          ecc.items.put(item, scc.items.remove(item) + have);
        }
      }
      ac.states.poll();
      ac.path = null;
      if(success){
        for(Item item : command.desiredItems.keySet()){
          int have = ecc.items.get(item);
          ecc.items.put(item, have + command.desiredItems.get(item));
        }
        for(Item item : command.reqItems.keySet()){
          int have = ecc.items.get(item);
          ecc.items.put(item, have - command.reqItems.get(item));
          if(ecc.items.get(item) <= 0)
            ecc.items.remove(item);
        }
      }else{
      	java.lang.System.out.println("FAIL");
        ac.states.poll();
        ac.path = null;
        //ac.states.poll();
        //ac.path = null;
      }
    }
    */
  }
  
  private void handleGather(Item item, int count){
	    //final int NUM_LOGS_FROM_TREE = 1;
	    Command command = (Command)ac.states.peek();
	    World w = World.getWorld();
	    float distance = (command.location.sub(pc.pos)).getMag();
	    if(distance > CLOSE_ENOUGH && ac.path == null){
	      ac.path = getPath(roundVector(pc.pos), roundVector(command.location));
	    }else if(distance <= CLOSE_ENOUGH){
	      ContainerComponent ecc = cc;
	      w.setTile((int)command.location.y, (int)command.location.x,
	                new Dirt((int)command.location.y, (int)command.location.x));
	      int oldVal = ecc.items.get(item) == null ? 0 : ecc.items.get(item);
	      ecc.items.put(item, count + oldVal);

	      ac.states.poll();
	      ac.path = null;

	      if(ecc.items.get(item) >= ecc.maxCapacity){
	        EnumMap<Item, Integer> itemsToDeposit =
	          new EnumMap<Item, Integer>(Item.class);
	        itemsToDeposit.put(item, ecc.items.get(item));
	        PositionComponent spc = (PositionComponent)eManager.getComponent(
	            Component.POSITION, eManager.getFirstMatching(Component.SHIP));
	        ac.states.add(new Command(
	              State.Type.DEPOSIT_ITEMS, spc.pos, getTime(), itemsToDeposit));
	      }
	    }
	  }
  
  public void handleKill() {
	  final int NUM_MEAT_FROM_DEER = 1;
	    Command command = (Command)ac.states.peek();
	    PositionComponent targetPos = (PositionComponent)eMan.getComponent(Component.POSITION,command.target);
	    Vec2f pcPos = pc.pos;
	    if (pcPos == null || targetPos == null) {
	    	ac.states.poll();
	    	ac.path = null;
	    	return;
	    }
	    float distance = targetPos.getPos().sub(pcPos).getMag();
	    if(distance > CLOSE_ENOUGH && ac.path == null){
	      ac.path = getPath(roundVector(pcPos), roundVector(((PositionComponent)eMan.getComponent(Component.POSITION,command.target)).getPos()));
	    }else if(distance <= CLOSE_ENOUGH){
	      AnimalComponent anCom = ((AnimalComponent)eMan.getComponent(Component.ANIMAL,command.target));
	      if (anCom == null) return;
	      anCom.toggleHit();
	      ac.path = null;
	      
	      
	      if(anCom.HP <= 0.0f) {
	    	  anCom.dead = true;
		      ContainerComponent ecc = cc;
		      int oldVal = ecc.items.get(Item.MEAT) == null ? 0 : ecc.items.get(Item.MEAT);
		      ecc.items.put(Item.MEAT, NUM_MEAT_FROM_DEER + oldVal);
	
		      ac.states.poll();
		      ac.path = null;
	
		      if(ecc.items.get(Item.MEAT) >= ecc.maxCapacity){
		        EnumMap<Item, Integer> itemsToDeposit =
		          new EnumMap<Item, Integer>(Item.class);
		        itemsToDeposit.put(Item.MEAT, ecc.items.get(Item.MEAT));
		        PositionComponent spc = (PositionComponent)eManager.getComponent(
		            Component.POSITION, eManager.getFirstMatching(Component.SHIP));
		        ac.states.add(new Command(
		        State.Type.DEPOSIT_ITEMS, spc.pos, getTime(), itemsToDeposit));
	      }
	      }
	    }
  }

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
  public Vec2f findClosest(Sprite s, Vec2f pos){
	Vector<Entity> allEntities = eManager.getMatchingEntities(Component.RENDER | Component.POSITION);
	Vec2f returnVector = null;
	for (Entity e: allEntities){
		RenderComponent rc = (RenderComponent)eManager.getComponent(Component.RENDER, e);
		if(rc.s == s){
			PositionComponent pc = (PositionComponent)eManager.getComponent(Component.POSITION, e);
			if(returnVector == null)
				returnVector = pc.pos;
			else{
				if(Math.abs(pos.sub(pc.pos).getMag()) < Math.abs(pos.sub(returnVector).getMag()))
					returnVector = pc.pos;
			}
		}
	}
	if(returnVector != null)
		return returnVector;
	  
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

  private long getTime(){
    return java.lang.System.currentTimeMillis();
  }

  private PositionComponent pc;
  private AIComponent ac;
  private MobilityComponent mc;
  private LivingComponent lc;
  private ContainerComponent cc;
  private MessageComponent msc;
  private AnimalComponent anc;
  private EntityManager eMan;
  
  private static Random r = new Random();
  private static final float CLOSE_ENOUGH = 0.2f;
}
