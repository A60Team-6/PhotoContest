package com.telerikacademy.web.photocontest.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    /* ToDo PLACE IN APPLICATION.PROPERTIES FILE! */

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "deeelijg4",
                "api_key", "321742564111219",
                "api_secret", "AajgJ7zRiuhOui0_5UEgdWPN5PM"
        );
        return new Cloudinary(config);
    }
}
