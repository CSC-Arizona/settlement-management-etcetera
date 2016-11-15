/*
 * Keeps track if a player unit is hungry and has arrows.
 * Author: Caleb Short
 */
package entities;

public class PlayerAIComponent extends AIComponent{
	
  public PlayerAIComponent() {
    super();
	  timeHungry = 0.0f;
	  numArrows = 0;
	  AIType = "player";
	}
	
  float timeHungry;
  int numArrows;
	
}
