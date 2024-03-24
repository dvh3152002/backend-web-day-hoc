package com.example.backendkhoaluan.service.imp;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadCloudinaryService {
    String uploadFile(MultipartFile file) throws IOException;

    void deleteVideo(String publicId);
}
