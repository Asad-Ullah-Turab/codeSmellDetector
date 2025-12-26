package com.scd.codeSmellDetector.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JFrame {
  private ProjectTreePanel projectTreePanel;
  private CodeEditorPanel codeEditorPanel;
  private CodeSmellResultsPanel codeSmellResultsPanel;
  private JButton detectButton;
  private JButton selectProjectButton;

  public MainView(int width, int height) {
    initializeUI(width, height);
  }

  private void initializeUI(int width, int height) {
    setTitle("Code Smell Detector");
    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    // Main container
    JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Top toolbar
    JPanel toolbarPanel = createToolbar();
    mainPanel.add(toolbarPanel, BorderLayout.NORTH);

    // Content panel with three sections
    JSplitPane contentSplit = createContentPanel();
    mainPanel.add(contentSplit, BorderLayout.CENTER);

    add(mainPanel);
    setLocationRelativeTo(null);
  }

  private JPanel createToolbar() {
    JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    toolbarPanel.setBackground(new Color(240, 240, 240));
    toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

    selectProjectButton = new JButton("📁 Open Project");
    selectProjectButton.setFont(new Font("Arial", Font.PLAIN, 12));
    selectProjectButton.setFocusPainted(false);
    selectProjectButton.setBackground(new Color(63, 81, 181));
    selectProjectButton.setForeground(Color.WHITE);
    selectProjectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    detectButton = new JButton("🔍 Detect Code Smells");
    detectButton.setFont(new Font("Arial", Font.PLAIN, 12));
    detectButton.setFocusPainted(false);
    detectButton.setBackground(new Color(76, 175, 80));
    detectButton.setForeground(Color.WHITE);
    detectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    detectButton.setEnabled(false);

    toolbarPanel.add(selectProjectButton);
    toolbarPanel.add(detectButton);

    return toolbarPanel;
  }

  private JSplitPane createContentPanel() {
    // Left panel - Project Tree
    projectTreePanel = new ProjectTreePanel();

    // Center panel - Code Editor and Results
    JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    rightSplit.setResizeWeight(0.6);
    rightSplit.setDividerLocation(0.6);

    codeEditorPanel = new CodeEditorPanel();
    codeSmellResultsPanel = new CodeSmellResultsPanel();

    rightSplit.setTopComponent(codeEditorPanel);
    rightSplit.setBottomComponent(codeSmellResultsPanel);

    // Main split pane
    JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    mainSplit.setResizeWeight(0.25);
    mainSplit.setDividerLocation(0.25);
    mainSplit.setLeftComponent(projectTreePanel);
    mainSplit.setRightComponent(rightSplit);

    return mainSplit;
  }

  public JButton getSelectProjectButton() {
    return selectProjectButton;
  }

  public JButton getDetectButton() {
    return detectButton;
  }

  public ProjectTreePanel getProjectTreePanel() {
    return projectTreePanel;
  }

  public CodeEditorPanel getCodeEditorPanel() {
    return codeEditorPanel;
  }

  public CodeSmellResultsPanel getCodeSmellResultsPanel() {
    return codeSmellResultsPanel;
  }
}
