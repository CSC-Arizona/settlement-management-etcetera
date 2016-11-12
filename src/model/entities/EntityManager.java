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

  private EntityManager(){
    used = 0;
    recycleBin = new Vector<Integer>();
    entityBitSets = new Vector<Long>(MAX_ENTITIES);
    for(int i = 0; i < MAX_ENTITIES; ++i){
      entityBitSets.add(new Long(0));
    }
    entityBitSets.setSize(MAX_ENTITIES);
    compVecs = new Vector<Vector<Component>>();
    for(int i = 0; i < Component.TOTAL_COMPS; ++i){
      compVecs.add(new Vector<Component>());
      compVecs.get(i).setSize(MAX_ENTITIES);
    }
  }

  /*
   * This is how we get a brand new entity.
   */
  public Entity genNewEntity(){
    return genNewEntity(0);
  }

  /*
   * Same as above, but adds requested components with their default data values
   */
  public Entity genNewEntity(long componentBitSet){
    // Check the bit set to make sure it's valid
    if(componentBitSet >= (1 << Component.TOTAL_COMPS))
      return null;

    int id;
    if(recycleBin.size() != 0){
      id = recycleBin.get(recycleBin.size() - 1);
      recycleBin.remove(recycleBin.size() - 1);
    }else{
      id = used++;
    }
    
    for(int i = 0; (1 << i) < (1 << Component.TOTAL_COMPS); ++i){
      long curComp = componentBitSet & (1 << i);
      if(curComp == 1)
        compVecs.get(i).setElementAt(Component.getComponentFromCID(curComp), id);
    }

    entityBitSets.setElementAt(componentBitSet, id);
    return new Entity(id);
  }

  /*
   * Removes the entity from the system.
   */
  public void rmEntity(Entity e){
    if(e.getID() == used - 1)
      --used;
    else
      recycleBin.add(e.getID());
    entityBitSets.setElementAt(null, e.getID());
    for(Vector<Component> cv : compVecs)
      cv.setElementAt(null, e.getID());
  }

  /*
   * Returns the desired component vector.
   */
  public Vector<Component> getCompVec(long componentID){
    for(int i = 0; (1 << i) < (1 << Component.TOTAL_COMPS); ++i){
      if((componentID & (1 << i)) == 1)
        return compVecs.get(i);
    }
    return null;
  }

  /*
   * Adds a component to an entity.
   */
  public void addComponent(Component comp, Entity e){
    int compVecPtr = getCompVecIndex(comp.ID);
    compVecs.get(compVecPtr).setElementAt(comp, e.getID());
    entityBitSets.setElementAt(
        entityBitSets.get(e.getID()) | comp.ID, e.getID());
  }

  /*
   * Gets the requested component associated with the given entity.
   */
  public Component getComponent(long componentID, Entity e){
    return compVecs.get(getCompVecIndex(componentID)).get(e.getID());
  }

  /*
   * Returns all of the entities that have desired components.
   */
  public Vector<Entity> getMatchingEntities(long componentBitSet){
    Vector<Entity> ret = new Vector<Entity>();
    for(int id = 0; id < entityBitSets.size(); ++id){
      if(hasComponents(componentBitSet, new Entity(id)))
        ret.add(new Entity(id));
    }
    return ret;
  }

  /*
   * Tells us whether or not an entity has specified components.
   */
  public boolean hasComponents(long componentBitSet, Entity e){
    return (entityBitSets.get(e.getID()) & componentBitSet) == componentBitSet;
  }

  private int getCompVecIndex(long componentID){
    int  ret = 0;
    for(long i = componentID; i != 1; i >>>= 1, ++ret);
    return ret;
  }

  // Holds freed up ID's
  private Vector<Integer> recycleBin;
  private Vector<Vector<Component>> compVecs;
  private Vector<Long> entityBitSets;
  private int used;

  private static final int MAX_ENTITIES = 2048;
}

