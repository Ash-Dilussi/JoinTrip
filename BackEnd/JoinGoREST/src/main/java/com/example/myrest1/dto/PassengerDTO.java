package com.example.myrest1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PassengerDTO {
    
    private String aname;
    private String id;
    private String destination;
    private String pickup;

}
