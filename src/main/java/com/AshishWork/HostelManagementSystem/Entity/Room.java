package com.AshishWork.HostelManagementSystem.Entity;

import com.AshishWork.HostelManagementSystem.Enum.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", unique = true, nullable = false, length = 10)
    private String roomNumber;

    @Column(name = "room_type", nullable = false, length = 20)
    private String roomType; // SINGLE, DOUBLE, DORMITORY

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "current_occupancy")
    private Integer currentOccupancy = 0;

    @Column(name = "price_per_month", nullable = false)
    private Double pricePerMonth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;
}
