package org.view;

import javax.swing.SwingUtilities;
import org.view.swing.SwingView;

public class Main {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(SwingView::new);
  }
}