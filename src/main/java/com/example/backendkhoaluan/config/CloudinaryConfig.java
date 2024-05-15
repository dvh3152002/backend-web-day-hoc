package com.example.backendkhoaluan.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${root.cloud.name}")
    private final String CLOUD_NAME="dcylbuqrg";

    @Value("${root.cloud.key}")
    private final String API_KEY="913913557683551";

    @Value("${root.cloud.secret}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
