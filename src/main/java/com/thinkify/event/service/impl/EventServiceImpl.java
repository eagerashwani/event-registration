package com.thinkify.event.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkify.event.dto.EventDto;
import com.thinkify.event.entity.Event;
import com.thinkify.event.repository.EventRepository;
import com.thinkify.event.service.EventService;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public String createEvent(EventDto eventDto) {
        
        Event event = dtoToEntity(eventDto);
        event.setId(null);
        eventRepository.save(event);
        return "Event Created";
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateEvent(EventDto eventDto, Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isPresent()){
            Event eventEntity = dtoToEntity(eventDto);
            Event e = event.get();
            e.setEndDate(eventEntity.getEndDate());
            e.setLocation(eventEntity.getLocation());
            e.setName(eventEntity.getName());
            e.setOrganizerId(eventEntity.getOrganizerId());
            e.setStartDate(eventEntity.getStartDate());
            eventRepository.save(e);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEvent(Long id) {
        Optional<Event> companyOptional = eventRepository.findById(id);
        if(companyOptional.isPresent()){
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
     private Event dtoToEntity(EventDto eventDto) {
        return mapper.map(eventDto, Event.class);
    }
}
