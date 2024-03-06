package com.example.backendkhoaluan.service.imp;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public String save(MultipartFile file);

    public Resource load(String filename);

    public void deleteAll(String fileName);

    public Stream<Path> loadAll();
}
