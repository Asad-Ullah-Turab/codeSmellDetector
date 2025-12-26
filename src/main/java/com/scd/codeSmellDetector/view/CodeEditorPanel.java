package com.scd.codeSmellDetector.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CodeEditorPanel extends JPanel {
    private JTextArea codeArea;
    private JLabel fileNameLabel;

    public CodeEditorPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Code Editor"));

        // File name header
        fileNameLabel = new JLabel("No file selected");
        fileNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fileNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        fileNameLabel.setBackground(new Color(240, 240, 240));
        fileNameLabel.setOpaque(true);
        add(fileNameLabel, BorderLayout.NORTH);

        // Code area
        codeArea = new JTextArea();
        codeArea.setEditable(false);
        codeArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        codeArea.setBackground(new Color(30, 30, 30));
        codeArea.setForeground(new Color(200, 200, 200));
        codeArea.setTabSize(4);

        JScrollPane scrollPane = new JScrollPane(codeArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void displayCode(String fileName, String code) {
        fileNameLabel.setText("📄 " + fileName);
        codeArea.setText(code);
        codeArea.setCaretPosition(0);
    }

    public void clearCode() {
        fileNameLabel.setText("No file selected");
        codeArea.setText("");
    }

    public String getCode() {
        return codeArea.getText();
    }
}
