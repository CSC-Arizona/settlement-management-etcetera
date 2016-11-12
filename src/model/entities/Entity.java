/**
 * Serves as a wrapper for int, since java does not provide any typedef-like
 * functionality.
 * @author Artyom Perov.
 *
 * This class exists for the sole purpose of (hopefully) making the entity
 * system less confusing. Instead of having to write "int Dog" we can write
 * "Entity Dog".
 */

package entities;

public class Entity {
  /*
   * Default access modifier, can only be constructed by the classes in entities
   * package.
   */
  Entity(int ID){
    this.ID = ID;
  }

  public int getID(){
    return ID;
  }

  private final int ID;
}

