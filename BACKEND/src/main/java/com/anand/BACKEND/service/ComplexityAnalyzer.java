package com.anand.BACKEND.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ComplexityAnalyzer {

    public List<String> readFile(MultipartFile file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public String calculateTimeComplexity(List<String> lines) {
        int loopCount = 0;
        int nestedLoopDepth = 0;
        boolean isRecursive = false;
        String methodName = "";
        Stack<Integer> loopStack = new Stack<>();

        Pattern forPattern = Pattern.compile(".*for\\s*\\(.*;.*;.*\\).*");
        Pattern whilePattern = Pattern.compile(".*while\\s*\\(.*\\).*");

        Pattern methodPattern = Pattern.compile("\\b(?:public|private|protected|static)?\\s*(?:<\\w+>)?\\s*(\\w+)\\s*\\(.*\\)\\s*\\{?");
        Pattern methodCallPattern = null;

        for (String line : lines) {
            line = line.trim();

            if (methodName.isEmpty()) {
                Matcher methodMatcher = methodPattern.matcher(line);
                if (methodMatcher.find()) {
                    methodName = methodMatcher.group(1); // Extract method name
                    methodCallPattern = Pattern.compile("\\b" + methodName + "\\s*\\(.*\\)\\s*;?");
                }
            }

            if (methodCallPattern != null && methodCallPattern.matcher(line).find()) {
                isRecursive = true;
            }

            if (forPattern.matcher(line).matches() || whilePattern.matcher(line).matches()) {
                loopStack.push(loopStack.isEmpty() ? 1 : loopStack.peek() + 1);
                loopCount++;
                nestedLoopDepth = Math.max(nestedLoopDepth, loopStack.peek());
            } else if (line.equals("}")) {
                if (!loopStack.isEmpty()) loopStack.pop();
            }
        }

        if (isRecursive) return "O(2^n) (Recursive Algorithm Detected)";
        if (nestedLoopDepth >= 3) return "O(n^" + nestedLoopDepth + ")";
        if (nestedLoopDepth == 2) return "O(n^2)";
        if (loopCount == 1) return "O(n)";

        return "O(1)";
    }
}
