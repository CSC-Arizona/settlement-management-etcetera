package run;

import javax.swing.*;

import utility.Sprite;
import world.World;

import java.awt.*;
import java.awt.image.*;
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
  
  final BufferedImage image = (new BufferedImage(totalWidthPixels, totalHeightPixels, BufferedImage.TYPE_INT_ARGB));

  final JFrame frame = new JFrame();

  final JLabel label = new JLabel(new ImageIcon(image));
  final JScrollPane scrollPane = new JScrollPane(label);

  RunMe() {
	frame.setLayout(new BorderLayout());
	frame.setPreferredSize(new Dimension(viewWidthPixels + 10, viewHeightPixels));
    label.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    scrollPane.setPreferredSize(new Dimension(viewWidthPixels, viewHeightPixels));

    frame.add(scrollPane, BorderLayout.WEST);

    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    Game g = new Game(image, label);
    g.start();
  }
}
