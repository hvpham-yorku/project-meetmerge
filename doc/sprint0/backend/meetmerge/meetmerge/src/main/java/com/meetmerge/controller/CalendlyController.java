package com.meetmerge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/calendly")
@CrossOrigin(origins = "http://localhost:5173")
public class CalendlyController {
    
    @Value("${CALENDLY_API_KEY}")
    private String API_KEY; 

    @PostMapping("/validate-links")
    public ResponseEntity<?> validateLinks(@RequestBody List<String> links) {
        RestTemplate restTemplate = new RestTemplate();

        if (links == null || links.isEmpty()) {
            return ResponseEntity.badRequest().body("No links provided!");
        }

        for (String link : links) {
            if (!isValidCalendlyLink(link)) {
                return ResponseEntity.badRequest().body("Invalid Calendly link format: " + link);
            }

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + API_KEY);
                headers.set(HttpHeaders.ACCEPT, "application/json");

                HttpEntity<String> entity = new HttpEntity<>(headers);

                // Send a GET request to see if the link exists
                ResponseEntity<String> response = restTemplate.exchange(link, HttpMethod.HEAD, entity, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    return ResponseEntity.badRequest().body("Calendly link does not exist: " + link);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Calendly link does not exist: " + link);
            }
        }
        return ResponseEntity.ok("All links are valid!");
    }

    private boolean isValidCalendlyLink(String link) {
        String regex = "^(https?://calendly\\.com/[a-zA-Z0-9_-]+(/[a-zA-Z0-9_-]+)?)$";
        return link.matches(regex);
    }
}
