/**
 * Abstract class for components.
 * @author Artyom Perov.
 *
 * This class holds component ID's, and ensures new components are aware of
 * their ID.
 *
 * To add a component:
 *     1) Extend your component named NameComponent from Component
 *     2) Add a constant representing it to list below (they should be in order)
 *     3) Increment the TOTAL_COMPS long
 *     Optional: create a system using it.
 */

package entities;

import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public abstract class Component implements Serializable {
  /*
   * Default access modifier: seen only within the entities package.
   *
   * Might change this to an enum later (probably not though, since that would
   * mean "genNewEntity(Component.POSITION | Component.VELOCITY | Component.AI)
   * would have to be changed to:
   * "genNewEntity(ComponentType.POSITION.getValue() |
   * ComponentType.VELOCITY.getValue() | ComponentType.AI.getValue())", and it
   * is already way too verbose for my taste.
   */
  public static final long POSITION      = 1 << 0;
  public static final long MOBILITY      = 1 << 1;
  public static final long COLLISION     = 1 << 2;
  public static final long RENDER        = 1 << 3;
  public static final long AI            = 1 << 4;
  public static final long LIVING        = 1 << 5;
  public static final long COMMANDABLE   = 1 << 6;
  public static final long CONTAINER     = 1 << 7;
  public static final long SHIP          = 1 << 8;
  public static final long NAME		  = 1 << 9;
  public static final long TOTAL_COMPS   =      10;

  /*
   * Must be called with an appropriate ComponentID taken from above.
   */
  public Component(long ComponentID) {
    ID = ComponentID;
  }

  final long ID;
}
