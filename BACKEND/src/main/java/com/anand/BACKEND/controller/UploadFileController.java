package com.anand.BACKEND.controller;

import com.anand.BACKEND.service.ComplexityAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<String> uploadFile (@RequestParam("file")MultipartFile file) {
        try {
            List<String> lines = complexityAnalyzer.readFile(file);
            String complexity = complexityAnalyzer.calculateTimeComplexity(lines);

            return ResponseEntity.ok("The time Complexity of the code is:  " + complexity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
