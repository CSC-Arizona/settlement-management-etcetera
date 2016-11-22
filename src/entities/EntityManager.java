/**
 * A singleton class that handles creation/configuration/management of entities.
 * @author Artyom Perov
 *
 * Internally entities are represented by a single integer -- their ID. This ID
 * defines the index in various component vectors, as well as the entityBitSets
 * vector.
 * 
 * This class provides various functionality to aid systems in processing
 * appropriate entities, as well as entity creation.
 *
 */

package entities;

import java.util.Vector;

public enum EntityManager {
  INSTANCE;

  private EntityManager() {
    used = 0;
    recycleBin = new Vector<Integer>();
    entityBitSets = new Vector<Long>(MAX_ENTITIES);
    for (int i = 0; i < MAX_ENTITIES; ++i) {
      entityBitSets.add(new Long(0));
    }
    entityBitSets.setSize(MAX_ENTITIES);
    compVecs = new Vector<Vector<Component>>();
    for (int i = 0; i < Component.TOTAL_COMPS; ++i) {
      compVecs.add(new Vector<Component>());
      compVecs.get(i).setSize(MAX_ENTITIES);
    }
  }

  /*
   * This is how we get a brand new entity.
   */
  public Entity genNewEntity() {
    int id;
    if (recycleBin.size() != 0) {
      id = recycleBin.get(recycleBin.size() - 1);
      recycleBin.remove(recycleBin.size() - 1);
    } else {
      id = used++;
    }
    return new Entity(id);
  }

  /*
   * Removes the entity from the system.
   */
  public void rmEntity(Entity e) {
    if(e != null){
      if (e.getID() == used - 1)
        --used;
      else
        recycleBin.add(e.getID());
      entityBitSets.setElementAt(null, e.getID());
      for (Vector<Component> cv : compVecs)
        cv.setElementAt(null, e.getID());
    }
  }

  /*
   * Returns the desired component vector.
   */
  public Vector<Component> getCompVec(long componentID) {
    for (int i = 0; (1 << i) < (1 << Component.TOTAL_COMPS); ++i) {
      if ((componentID & (1 << i)) == componentID)
        return compVecs.get(i);
    }
    return null;
  }

  /*
   * Adds a component to an entity.
   */
  public void addComponent(Component comp, Entity e) {
    if(e != null){
      int compVecPtr = getCompVecIndex(comp.ID);
      compVecs.get(compVecPtr).setElementAt(comp, e.getID());
      long oldBS = entityBitSets.get(e.getID()) == null ? 0 : entityBitSets.get(e.getID());
      entityBitSets.setElementAt(oldBS | comp.ID, e.getID());
    }
  }

  /*
   * Gets the requested component associated with the given entity.
   */
  public Component getComponent(long componentID, Entity e) {
    return compVecs.get(getCompVecIndex(componentID)).get(e.getID());
  }

  /*
   * Returns all of the entities that have desired components.
   */
  public Vector<Entity> getMatchingEntities(long componentBitSet) {
    Vector<Entity> ret = new Vector<Entity>();
    for (int id = 0; id < used; ++id) {
      if (hasComponents(componentBitSet, new Entity(id)))
        ret.add(new Entity(id));
    }
    return ret;
  }

  /*
   * Returns a vector of entities that have CBS components AND don't have
   * any of the blacklisted components.
   */
  public Vector<Entity> getMatchingEntities(long CBS, long blacklist){
    Vector<Entity> ret = new Vector<Entity>();
    int id;
    for (id = 0; id < used; ++id) {
      Entity e = new Entity(id);
      try{
      long thisCBS = entityBitSets.get(id);
      if((thisCBS & CBS) == CBS && (thisCBS & blacklist) == 0)
          ret.add(e);
      }catch(NullPointerException e1){
    	  System.out.println(entityBitSets);
    	  System.out.println(id);
    	  e1.printStackTrace();
    	  System.exit(1);
      }
    }
    return ret;
  }

  /*
   * Tells us whether or not an entity has specified components.
   */
  public boolean hasComponents(long componentBitSet, Entity e) {
    if(e == null || entityBitSets.get(e.getID()) == null)
      return false;
    else
      return (entityBitSets.get(e.getID()) & componentBitSet) == componentBitSet;
  }
  
  public int getUsed(){
    return used;
  }

  private int getCompVecIndex(long componentID) {
    int ret = 0;
    for (long i = componentID; i != 1; i >>>= 1, ++ret);
    return ret;
  }

  // Holds freed up ID's
  private Vector<Integer> recycleBin;
  private Vector<Vector<Component>> compVecs;
  private Vector<Long> entityBitSets;
  private int used;

  private static final int MAX_ENTITIES = 2048;
}
