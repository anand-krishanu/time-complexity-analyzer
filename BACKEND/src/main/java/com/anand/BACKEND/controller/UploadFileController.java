package com.anand.BACKEND.controller;

import com.anand.BACKEND.service.ComplexityAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class UploadFileController {
    @Autowired
    private final ComplexityAnalyzer complexityAnalyzer;

    public UploadFileController(ComplexityAnalyzer complexityAnalyzer) {
        this.complexityAnalyzer = complexityAnalyzer;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile (@RequestParam("file")MultipartFile file) {
        try {
            List<String> lines = complexityAnalyzer.readFile(file);
            String complexity = complexityAnalyzer.calculateTimeComplexity(lines);

            String scrapedCode = String.join("\n", lines);

            Map<String, String> response = new HashMap<>();
            response.put("analysis", "The time complexity of the code is: " + complexity);
            response.put("scrapedCode", scrapedCode);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
