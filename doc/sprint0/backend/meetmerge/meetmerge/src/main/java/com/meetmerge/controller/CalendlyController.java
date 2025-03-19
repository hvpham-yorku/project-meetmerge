package com.meetmerge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/availability-schedule")
    public ResponseEntity<?> getUserAvailabilitySchedule(@RequestHeader("Authorization") String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set(HttpHeaders.ACCEPT, "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            //Need current user info to get the user Uri
            ResponseEntity<Map> userResponse = restTemplate.exchange(
                "https://api.calendly.com/users/me",
                HttpMethod.GET,
                entity,
                Map.class
            );

            if (userResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch user information.");
            }

            Map<String, Object> userBody = userResponse.getBody();
            if (userBody == null || !userBody.containsKey("resource")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user information.");
            }

            Map<String, Object> userResource = (Map<String, Object>) userBody.get("resource");
            String userUri = (String) userResource.get("uri");  // Get user URI

             // Fetch the user's availability schedule
             String availabilityUrl = "https://api.calendly.com/user_availability_schedules?user=" + userUri;
             ResponseEntity<Map> response = restTemplate.exchange(
                availabilityUrl,
                HttpMethod.GET,
                entity,
                Map.class
                );
            
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch events.");
                }       

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("collection")) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No availability schedule found.");
            }
    
            List<Map<String, Object>> schedules = (List<Map<String, Object>>) responseBody.get("collection");
    

             //Use Streams to flatten the rules and intervals into a single list
            List<Map<String, String>> availabilityList = schedules.stream()
                .flatMap(schedule -> ((List<Map<String, Object>>) schedule.get("rules")).stream())
                .flatMap(rule -> ((List<Map<String, Object>>) rule.get("intervals")).stream()
                        .map(interval -> Map.of(
                                "wday", (String) rule.get("wday"),
                                "date", rule.getOrDefault("date", "N/A").toString(),
                                "from", (String) interval.get("from"),
                                "to", (String) interval.get("to")
                        ))
                )
                .collect(Collectors.toList());

            return ResponseEntity.ok(availabilityList);

    } catch(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching availability: " + e.getMessage());
    }
}
}
