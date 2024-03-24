package com.example.backendkhoaluan.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.service.imp.FileUploadCloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadCloudinaryServiceImp implements FileUploadCloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String id=UUID.randomUUID().toString();
            cloudinary.uploader().upload(file.getBytes(),
                            Map.of("resource_type","video",
                                    "folder","videos",
                                    "public_id", id))
                    .get("secure_url");
            return "videos/"+id;
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void deleteVideo(String publicId) {
        try {
            cloudinary.api().deleteResources(Arrays.asList(publicId),
                    ObjectUtils.asMap("type", "upload", "resource_type", "video"));
        } catch (Exception e) {
            throw new DeleteException("Lỗi xóa video ",e.getLocalizedMessage());
        }
    }
}
