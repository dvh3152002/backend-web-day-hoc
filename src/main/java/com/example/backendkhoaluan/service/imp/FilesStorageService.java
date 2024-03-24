package com.example.backendkhoaluan.service.imp;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public String save(String path, MultipartFile file);

    Resource loadImg(String path, String fileName);

    InputStream loadVideo(String path, String filename);
    public void deleteAll(String path,String fileName);

    public Stream<Path> loadAll(String url);
}
