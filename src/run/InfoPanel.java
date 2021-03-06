/*
 * Class InfoPanel.java
 * Author: Caleb Short
 * The purpose of this class is to represent the information of a unit clicked on by the user. The class implements the Singleton Design,
 * 	 only keeping one copy of itself. Other parts of the code may change the entity/sprite that models the InfoClass.
 */
package run;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import entities.AIComponent;
import entities.AnimalComponent;
import entities.Command;
import entities.CommandSystem;
import entities.CommandableComponent;
import entities.Component;
import entities.ContainerComponent;
import entities.Entity;
import entities.EntityManager;
import entities.Item;
import entities.LivingComponent;
import entities.NameComponent;
import entities.RenderComponent;
import entities.State;
import utility.Sprite;
import utility.Tileset;

public class InfoPanel extends JPanel implements Serializable{
  private Entity e;
  private Sprite s;
  EntityManager eManager;
  private static InfoPanel uniqueInstance;
  float alignment;
  Dimension size;
  Dimension buffer;
  JLabel sleepLabel, reproduceLabel, storeLabel, shipLabel, nothing;
  private JLabel healthLabel;
  private JList<String> inventoryList;
  private JList<String> commandList;
  
  // Allows another class to get a copy of the single instance of this class
  public static InfoPanel getInstance(){
    if (uniqueInstance == null) {
	  uniqueInstance = new InfoPanel();
	}
	return uniqueInstance;
  }
  
  // Initializes this class with it's representative entity as null
  private InfoPanel(){
	e = (Entity) null;
	s = (Sprite) null;
	eManager = EntityManager.INSTANCE;
	//this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	alignment = java.awt.Component.LEFT_ALIGNMENT;
	size = this.getPreferredSize();
	buffer = new Dimension(0,5);
	//setAlignmentX(LEFT_ALIGNMENT);
	BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(layout);
	createPanel();
  }
	
  // Allows another class to change the entity that models the InfoPanel
  public void setModelEntitySprite(Entity e, Sprite s){
	this.e = e;
	this.s = s;
	createPanel();
  }
  
  // Draws the panel based on the current entity
  private void createPanel(){
	removeAll();
	createBuildingsInstructions();
	createName();
	createImage();
	createHealth();
	createAnimalHealth();
	createInventory();
	createCommandQueue();
	revalidate();
	repaint();
  }
  
//Adds JLabels that give instructions for creating buildings
 private void createBuildingsInstructions(){
	JLabel helpLabel = new JLabel("Press H for help");
	helpLabel.setForeground(Color.BLUE);
	add(helpLabel);
	add(Box.createRigidArea(buffer));
	
	String instructions = "<html>Press a key below to choose a left-click setting<br>Then click "
			+ "somewhere to build/gather<html>";
	JLabel buildInstructions = new JLabel(instructions);
	add(buildInstructions);
	  
	sleepLabel = new JLabel("Q: Construct a sleeping house");
	add(sleepLabel);
	  
	reproduceLabel = new JLabel("W: Construct a reproduction house");
	add(reproduceLabel);
	  
	storeLabel = new JLabel("E: Construct a storage unit");
	add(storeLabel);
	
	shipLabel = new JLabel("R: Construct the ship to escape earth!");
	add(shipLabel);
	
	nothing = new JLabel("A: Collect resources.");
	add(nothing);
	
	sleepLabel.setForeground(Color.BLACK);
	reproduceLabel.setForeground(Color.BLACK);
	storeLabel.setForeground(Color.BLACK);
	storeLabel.setForeground(Color.BLACK);
	nothing.setForeground(Color.RED);
	  
	add(Box.createRigidArea(buffer));
 }
  
  // Sets up a title feature and adds it to the InfoPanel
  private void createName(){
    Border nameBorder = BorderFactory.createTitledBorder("Name");
	JLabel name = new JLabel();
	//name.setSize(this.getSize().width, 40);
	name.setAlignmentX(alignment);
	if(e != null){
	  NameComponent nc = (NameComponent) eManager.getComponent(Component.NAME, e);
	  name.setBorder(nameBorder);
	  name.setText(nc.name+"        ");
	}
	else if(s != null){
	  name.setBorder(nameBorder);
	  name.setText(s.toString() + "        ");
	}
	else{
	  name.setText("Right-click on an item for information");
	}
	add(name);
	add(Box.createRigidArea(buffer));
  }
  
