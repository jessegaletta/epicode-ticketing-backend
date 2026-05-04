package edu.epicode.ticketing.tools;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getImageUploader(@Value("${cloudinary.name}") String cloudName,
                                       @Value("${cloudinary.apiKey}") String apiKey,
                                       @Value("${cloudinary.secret}") String secret){
        Map<String, String> configuration = new HashMap<>();
        configuration.put("cloud_name", cloudName);
        configuration.put("api_key", apiKey);
        configuration.put("api_secret", secret);
        return new Cloudinary(configuration);
    }
}
