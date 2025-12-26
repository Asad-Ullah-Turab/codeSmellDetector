package com.scd.codeSmellDetector.controller;

import com.scd.codeSmellDetector.view.MainView;
import com.scd.codeSmellDetector.model.CodeSmellAnalyzer;
import com.scd.codeSmellDetector.model.CodeSmell;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MainViewController {
  private MainView mainView;
  private CodeSmellAnalyzer analyzer;
  private File currentProject;
  private File currentSelectedFile;

  public MainViewController() {
    this.analyzer = new CodeSmellAnalyzer();
  }

  public void displayMainView() {
    mainView = new MainView(1400, 900);
    mainView.setVisible(true);
    attachEventListeners();
  }

  private void attachEventListeners() {
    // Select project button
    mainView.getSelectProjectButton().addActionListener(e -> selectProjectDirectory());

    // Detect button
    mainView.getDetectButton().addActionListener(e -> analyzeCurrentFile());

    // File tree selection
    mainView.getProjectTreePanel().getFileTree().addTreeSelectionListener(e -> {
      File selectedFile = mainView.getProjectTreePanel().getSelectedFile();
      if (selectedFile != null && selectedFile.isFile()) {
        displayFileContent(selectedFile);
      }
    });
  }

  private void selectProjectDirectory() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    int result = fileChooser.showOpenDialog(mainView);
    if (result == JFileChooser.APPROVE_OPTION) {
      currentProject = fileChooser.getSelectedFile();
      mainView.getProjectTreePanel().loadProjectTree(currentProject);
      mainView.getCodeEditorPanel().clearCode();
      mainView.getCodeSmellResultsPanel().clearResults();
      mainView.getDetectButton().setEnabled(true);
    }
  }

  private void displayFileContent(File file) {
    currentSelectedFile = file;
    try {
      String content = new String(Files.readAllBytes(file.toPath()));
      mainView.getCodeEditorPanel().displayCode(file.getName(), content);
      mainView.getCodeSmellResultsPanel().clearResults();
    } catch (IOException e) {
      mainView.getCodeEditorPanel().displayCode(file.getName(), "Error reading file: " + e.getMessage());
    }
  }

  private void analyzeCurrentFile() {
    if (currentSelectedFile == null) {
      JOptionPane.showMessageDialog(mainView, "Please select a Java file first", "No File Selected",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    List<CodeSmell> codeSmells = analyzer.analyzeFile(currentSelectedFile);
    mainView.getCodeSmellResultsPanel().displayResults(codeSmells);
  }
}
