package com.scd.codeSmellDetector.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CodeSmellAnalyzer {
    private static final int LONG_METHOD_THRESHOLD = 30;
    private static final int LARGE_CLASS_THRESHOLD = 500;
    private static final int TOO_MANY_PARAMETERS = 5;

    public List<CodeSmell> analyzeFile(File file) {
        List<CodeSmell> smells = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            String content = String.join("\n", lines);

            smells.addAll(detectLongMethods(file, lines));
            smells.addAll(detectLargeClass(file, lines));
            smells.addAll(detectTooManyParameters(file, lines));
            smells.addAll(detectGodObject(file, content));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return smells;
    }

    private List<CodeSmell> detectLongMethods(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();
        int methodStartLine = -1;
        int braceCount = 0;
        boolean inMethod = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.contains("(")
                    && (line.contains("public") || line.contains("private") || line.contains("protected"))) {
                methodStartLine = i + 1;
                inMethod = true;
                braceCount = 0;
            }

            if (inMethod) {
                braceCount += line.chars().filter(ch -> ch == '{').count();
                braceCount -= line.chars().filter(ch -> ch == '}').count();

                if (braceCount == 0 && methodStartLine != -1) {
                    int methodLength = i - methodStartLine + 1;
                    if (methodLength > LONG_METHOD_THRESHOLD) {
                        smells.add(new CodeSmell(
                                "Long Method",
                                file.getName(),
                                methodStartLine,
                                "Method has " + methodLength + " lines (threshold: " + LONG_METHOD_THRESHOLD + ")",
                                "Medium"));
                    }
                    inMethod = false;
                    methodStartLine = -1;
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectLargeClass(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        if (lines.size() > LARGE_CLASS_THRESHOLD) {
            smells.add(new CodeSmell(
                    "Large Class",
                    file.getName(),
                    1,
                    "Class has " + lines.size() + " lines (threshold: " + LARGE_CLASS_THRESHOLD + ")",
                    "High"));
        }

        return smells;
    }

    private List<CodeSmell> detectTooManyParameters(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.contains("(")
                    && (line.contains("public") || line.contains("private") || line.contains("protected"))) {
                int paramCount = line.split(",").length;
                if (line.contains("(") && line.contains(")") && paramCount > TOO_MANY_PARAMETERS) {
                    smells.add(new CodeSmell(
                            "Too Many Parameters",
                            file.getName(),
                            i + 1,
                            "Method has " + paramCount + " parameters (threshold: " + TOO_MANY_PARAMETERS + ")",
                            "Medium"));
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectGodObject(File file, String content) {
        List<CodeSmell> smells = new ArrayList<>();

        long methodCount = content.lines()
                .filter(line -> line.trim().matches(".*(public|private|protected).*\\(.*\\).*")).count();

        if (methodCount > 20) {
            smells.add(new CodeSmell(
                    "God Object",
                    file.getName(),
                    1,
                    "Class has " + methodCount + " methods (threshold: 20)",
                    "High"));
        }

        return smells;
    }
}
