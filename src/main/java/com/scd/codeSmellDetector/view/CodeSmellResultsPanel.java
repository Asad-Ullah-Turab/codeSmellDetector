package com.scd.codeSmellDetector.view;

import com.scd.codeSmellDetector.model.CodeSmell;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CodeSmellResultsPanel extends JPanel {
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel summaryLabel;

    public CodeSmellResultsPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Code Smell Analysis Results"));

        // Summary label
        summaryLabel = new JLabel("No analysis performed");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 11));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        summaryLabel.setBackground(new Color(240, 240, 240));
        summaryLabel.setOpaque(true);
        add(summaryLabel, BorderLayout.NORTH);

        // Results table
        String[] columnNames = { "Type", "Line", "Severity", "Description" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 11));
        resultsTable.setRowHeight(25);
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void displayResults(List<CodeSmell> codeSmells) {
        tableModel.setRowCount(0);

        if (codeSmells.isEmpty()) {
            summaryLabel.setText("✓ No code smells detected!");
            summaryLabel.setForeground(new Color(76, 175, 80));
        } else {
            summaryLabel.setText("⚠ Found " + codeSmells.size() + " code smell(s)");
            summaryLabel.setForeground(new Color(244, 67, 54));

            for (CodeSmell smell : codeSmells) {
                tableModel.addRow(new Object[] {
                        smell.getSmellType(),
                        smell.getLineNumber(),
                        smell.getSeverity(),
                        smell.getDescription()
                });
            }
        }
    }

    public void clearResults() {
        tableModel.setRowCount(0);
        summaryLabel.setText("No analysis performed");
        summaryLabel.setForeground(Color.BLACK);
    }
}
