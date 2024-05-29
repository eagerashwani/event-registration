package com.thinkify.event.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkify.event.dto.EventDto;
import com.thinkify.event.entity.Event;
import com.thinkify.event.service.EventService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/event")
public class EventController {
    
    @Autowired
    private EventService eventService;

    @GetMapping("/get-one-event/{id}")
    @PreAuthorize("hasAnyRole('ATTENDEE', 'ADMIN')")
    public ResponseEntity<Event> getOneEvent(@PathVariable Long id) {
        Event event = eventService.getEvent(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping("/get-all-events")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN', 'ATTENDEE')")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> allEvents = eventService.getAllEvents();
        return new ResponseEntity<>(allEvents, HttpStatus.OK); 
    }

    @PostMapping("/create-event")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> createEvent(@RequestBody EventDto event) {
        String createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("update-event/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody EventDto event) {
      boolean updateEvent = eventService.updateEvent(event, id);
      if(updateEvent)
            return new ResponseEntity<>("Event updated successfully", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-event/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id){
        boolean removedEvent = eventService.deleteEvent(id);
        if(removedEvent)
            return new ResponseEntity<>("Event Deleted Successfully", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    
    
}
