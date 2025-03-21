package com.meetmerge.controller;

import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "http://localhost:5173") //Allow frontend requests
public class MeetingController {

    @PostMapping("/merge")
    public String mergeCalendars(@RequestBody List<String> calendarLinks){
        //placeholder logic
        return "Merged availability for: " + String.join(", ", calendarLinks);

    }

    @GetMapping("/test")
    public String test(){
        //placeholder logic
        return "MeetMerge backend is running";

    }
}