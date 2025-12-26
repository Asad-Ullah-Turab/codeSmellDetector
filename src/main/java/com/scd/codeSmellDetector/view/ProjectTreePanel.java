package com.scd.codeSmellDetector.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;

public class ProjectTreePanel extends JPanel {
    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode root;

    public ProjectTreePanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Project Files"));

        root = new DefaultMutableTreeNode("Project");
        treeModel = new DefaultTreeModel(root);
        fileTree = new JTree(treeModel);
        fileTree.setFont(new Font("Arial", Font.PLAIN, 11));
        fileTree.setRowHeight(20);
        fileTree.setCellRenderer(new FileTreeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(fileTree);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadProjectTree(File projectDirectory) {
        root.removeAllChildren();
        root.setUserObject(projectDirectory);
        loadDirectory(projectDirectory, root);
        treeModel.reload();
        expandTree();
    }

    private void loadDirectory(File directory, DefaultMutableTreeNode node) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (shouldIncludeFile(file)) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file);
                    node.add(childNode);
                    if (file.isDirectory()) {
                        loadDirectory(file, childNode);
                    }
                }
            }
        }
    }

    private boolean shouldIncludeFile(File file) {
        String name = file.getName();
        if (file.isDirectory()) {
            return !name.equals("target") && !name.equals(".git") && !name.startsWith(".");
        }
        return name.endsWith(".java");
    }

    private void expandTree() {
        for (int i = 0; i < fileTree.getRowCount(); i++) {
            fileTree.expandRow(i);
        }
    }

    public JTree getFileTree() {
        return fileTree;
    }

    public File getSelectedFile() {
        Object selectedNode = fileTree.getLastSelectedPathComponent();
        if (selectedNode instanceof DefaultMutableTreeNode) {
            Object userObject = ((DefaultMutableTreeNode) selectedNode).getUserObject();
            if (userObject instanceof File) {
                File file = (File) userObject;
                if (file.isFile()) {
                    return file;
                }
            }
        }
        return null;
    }

    private static class FileTreeCellRenderer extends JLabel implements TreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if (userObject instanceof File) {
                File file = (File) userObject;
                setText(file.getName());
            } else {
                setText(userObject.toString());
            }

            setBackground(selected ? new Color(63, 81, 181) : Color.WHITE);
            setForeground(selected ? Color.WHITE : Color.BLACK);
            setOpaque(selected);

            return this;
        }
    }
}
