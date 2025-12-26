package com.scd.codeSmellDetector.model;

public class CodeSmell {
    private String smellType;
    private String fileName;
    private int lineNumber;
    private String description;
    private String severity;

    public CodeSmell(String smellType, String fileName, int lineNumber, String description, String severity) {
        this.smellType = smellType;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.description = description;
        this.severity = severity;
    }

    public String getSmellType() {
        return smellType;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }
}
