package run;

import javax.swing.*;

import utility.Sprite;
import entities.Component;
import entities.EntityManager;
import world.World;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.awt.event.*;

class RunMe {
  public static void main(String[] args) {
    new RunMe();
  }
  
  final int viewWidth = 30;
  final int viewHeight = 25;
  
  final int viewWidthPixels = viewWidth * Sprite.WIDTH;
  final int viewHeightPixels = viewHeight * Sprite.HEIGHT;
  
  final int totalWidthPixels = World.WORLD_SIZE * Sprite.WIDTH;
  final int totalHeightPixels = World.WORLD_SIZE * Sprite.HEIGHT;
  
  final int infoPanelWidth = 300;
  
  final transient BufferedImage image = (new BufferedImage(totalWidthPixels, totalHeightPixels, BufferedImage.TYPE_INT_ARGB));

  final JFrame frame = new JFrame();

  final JLabel label = new JLabel(new ImageIcon(image));
  final JScrollPane scrollPane = new JScrollPane(label);
  
  final InfoPanel infoPanel = InfoPanel.getInstance();
  final JScrollPane infoPane = new JScrollPane(infoPanel);

  Game g;

  RunMe() {
	frame.setLayout(new BorderLayout());
	frame.setPreferredSize(new Dimension(viewWidthPixels + infoPanelWidth, viewHeightPixels));
    label.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    scrollPane.setPreferredSize(new Dimension(viewWidthPixels, viewHeightPixels));
    infoPanel.setPreferredSize(new Dimension(infoPanelWidth, viewHeightPixels*2));
    infoPane.setPreferredSize(new Dimension(infoPanelWidth, viewHeightPixels));
    
    frame.setLayout(new BorderLayout());
    frame.add(scrollPane, BorderLayout.WEST);
    frame.add(infoPane, BorderLayout.EAST);

    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.addWindowListener(new MyWindowListener());
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setVisible(true);
    
    int result = JOptionPane.showConfirmDialog(null, "Resume from previous session?","alert", JOptionPane.YES_NO_OPTION);
	  if (result != 1) {
	    readData();
<<<<<<< HEAD
	    g.setBackground(image, frame, scrollPane);
	  }
    if (g == null) {
      g = new Game(image, frame, scrollPane);
=======
	    g.setBackground(image, frame);
	  }
    if (g == null) {
      g = new Game(image, frame);
>>>>>>> 287dd10fe5d4185c801e47737cdc10a6f4945264
      g.spawnAliens(2);
    }
    g.start();
  }
  
  private void readData() {
    try {
      if (new File("gamedata").exists()) { 
        FileInputStream rawBytes = new FileInputStream("gamedata"); 
        ObjectInputStream inFile = new ObjectInputStream(rawBytes);
        g = (Game)inFile.readObject();
        World.setWorld((World)inFile.readObject());
        Vector<Integer> recycleBin = (Vector<Integer>)inFile.readObject();
        Vector<Vector<Component>> compVecs = (Vector<Vector<Component>>)inFile.readObject();
        Vector<Long> entityBitSets = (Vector<Long>)inFile.readObject();
        int used = (Integer)inFile.readObject();
        EntityManager eMan = EntityManager.INSTANCE;
        eMan.loadInstance(recycleBin, compVecs, entityBitSets, used);
        
        inFile.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private class MyWindowListener extends WindowAdapter implements WindowListener {
    @Override
    public void windowClosing(WindowEvent we) {
      int result = JOptionPane.showConfirmDialog(null, "Save Data?","alert", JOptionPane.YES_NO_CANCEL_OPTION);
      if (result == 1) {
        //no
        System.exit(0);
      } else if (result != 2) {
        //yes
        try {
          FileOutputStream bytesToDisk = new FileOutputStream("gamedata"); 
          ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
          outFile.writeObject(g);
          outFile.writeObject(World.getWorld());
          EntityManager eMan = EntityManager.INSTANCE;
          eMan.saveInstance(outFile);
          
          outFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        System.exit(0);
      }
    }
  }
}
