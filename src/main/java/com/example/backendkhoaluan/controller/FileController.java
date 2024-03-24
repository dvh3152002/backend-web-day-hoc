package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.service.imp.FileUploadCloudinaryService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private FileUploadCloudinaryService cloudinaryService;

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFile(@PathVariable String fileName) {
        Resource resource=filesStorageService.load(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping(value = "/video/{fileName}",produces = "video/mp4")
    public ResponseEntity<?> downloadVideoFile(@PathVariable String fileName) {
        Resource resource = filesStorageService.load(fileName);
        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String url=cloudinaryService.uploadFile(file);
        return ResponseEntity.ok().body(url);
    }
}
