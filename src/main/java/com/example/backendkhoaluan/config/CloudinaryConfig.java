package com.example.backendkhoaluan.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME="dcylbuqrg";

    private final String API_KEY="913913557683551";

    private final String API_SECRET="F4W4TxnVanL30BzCfcyxJt1rWJ8";

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
