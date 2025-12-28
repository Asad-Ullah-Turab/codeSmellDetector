package com.scd.codeSmellDetector.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeSmellAnalyzer {
    private static final int LONG_METHOD_THRESHOLD = 30;
    private static final int LARGE_CLASS_THRESHOLD = 500;
    private static final int TOO_MANY_PARAMETERS = 5;
    private static final int HIGH_CYCLOMATIC_COMPLEXITY = 10;
    private static final int DUPLICATE_CODE_THRESHOLD = 5;

    public List<CodeSmell> analyzeFile(File file) {
        List<CodeSmell> smells = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            String content = String.join("\n", lines);

            smells.addAll(detectLongMethods(file, lines));
            smells.addAll(detectLargeClass(file, lines));
            smells.addAll(detectTooManyParameters(file, lines));
            smells.addAll(detectGodObject(file, content));
            smells.addAll(detectDeadCode(file, lines));
            smells.addAll(detectHighCyclomaticComplexity(file, lines));
            smells.addAll(detectPrimitiveObsession(file, lines));
            smells.addAll(detectLongParameterList(file, lines));
            // smells.addAll(detectMissingJavadoc(file, lines));
            smells.addAll(detectMagicNumbers(file, lines));
            smells.addAll(detectFeatureEnvy(file, lines));
            smells.addAll(detectLazyClass(file, lines));
            smells.addAll(detectSpeculativeGenerality(file, lines));

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

    private List<CodeSmell> detectDeadCode(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // Detect unreachable code after return
            if (line.equals("return;") || line.startsWith("return ")) {
                if (i + 1 < lines.size()) {
                    String nextLine = lines.get(i + 1).trim();
                    if (!nextLine.isEmpty() && !nextLine.startsWith("}") && !nextLine.startsWith("//")) {
                        smells.add(new CodeSmell(
                                "Dead Code",
                                file.getName(),
                                i + 2,
                                "Unreachable code detected after return statement",
                                "Low"));
                    }
                }
            }

            // Detect unused variables
            if (line.matches(".*\\b(int|String|double|boolean|float|long)\\b\\s+\\w+\\s*=.*") && !line.contains("//")) {
                Pattern p = Pattern.compile("(int|String|double|boolean|float|long)\\s+(\\w+)\\s*=");
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String varName = m.group(2);
                    String methodContent = extractMethodContent(lines, i);
                    if (!methodContent.contains(varName)
                            || methodContent.indexOf(varName) == methodContent.lastIndexOf(varName)) {
                        smells.add(new CodeSmell(
                                "Unused Variable",
                                file.getName(),
                                i + 1,
                                "Variable '" + varName + "' appears to be unused",
                                "Low"));
                    }
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectHighCyclomaticComplexity(File file, List<String> lines) {
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

                // Count complexity
                int complexity = 1;
                complexity += line.split("if\\s*\\(").length - 1;
                complexity += line.split("else\\s*if\\s*\\(").length - 1;
                complexity += line.split("for\\s*\\(").length - 1;
                complexity += line.split("while\\s*\\(").length - 1;
                complexity += line.split("case\\s+").length - 1;
                complexity += line.split("catch\\s*\\(").length - 1;

                if (braceCount == 0 && methodStartLine != -1 && complexity > HIGH_CYCLOMATIC_COMPLEXITY) {
                    smells.add(new CodeSmell(
                            "High Cyclomatic Complexity",
                            file.getName(),
                            methodStartLine,
                            "Method has cyclomatic complexity of " + complexity + " (threshold: "
                                    + HIGH_CYCLOMATIC_COMPLEXITY + ")",
                            "Medium"));
                    inMethod = false;
                    methodStartLine = -1;
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectPrimitiveObsession(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();
        int primitiveCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.matches(".*\\b(private|public)\\s+(int|double|float|boolean|long|short|byte)\\s+\\w+.*")) {
                primitiveCount++;
            }
        }

        if (primitiveCount > 5) {
            smells.add(new CodeSmell(
                    "Primitive Obsession",
                    file.getName(),
                    1,
                    "Class uses " + primitiveCount + " primitive type fields (consider using objects)",
                    "Low"));
        }

        return smells;
    }

    private List<CodeSmell> detectLongParameterList(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.contains("(") && line.contains(")")) {
                String params = line.substring(line.indexOf("("), line.indexOf(")"));
                long paramCount = params.split(",").length;
                if (paramCount > 7) {
                    smells.add(new CodeSmell(
                            "Long Parameter List",
                            file.getName(),
                            i + 1,
                            "Method has " + paramCount + " parameters (threshold: 7)",
                            "Low"));
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectMissingJavadoc(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if ((line.startsWith("public") || line.startsWith("protected")) && line.contains("(")) {
                if (i == 0 || !lines.get(i - 1).contains("*/")) {
                    smells.add(new CodeSmell(
                            "Missing Javadoc",
                            file.getName(),
                            i + 1,
                            "Public method lacks Javadoc documentation",
                            "Low"));
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectMagicNumbers(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();
        Pattern magicPattern = Pattern.compile("\\b(\\d{2,}|[0-9]\\.\\d+)\\b");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.startsWith("//") && !line.startsWith("*")) {
                Matcher m = magicPattern.matcher(line);
                int count = 0;
                while (m.find()) {
                    count++;
                }
                if (count > 2) {
                    smells.add(new CodeSmell(
                            "Magic Numbers",
                            file.getName(),
                            i + 1,
                            "Line contains " + count + " magic numbers (consider using named constants)",
                            "Low"));
                }
            }
        }

        return smells;
    }

    private List<CodeSmell> detectFeatureEnvy(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            long methodCalls = line.split("\\.").length - 1;
            if (methodCalls > 5) {
                smells.add(new CodeSmell(
                        "Feature Envy",
                        file.getName(),
                        i + 1,
                        "Method uses " + methodCalls + " method calls from other objects (threshold: 5)",
                        "Low"));
            }
        }

        return smells;
    }

    private List<CodeSmell> detectLazyClass(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();
        long methodCount = lines.stream()
                .filter(line -> line.trim().matches(".*(public|private|protected).*\\(.*\\).*")).count();

        if (methodCount < 3 && lines.size() < 50) {
            smells.add(new CodeSmell(
                    "Lazy Class",
                    file.getName(),
                    1,
                    "Class has only " + methodCount + " methods and " + lines.size() + " lines (might be unnecessary)",
                    "Low"));
        }

        return smells;
    }

    private List<CodeSmell> detectSpeculativeGenerality(File file, List<String> lines) {
        List<CodeSmell> smells = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if ((line.contains("public") || line.contains("protected")) && line.contains("abstract")
                    && !line.contains("class")) {
                smells.add(new CodeSmell(
                        "Speculative Generality",
                        file.getName(),
                        i + 1,
                        "Unused abstract method might indicate speculative design",
                        "Low"));
            }
        }

        return smells;
    }

    private String extractMethodContent(List<String> lines, int startLine) {
        StringBuilder content = new StringBuilder();
        int braceCount = 0;
        boolean inMethod = false;

        for (int i = startLine; i < lines.size(); i++) {
            String line = lines.get(i);
            braceCount += line.chars().filter(ch -> ch == '{').count();
            braceCount -= line.chars().filter(ch -> ch == '}').count();

            if (braceCount > 0) {
                inMethod = true;
            }

            if (inMethod) {
                content.append(line).append("\n");
            }

            if (inMethod && braceCount == 0) {
                break;
            }
        }

        return content.toString();
    }
}
