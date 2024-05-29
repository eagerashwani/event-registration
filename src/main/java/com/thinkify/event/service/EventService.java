package com.thinkify.event.service;

import java.util.List;

import com.thinkify.event.dto.EventDto;
import com.thinkify.event.entity.Event;

public interface EventService {
    public String createEvent(EventDto event);
    public List<Event> getAllEvents();
    public Event getEvent(Long id);
    public boolean updateEvent(EventDto event, Long id);
    public boolean deleteEvent(Long id);
}
