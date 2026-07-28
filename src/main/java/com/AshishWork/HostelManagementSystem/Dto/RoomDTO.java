package com.AshishWork.HostelManagementSystem.Dto;

import lombok.Builder;
import lombok.Data;

@Data

public class RoomDTO {

    private Long id;
    private String roomNumber;
    private String roomType;
    private Integer capacity;
    private Integer currentOccupancy;
    private Double pricePerMonth;
    private String status;
}

