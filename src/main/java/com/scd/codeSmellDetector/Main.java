package com.scd.codeSmellDetector;

import com.scd.codeSmellDetector.controller.MainViewController;
import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainViewController mainViewController = new MainViewController();
      mainViewController.displayMainView();
    });
  }
}
