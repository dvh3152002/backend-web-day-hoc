package com.example.backendkhoaluan.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.backendkhoaluan.exception.FileException;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImp implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Value("${root.path.image}")
    private String path;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String id = UUID.randomUUID().toString();
            cloudinary.uploader().upload(file.getBytes(),
                            Map.of("resource_type", "image",
                                    "folder", path,
                                    "public_id", id))
                    .get("secure_url");
            return path + "/" + id;
        } catch (Exception e) {
            throw new FileException(e.getLocalizedMessage());
        }
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.api().deleteResources(Collections.singletonList(publicId),
                    ObjectUtils.asMap("type", "upload", "resource_type", "image"));
        } catch (Exception e) {
            throw new FileException(e.getLocalizedMessage());
        }
    }

    @Override
    public String getImageUrl(String publicId){
        try {
            Map result = cloudinary.api().resource(publicId, ObjectUtils.asMap("type", "upload", "resource_type", "image"));
            return (String) result.get("url");
        }catch (Exception e) {
            throw new FileException(e.getLocalizedMessage());
        }
    }
}