  // Sets up an image feature and adds it to the InfoPanel
  private void createImage(){
	if(e == null && s == null)
		return;
	ImageIcon icon;
	if(e != null){
	  RenderComponent rc = (RenderComponent) eManager.getComponent(Component.RENDER, e);
	  Image image = rc.texture;
	  icon = new ImageIcon(image);
	}
	else{
	  Image image = Tileset.instance().getSprite(s);
	  icon = new ImageIcon(image);
	}
	JLabel imageLabel = new JLabel("    ", icon, 0);
	imageLabel.setAlignmentX(alignment);
	Border imageBorder = BorderFactory.createTitledBorder("Image");
	imageLabel.setBorder(imageBorder);
	add(imageLabel);
	add(Box.createRigidArea(buffer));
 }
  
  // Sets up the health feature for the entity if it has a LivingComponent
  private void createHealth(){
    if(e == null || !eManager.hasComponents(Component.LIVING, e))
	  return;
	healthLabel = new JLabel();
	healthLabel.setSize(this.getSize().width, 80);
	healthLabel.setAlignmentX(alignment);
	Border healthBorder = BorderFactory.createTitledBorder("Welfare");
	healthLabel.setBorder(healthBorder);
	LivingComponent lc = (LivingComponent) eManager.getComponent(Component.LIVING, e);
	String text1 = "Health: " + (int) lc.HP + "/" + (int) lc.maxHP + "\n"; 
	String text2 = "Hydration: " + (int) lc.hydration + "/" + (int) lc.maxHydration; 
	String text3 = "Rest Value: " + (int) lc.restVal + "/" + (int) lc.maxRestVal;
	String text4 = "Hunger: " + (int) lc.hungerVal + "/" + (int) lc.maxHungerVal;
	String text = "<html>" + text1 + "<br>" + text2 + "<br>" + text3 + "<br>" + text4;
	healthLabel.setText(text);
	add(healthLabel);
	add(Box.createRigidArea(buffer));
  }
  
//Sets up the health feature for the entity if it has a LivingComponent
 private void createAnimalHealth(){
   if(e == null || !eManager.hasComponents(Component.ANIMAL, e))
	  return;
	healthLabel = new JLabel();
	healthLabel.setSize(this.getSize().width, 80);
	healthLabel.setAlignmentX(alignment);
	Border healthBorder = BorderFactory.createTitledBorder("Health");
	healthLabel.setBorder(healthBorder);
	AnimalComponent ac = (AnimalComponent) eManager.getComponent(Component.ANIMAL, e);
	String text = "Health: " + (int) ac.HP + "/" + (int) ac.maxHP + "\n"; 
	healthLabel.setText(text);
	add(healthLabel);
	add(Box.createRigidArea(buffer));
 }
  
  // Sets up the inventory feature for the entity if it has a ContainerComponent
  private void createInventory(){
	if(e == null || !eManager.hasComponents(Component.CONTAINER, e))
	  return;
	ContainerComponent cc = (ContainerComponent) eManager.getComponent(Component.CONTAINER, e);
	EnumMap<Item, Integer> items = cc.getItems();
	DefaultListModel<String> model = new DefaultListModel<String>();
	for(Item i : items.keySet()){
	  model.addElement(items.get(i) + " " +i.getName());
	}
	inventoryList = new JList<String>(model);
	JScrollPane inventoryScroller = new JScrollPane(inventoryList);
	inventoryScroller.setPreferredSize(new Dimension(this.getSize().width, 200));
	inventoryScroller.setMaximumSize(new Dimension(this.getSize().width, 200));	  
	Border inventoryBorder = BorderFactory.createTitledBorder("Inventory (" + cc.getMax()+")");
	inventoryScroller.setBorder(inventoryBorder);
	inventoryScroller.setAlignmentX(alignment);
	add(inventoryScroller);
	add(Box.createRigidArea(buffer));
  }
  
  // Sets up the task queue for the entity if it has a CommandableComponent
  private void createCommandQueue(){
	if(e == null || !eManager.hasComponents(Component.COMMANDABLE, e))
	  return;
	AIComponent ac = (AIComponent) eManager.getComponent(Component.AI, e);
	PriorityQueue<State> states = ac.getStates();
	
	//Command command= (Command)states.peek();
	
	DefaultListModel<String> model = new DefaultListModel<String>();
	for(State s : states){
	  //model.addElement(s.getType().name().equals("KILL") ? "KILL " + ((NameComponent)eManager.getComponent(Component.NAME,command.target)).name : s.getType().name());
	  model.addElement(s.getType().name());
	}
	commandList = new JList<String>(model);
	JScrollPane commandScroller = new JScrollPane(commandList);
	commandScroller.setPreferredSize(new Dimension(this.getSize().width, 200));  
	commandScroller.setMaximumSize(new Dimension(this.getSize().width, 200));
	Border commandBorder = BorderFactory.createTitledBorder("Command Queue");
	commandScroller.setBorder(commandBorder);
	commandScroller.setAlignmentX(alignment);
	add(commandScroller);
	add(Box.createRigidArea(buffer));
  }
  
