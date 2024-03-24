package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.exception.FileException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class FilesStorageServiceImp implements FilesStorageService {
    @Override
    @Transactional(rollbackFor = {Exception.class, InsertException.class, UpdateException.class})
    public String save(String path,MultipartFile file) {
        try {
            String fileName=file.getOriginalFilename();

            String randomId= UUID.randomUUID().toString();
            String finalName=randomId.concat(fileName).substring(fileName.indexOf("."));

            String filePath=path+File.separator+finalName;

            File f=new File(path);
            if(!f.exists()){
                f.mkdir();
            }

            Files.copy(file.getInputStream(),Paths.get(filePath));
            return finalName;
        }catch (Exception e){
            throw new FileException(e.getLocalizedMessage());
        }
    }

@Override
public Resource loadImg(String path,String filename) {
    try {
        Path root = Paths.get(path);
        Path file = root.resolve(filename);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    } catch (MalformedURLException e) {
        throw new RuntimeException("Error: " + e.getMessage());
    }
}

@Override
public InputStream loadVideo(String path, String filename) {
    try {
        String fullPath=path+File.separator+filename;
        InputStream inputStream=new FileInputStream(fullPath);

        return inputStream;
    }catch (Exception e){
        throw new FileException(e.getLocalizedMessage());
    }
}

    @Override
    @Transactional
    public void deleteAll(String path,String fileName) {
        log.info("file: ",path+ File.separator+fileName);
        Path root = Paths.get(path+ File.separator+fileName);
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll(String url) {
        try {
            Path root = Paths.get(url);
            return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
