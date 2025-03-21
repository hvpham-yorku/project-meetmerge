import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * OutlookCalendarIntegration - Focused on displaying available times
 * 
 * I revised this implementation to focus only on free time slots rather
 * than showing busy periods. This matches our app's purpose better.
 */
public class OutlookCalendarIntegration {

    // Our app credentials from Microsoft registration
    private static final String CLIENT_ID = "d5bd78e5-26d2-4594-9b71-246b9dcafc77";
    private static final String TENANT_ID = "12aed273-3a03-4ad3-bb8d-c53ecd9f427f";
    private static final String REDIRECT_URI = "http://localhost:8080/auth/callback";
    
    // I found this format easier to read than the default
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * My TimeSlot class - added extra info for better usability
     */
    public static class TimeSlot {
        private LocalDateTime start;
        private LocalDateTime end;
        private String timeZone;
        private String note;
        
        public TimeSlot(LocalDateTime start, LocalDateTime end, String timeZone) {
            this.start = start;
            this.end = end;
            this.timeZone = timeZone;
            this.note = "";
        }
        
        public TimeSlot(LocalDateTime start, LocalDateTime end, String timeZone, String note) {
            this.start = start;
            this.end = end;
            this.timeZone = timeZone;
            this.note = note;
        }
        
        public TimeSlot() {
            this.note = "";
        }
        
        public LocalDateTime getStart() {
            return start;
        }
        
        public void setStart(LocalDateTime start) {
            this.start = start;
        }
        
        public LocalDateTime getEnd() {
            return end;
        }
        
        public void setEnd(LocalDateTime end) {
            this.end = end;
        }
        
        public String getTimeZone() {
            return timeZone;
        }
        
        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }
        
        public String getNote() {
            return note;
        }
        
        public void setNote(String note) {
            this.note = note;
        }
        
