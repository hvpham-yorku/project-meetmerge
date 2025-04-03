package com.meetmerge.controller;

import com.meetmerge.service.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/google")
public class GoogleController {

    @Autowired
    private GoogleService googleService;

    //  Redirect user to Google OAuth login
    @GetMapping("/auth")
    public void redirectToGoogleAuth(HttpServletResponse response) throws IOException {
        String authUrl = googleService.getGoogleAuthUrl();
        response.sendRedirect(authUrl);
    }

    // Handle Google OAuth callback and extract calendar access token
    @GetMapping("/auth/callback")
    public ResponseEntity<Map<String, String>> handleGoogleCallback(@RequestParam("code") String code) {
        try {
            //FIX: exchangeCodeForTokens() returns the access token
            String accessToken = googleService.exchangeCodeForTokens(code);

            //send access token back to frontend
            Map<String, String> response = new HashMap<>();
            response.put("access_token", accessToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Authentication failed: " + e.getMessage()));
        }
    }

    //  Fetch all free slots for a month
    @GetMapping("/calendar/free-slots")  // 🔹 FIXED: Matches frontend route
    public ResponseEntity<List<String>> getFreeSlots() {
        try {
            List<String> freeSlots = googleService.getFreeSlots();
            return ResponseEntity.ok(freeSlots);
        } catch (Exception e) {
            System.err.println(" Error fetching free slots: " + e.getMessage()); // 🔹 Log errors for debugging
            return ResponseEntity.status(500).body(null);
        }
    }
}
