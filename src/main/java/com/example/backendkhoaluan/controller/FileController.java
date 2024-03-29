package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.exception.FileException;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Value("${root.path.image}")
    private String pathImg;

    @Value("${root.path.video}")
    private String pathVideo;

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFile(@PathVariable String fileName) {
        Resource resource=filesStorageService.loadImg(pathImg,fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping(value = "/video/{fileName}",produces = MediaType.ALL_VALUE)
    public void downloadVideoFile(@PathVariable String fileName, HttpServletResponse response) {
        try {
            InputStream resource=filesStorageService.loadVideo(pathVideo,fileName);
            response.setContentType(MediaType.ALL_VALUE);
            StreamUtils.copy(resource,response.getOutputStream());
        }catch (Exception e){
            throw new FileException(e.getLocalizedMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String url=cloudinaryService.uploadFile(file);
        return ResponseEntity.ok().body(url);
    }
}
