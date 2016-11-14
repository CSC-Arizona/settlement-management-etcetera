package run;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

class RunMe {
  public static void main(String[] args) {
    new RunMe();
  }

  final BufferedImage image = (new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB));

  final JFrame frame = new JFrame();

  final JLabel label = new JLabel(new ImageIcon(image));

  RunMe() {
    label.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

    frame.add(label);

    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    Game g = new Game(image, label);
    g.start();
  }
}
