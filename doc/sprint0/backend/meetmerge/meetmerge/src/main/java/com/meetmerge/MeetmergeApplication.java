package com.meetmerge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MeetmergeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetmergeApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //  Allow all API paths
                        .allowedOrigins("http://localhost:5173") //  Allow frontend requests
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //  Allow these HTTP methods
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
