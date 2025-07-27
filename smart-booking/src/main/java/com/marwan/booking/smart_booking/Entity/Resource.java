package com.marwan.booking.smart_booking.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "resources")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type; // e.g., "Car", "Room", "Doctor"

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;// BUSINESS user who owns it

    private LocalTime availableFrom;
    private LocalTime availableTo;

    private String imagePath;


}
