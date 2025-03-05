package com.anand.BACKEND.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

@Service
public class ComplexityAnalyzer {
    public List<String> readFile (MultipartFile file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    public String calculateTimeComplexity (List<String> lines) {
        int loopCount = 0;
        int nestedLoopDepth = 0;
        Stack<Integer> loopStack = new Stack<>();

        Pattern forPattern = Pattern.compile(".*for\\s*\\(.*;.*;.*\\).*");
        Pattern whilePattern = Pattern.compile(".*while\\s*\\(.*\\).*");

        for (String line: lines) {
            if(forPattern.matcher(line).matches() || whilePattern.matcher(line).matches()) {
                loopStack.push(loopStack.isEmpty() ? 1 : loopStack.peek() + 1);
                loopCount++;
                nestedLoopDepth = Math.max(nestedLoopDepth, loopStack.peek());
            } else if (lines.contains("}")) {
                if(!loopStack.isEmpty()) loopStack.pop();
            }
        }

        if(nestedLoopDepth >= 3) return "O(n^" + nestedLoopDepth + ")";
        if(nestedLoopDepth == 2) return "O(n^2)";
        if (loopCount == 1) return "O(n)";

        return "O(1)";
    }
}
