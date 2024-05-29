package com.thinkify.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinkify.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>{
    
}