  // Updates the panel assuming that the panel has the same entity as before
  public void updatePanel(char buildChoice){
	  updateBuildChoice(buildChoice);
	  if(e == null)
	  return;
	updateBuildChoice(buildChoice);
	updateHealth();
	updateAnimalHealth();
	updateInventory();
	updateCommandQueue();
	repaint();
  }
  
//Displays as red the build option that is currently selected
 private void updateBuildChoice(char buildChoice){
	  sleepLabel.setForeground(Color.BLACK);
	  reproduceLabel.setForeground(Color.BLACK);
	  storeLabel.setForeground(Color.BLACK);
	  shipLabel.setForeground(Color.BLACK);
	  nothing.setForeground(Color.BLACK);
	  switch(buildChoice){
	  	case 'A':
		  nothing.setForeground(Color.RED);
	  	  break;
	  	case 'Q':
	  	  sleepLabel.setForeground(Color.RED);
		  break;
	  	case 'W':
		  reproduceLabel.setForeground(Color.RED);
	      break;
	  	case 'E':
		  storeLabel.setForeground(Color.RED);
		  break;
	  	case 'R':
	  	  shipLabel.setForeground(Color.RED);
	  	  break;
		default:
		  System.err.println("This should never happen.");
		  System.err.println(buildChoice);
		  break;
	  }
 }
  
  // Updates the name feature of the InfoPanel
  public void updateHealth(){
	if(!eManager.hasComponents(Component.LIVING, e))
	  return;
	LivingComponent lc = (LivingComponent) eManager.getComponent(Component.LIVING, e);
	String text1 = "Health: " + (int) lc.HP + "/" + (int) lc.maxHP + "\n"; 
	String text2 = "Hydration: " + (int) lc.hydration + "/" + (int) lc.maxHydration; 
	String text3 = "Rest Value: " + (int) lc.restVal + "/" + (int) lc.maxRestVal;
	String text4 = "Hunger: " + (int) lc.hungerVal + "/" + (int) lc.maxHungerVal;
	String text = "<html>" + text1 + "<br>" + text2 + "<br>" + text3 + "<br>" + text4;
	healthLabel.setText(text);
  }
  
  public void updateAnimalHealth(){
		if(!eManager.hasComponents(Component.ANIMAL, e))
		  return;
		AnimalComponent ac = (AnimalComponent) eManager.getComponent(Component.ANIMAL, e);
		String text = "Health: " + (int) ac.HP + "/" + (int) ac.maxHP + "\n"; 
		healthLabel.setText(text);
	  }
  
  // Updates the inventory feature of the InfoPanel
  public void updateInventory(){
	if(!eManager.hasComponents(Component.CONTAINER, e))
	  return;
    ContainerComponent cc = (ContainerComponent) eManager.getComponent(Component.CONTAINER, e);
    EnumMap<Item, Integer> items = cc.getItems();
	DefaultListModel<String> model = new DefaultListModel<String>();
	for(Item i : items.keySet()){
	  model.addElement(items.get(i) + " " + i.getName());
	}
	inventoryList.setModel(model);
  }
  
  // Updates the command feature of the InfoPanel
  public void updateCommandQueue(){
	if(!eManager.hasComponents(Component.COMMANDABLE, e))
	  return;
	AIComponent ac = (AIComponent) eManager.getComponent(Component.AI, e);
	PriorityQueue<State> states = ac.getStates();
	//Command command= (Command)states.peek();
	DefaultListModel<String> model = new DefaultListModel<String>();
	for(State s : states){
	  //model.addElement(s.getType().name().equals("KILL") ? "KILL " + ((NameComponent)eManager.getComponent(Component.NAME,command.target)).name : s.getType().name());
		model.addElement(s.getType().name());
	}
	commandList.setModel(model);
  }
  
  public static void load(InfoPanel ip) {
	  uniqueInstance = ip;
  }
  
}