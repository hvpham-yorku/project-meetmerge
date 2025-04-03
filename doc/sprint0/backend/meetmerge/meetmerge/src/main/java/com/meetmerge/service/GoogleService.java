package com.meetmerge.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;
import com.google.api.client.util.DateTime;
import com.google.api.client.http.HttpTransport;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class GoogleService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/calendar.readonly");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to initialize HTTP transport", e);
        }
    }

    private String accessToken;
    private String refreshToken;

    // Build client secrets from properties
    private GoogleClientSecrets buildClientSecrets() {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        return new GoogleClientSecrets().setInstalled(details);
    }

    // Step 1: Generate Google OAuth URL
    public String getGoogleAuthUrl() throws IOException {
        GoogleClientSecrets clientSecrets = buildClientSecrets();
        AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .set("access_type", "offline")
                .set("prompt", "consent");

        return authorizationUrl.build();
    }

    // Step 2: Exchange authorization code for access token
    public String exchangeCodeForTokens(String code) throws IOException {
        GoogleClientSecrets clientSecrets = buildClientSecrets();
        AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
        this.accessToken = response.getAccessToken();
        this.refreshToken = response.getRefreshToken();

        if (refreshToken != null) {
            System.out.println("Refresh Token: " + refreshToken);
        }

        return this.accessToken;
    }

    // Step 3: Fetch all free slots for the next month
    public List<String> getFreeSlots() throws IOException {
        if (accessToken == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));
        Calendar calendarService = new Calendar.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName("MeetMerge")
                .build();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime nextMonth = now.plusMonths(1);

        DateTime dateTimeMin = new DateTime(now.toInstant().toEpochMilli());
        DateTime dateTimeMax = new DateTime(nextMonth.toInstant().toEpochMilli());

        Events events = calendarService.events().list("primary")
                .setTimeMin(dateTimeMin)
                .setTimeMax(dateTimeMax)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> eventList = events.getItems();
        List<String> freeSlots = new ArrayList<>();

        ZonedDateTime lastEndTime = now;
        for (Event event : eventList) {
            DateTime startDateTime = event.getStart().getDateTime();
            if (startDateTime == null) {
                startDateTime = event.getStart().getDate();
            }

            DateTime endDateTime = event.getEnd().getDateTime();
            if (endDateTime == null) {
                endDateTime = event.getEnd().getDate();
            }

            ZonedDateTime eventStartTime;
            ZonedDateTime eventEndTime;

            try {
                eventStartTime = Instant.parse(startDateTime.toStringRfc3339()).atZone(ZoneId.systemDefault());
                eventEndTime = Instant.parse(endDateTime.toStringRfc3339()).atZone(ZoneId.systemDefault());
            } catch (Exception e) {
                eventStartTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startDateTime.getValue()), ZoneId.systemDefault());
                eventEndTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(endDateTime.getValue()), ZoneId.systemDefault());
            }

            if (lastEndTime.isBefore(eventStartTime)) {
                freeSlots.add(lastEndTime + " to " + eventStartTime);
            }

            lastEndTime = eventEndTime;
        }

        if (lastEndTime.isBefore(nextMonth)) {
            freeSlots.add(lastEndTime + " to " + nextMonth);
        }

        return freeSlots;
    }
} 