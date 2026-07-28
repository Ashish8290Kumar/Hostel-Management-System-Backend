//package com.AshishWork.HostelManagementSystem.Dto;
//
//import lombok.Data;
//
//@Data
//public class StudentDTO {
//
//    private Long id;
//    private String rollNumber;
//    private String feeStatus;
//    private String roomNumber;
//    private UserDTO userDetails;
//
//}

package com.AshishWork.HostelManagementSystem.Dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentDTO {

    private Long id;
    private String rollNumber;
    private String feeStatus;
    private String roomNumber;
    private UserDTO userDetails;


    private String roomType;
    private Double pricePerMonth;
    private Integer capacity;


    private List<?> installments;
}

