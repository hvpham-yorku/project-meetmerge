package com.meetmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;



import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

@RestController
@RequestMapping("/calendly")
public class CalendlyController {
    
    private final Dotenv dotenv = Dotenv.load(); // Load environment variables
    private String API_KEY = dotenv.get("CALENDLY_API_KEY"); 

    @PostMapping("/validate-links")
    public ResponseEntity<?> validateLinks(@RequestBody List<String> links) {
        RestTemplate restTemplate = new RestTemplate();

        for (String link : links) {
            if (!isValidCalendlyLink(link)) {
                return ResponseEntity.badRequest().body("Invalid Calendly link format: " + link);
            }

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + API_KEY);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                String apiUrl = "https://api.calendly.com/users/me/scheduled_events";
                ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

                if (response.getStatusCode() != HttpStatus.OK) {
                    return ResponseEntity.badRequest().body("Calendly link does not exist: " + link);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Calendly link does not exist: " + link);
            }
        }
        return ResponseEntity.ok("All links are valid!");
    }

    private boolean isValidCalendlyLink(String link) {
        String regex = "^(https?://calendly\\\\.com/[a-zA-Z0-9_-]+(/[a-zA-Z0-9_-]+)?)$";
        return link.matches(regex);
    }
}