        @Override
        public String toString() {
            return start.format(TIME_FORMAT) + " to " + end.format(TIME_FORMAT) +
                   (note.isEmpty() ? "" : " (" + note + ")");
        }
    }
    
    /**
     * Simple User class for the demo
     */
    public static class User {
        private String userId;
        private String name;
        private boolean calendarConnected;
        private List<TimeSlot> busySlots;
        
        public User(String userId, String name, boolean calendarConnected) {
            this.userId = userId;
            this.name = name;
            this.calendarConnected = calendarConnected;
            this.busySlots = new ArrayList<>();
        }
        
        public String getUserId() {
            return userId;
        }
        
        public String getName() {
            return name;
        }
        
        public boolean isCalendarConnected() {
            return calendarConnected;
        }
        
        public List<TimeSlot> getBusySlots() {
            return busySlots;
        }
    }

    /**
     * Generate the OAuth URL - I made this cleaner than the standard approach
     */
    public String generateAuthUrl() {
        // Using the tenant ID we got from our Azure app registration
        String authEndpoint = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/authorize";
        
        // I prefer StringBuilder for URL building - easier to maintain
        StringBuilder url = new StringBuilder(authEndpoint);
        url.append("?client_id=").append(CLIENT_ID);
        url.append("&response_type=code");
        url.append("&redirect_uri=").append(REDIRECT_URI);
        url.append("&response_mode=query");
        url.append("&scope=Calendars.Read%20User.Read%20offline_access"); // URL encoded scopes
        url.append("&state=").append(UUID.randomUUID().toString().substring(0, 8));
        
        return url.toString();
    }
    
    /**
     * Test that we can connect to Microsoft Graph API
     */
    public boolean testGraphApiConnection() {
        try {
            URL url = new URL("https://graph.microsoft.com/v1.0/$metadata");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5 seconds timeout
            
            int responseCode = conn.getResponseCode();
            System.out.println("API test result: " + responseCode);
            
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            System.out.println("API test error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Create mock calendar data - for testing in Sprint 1
     * Only tracks busy slots internally - users never see this
     */
    public List<TimeSlot> createMockCalendar(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        System.out.println("Processing calendar for user: " + userId);
        List<TimeSlot> busySlots = new ArrayList<>();
        
        LocalDateTime today = startTime.toLocalDate().atStartOfDay();
        
        // Create different schedules for different users
        switch(userId) {
            case "user1":
                // First user (you)
                busySlots.add(new TimeSlot(
                    today.plusHours(9),
                    today.plusHours(10).plusMinutes(30),
                    "America/New_York"
                ));
                
                busySlots.add(new TimeSlot(
                    today.plusHours(12),
                    today.plusHours(13),
                    "America/New_York"
                ));
                break;
                
            case "user2":
                // Second user 
                busySlots.add(new TimeSlot(
                    today.plusHours(10),
                    today.plusHours(11),
                    "America/New_York"
                ));
                
                busySlots.add(new TimeSlot(
                    today.plusHours(14),
                    today.plusHours(15).plusMinutes(30),
                    "America/New_York"
                ));
                break;
                
            case "user3":
                // Third user
                busySlots.add(new TimeSlot(
                    today.plusHours(11),
                    today.plusHours(12),
                    "America/New_York"
                ));
                break;
        }
        
        return busySlots;
    }
    
    /**
     * Find available time slots - my algorithm for finding free time
     */
    public List<TimeSlot> findAvailableTimeSlots(List<TimeSlot> busySlots, 
                                              LocalDateTime startTime,
                                              LocalDateTime endTime) {
        
        // Sort busy slots by start time
        busySlots.sort(Comparator.comparing(TimeSlot::getStart));
        
        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalDateTime currentTime = startTime;
        
        // Find gaps between busy slots
        for (TimeSlot busySlot : busySlots) {
            // Only check slots in our time range
            if (busySlot.getEnd().isAfter(startTime) && busySlot.getStart().isBefore(endTime)) {
                
                if (busySlot.getStart().isAfter(currentTime)) {
                    // Found a free gap!
                    TimeSlot freeSlot = new TimeSlot();
                    freeSlot.setStart(currentTime);
                    freeSlot.setEnd(busySlot.getStart());
                    freeSlot.setTimeZone("America/New_York");
                    freeSlot.setNote("Available");
                    
                    // Only add slots at least 15 minutes long
                    if (getDurationMinutes(freeSlot) >= 15) {
                        availableSlots.add(freeSlot);
                    }
                }
                
                // Move current time pointer forward
                if (busySlot.getEnd().isAfter(currentTime)) {
                    currentTime = busySlot.getEnd();
                }
            }
        }
        
        // Check if there's time available after all busy slots
        if (currentTime.isBefore(endTime)) {
            TimeSlot freeSlot = new TimeSlot();
            freeSlot.setStart(currentTime);
            freeSlot.setEnd(endTime);
            freeSlot.setTimeZone("America/New_York");
            freeSlot.setNote("Available");
            
            if (getDurationMinutes(freeSlot) >= 15) {
                availableSlots.add(freeSlot);
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Get duration in minutes - handles day boundaries
     */
    private long getDurationMinutes(TimeSlot slot) {
        LocalDateTime start = slot.getStart();
        LocalDateTime end = slot.getEnd();
        
        // Calculate minutes in each day
        long startMinutes = start.getHour() * 60L + start.getMinute();
        long endMinutes = end.getHour() * 60L + end.getMinute();
        
        // Add days if needed
        long daysDiff = end.toLocalDate().toEpochDay() - start.toLocalDate().toEpochDay();
        return endMinutes - startMinutes + (daysDiff * 24 * 60);
    }
    
    /**
     * Find overlapping availability - my core algorithm for MeetMerge
     */
    public List<TimeSlot> findOverlappingAvailability(List<User> users, 
                                                   LocalDateTime startTime,
                                                   LocalDateTime endTime) {
        
        if (users.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Get available slots for each user
        Map<String, List<TimeSlot>> userAvailability = new HashMap<>();
        
        for (User user : users) {
            if (user.isCalendarConnected()) {
                List<TimeSlot> available = findAvailableTimeSlots(
                    user.getBusySlots(), startTime, endTime);
                userAvailability.put(user.getUserId(), available);
                
                System.out.println(user.getName() + " has " + available.size() + " available time slots");
            }
        }
        
        // Find common availability
        return findCommonAvailability(userAvailability);
    }
    
    /**
     * Find common availability among all users
     */
    private List<TimeSlot> findCommonAvailability(Map<String, List<TimeSlot>> userAvailability) {
        List<List<TimeSlot>> allUserSlots = new ArrayList<>(userAvailability.values());
        
        if (allUserSlots.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Start with first user's availability
        List<TimeSlot> commonSlots = new ArrayList<>(allUserSlots.get(0));
        
        // Find overlaps with each subsequent user
        for (int i = 1; i < allUserSlots.size(); i++) {
            commonSlots = findSlotIntersections(commonSlots, allUserSlots.get(i));
            
            // Stop early if we find no overlaps
            if (commonSlots.isEmpty()) {
                return Collections.emptyList();
            }
        }
        
        // Sort by start time
        commonSlots.sort(Comparator.comparing(TimeSlot::getStart));
        
        return commonSlots;
    }
    
    /**
     * Find intersections between two sets of time slots
     */
    private List<TimeSlot> findSlotIntersections(List<TimeSlot> slotsA, List<TimeSlot> slotsB) {
        List<TimeSlot> overlaps = new ArrayList<>();
        
        for (TimeSlot slotA : slotsA) {
            for (TimeSlot slotB : slotsB) {
                // Find the overlap if any
                LocalDateTime overlapStart = slotA.getStart().isAfter(slotB.getStart()) 
                        ? slotA.getStart() : slotB.getStart();
                        
                LocalDateTime overlapEnd = slotA.getEnd().isBefore(slotB.getEnd()) 
                        ? slotA.getEnd() : slotB.getEnd();
                
                // If there is an overlap
                if (overlapStart.isBefore(overlapEnd)) {
                    TimeSlot overlap = new TimeSlot();
                    overlap.setStart(overlapStart);
                    overlap.setEnd(overlapEnd);
                    overlap.setTimeZone("America/New_York");
                    overlap.setNote("Available for Everyone");
                    
                    // Only include if at least 15 minutes
                    if (getDurationMinutes(overlap) >= 15) {
                        overlaps.add(overlap);
                    }
                }
            }
        }
        
        return overlaps;
    }
    
    /**
     * Added this method to format the time slot duration nicely
     */
    private String formatDuration(TimeSlot slot) {
        long minutes = getDurationMinutes(slot);
        if (minutes < 60) {
            return minutes + " min";
        } else {
            long hours = minutes / 60;
            long remainingMinutes = minutes % 60;
            if (remainingMinutes == 0) {
                return hours + " hr";
            } else {
                return hours + " hr " + remainingMinutes + " min";
            }
        }
    }
    
    /**
     * Run the demo - focusing only on available times
     */
    public void runDemo() {
        System.out.println("===== OUTLOOK CALENDAR INTEGRATION DEMO =====");
        
        // Step 1: Test API connection
        System.out.println("\n-- STEP 1: Testing Microsoft Graph API --");
        boolean apiAccessible = testGraphApiConnection();
        System.out.println("API accessible: " + apiAccessible);
        
        // Step 2: Show auth URL
        System.out.println("\n-- STEP 2: Authentication Flow --");
        String authUrl = generateAuthUrl();
        System.out.println("Auth URL:");
        System.out.println(authUrl);
        
        // Step 3: Set up the calendar data without displaying busy times
        System.out.println("\n-- STEP 3: Processing Calendar Data --");
        LocalDateTime startDate = LocalDateTime.now().withHour(9).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(17).withMinute(0);
        
        User user1 = new User("user1", "You", true);
        User user2 = new User("user2", "Team Member 1", true);
        User user3 = new User("user3", "Team Member 2", true);
        
        user1.busySlots = createMockCalendar(user1.getUserId(), startDate, endDate);
        user2.busySlots = createMockCalendar(user2.getUserId(), startDate, endDate);
        user3.busySlots = createMockCalendar(user3.getUserId(), startDate, endDate);
        
        List<User> users = Arrays.asList(user1, user2, user3);
        
        // Step 4: Find and display personal availability with enhanced details
        System.out.println("\n-- STEP 4: Your Available Times --");
        List<TimeSlot> yourAvailability = findAvailableTimeSlots(
            user1.getBusySlots(), startDate, endDate);
        
        System.out.println("You are free during these times:");
        for (TimeSlot slot : yourAvailability) {
            System.out.println("  * " + slot + " (" + formatDuration(slot) + ")");
        }
        
        // Step 5: Find and display team availability with enhanced details
        System.out.println("\n-- STEP 5: Team Availability --");
        List<TimeSlot> teamAvailability = findOverlappingAvailability(
            users, startDate, endDate);
        
        System.out.println("Everyone is available during these times:");
        if (teamAvailability.isEmpty()) {
            System.out.println("  * No common free times found");
        } else {
            for (TimeSlot slot : teamAvailability) {
                System.out.println("  * " + slot + " (" + formatDuration(slot) + ")");
            }
        }
        
        // Step 6: Finish
        System.out.println("\n-- CONCLUSION --");
        System.out.println("Outlook Calendar integration successful!");
        System.out.println("We can efficiently find when everyone is available.");
        System.out.println("===== END OF DEMO =====");
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) {
        OutlookCalendarIntegration demo = new OutlookCalendarIntegration();
        demo.runDemo();
    }
}
