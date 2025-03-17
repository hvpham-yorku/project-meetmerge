package com.meetmerge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@RestController
@RequestMapping("/api/calendly")
@CrossOrigin(origins = "http://localhost:5173")
public class CalendlyController {
    
    //@Value("${CALENDLY_API_KEY}")
    //private String API_KEY; 

    @Value("${CALENDLY_CLIENT_ID}")
    private String clientId;

    @Value("${CALENDLY_CLIENT_SECRET}")
    private String clientSecret;

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/oauth-config")
    public ResponseEntity<?> getOAuthConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("clientId", clientId);
        return ResponseEntity.ok(config);
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<?> exchangeCodeForToken(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String redirectUri = request.get("redirect_uri");
        
        if (code == null) {
            return ResponseEntity.badRequest().body(String.format("Missing code - '%s'", code));
        }

        if(redirectUri == null){
            return ResponseEntity.badRequest().body(String.format("Missing redirect URI - '%s'", redirectUri));
        }
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("code", code);
            formData.add("grant_type", "authorization_code");
            formData.add("redirect_uri", redirectUri);
            
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://auth.calendly.com/oauth/token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
    
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set(HttpHeaders.ACCEPT, "application/json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.calendly.com/users/me",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getBody() == null || !response.getBody().containsKey("resource")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User data not found.");
            }

            Map<String, Object> resource = (Map<String, Object>) response.getBody().get("resource");

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", resource.get("name"));
            userData.put("email", resource.get("email"));
            userData.put("scheduling_url", resource.get("scheduling_url"));
            userData.put("timezone", resource.get("timezone"));
            
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get user info: " + e.getMessage());
        }
    }

    @GetMapping("/busy-times")
    public ResponseEntity<?> getBusyTimes(@RequestHeader("Authorization") String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set(HttpHeaders.ACCEPT, "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Fetch scheduled events
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.calendly.com/scheduled_events",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch events.");
            }

            // Extract busy times from response
            List<Map<String, Object>> events = (List<Map<String, Object>>) response.getBody().get("collection");
            List<Map<String, String>> busyTimes = new ArrayList<>();

            for (Map<String, Object> event : events) {
                Map<String, String> busySlot = new HashMap<>();
                busySlot.put("start_time", (String) event.get("start_time"));
                busySlot.put("end_time", (String) event.get("end_time"));
                busyTimes.add(busySlot);
            }

            return ResponseEntity.ok(busyTimes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching busy times: " + e.getMessage());
        }
    }
}
