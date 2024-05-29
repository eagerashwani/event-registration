package com.thinkify.event.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private String name;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long organizerId;
}
