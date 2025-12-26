package com.scd.codeSmellDetector.controller;

import com.scd.codeSmellDetector.view.MainView;

public class MainViewController {
  MainView mainView;

  public void displayMainView() {
    mainView = new MainView(this, 400, 400);
    mainView.setVisible(true);
  }
}
