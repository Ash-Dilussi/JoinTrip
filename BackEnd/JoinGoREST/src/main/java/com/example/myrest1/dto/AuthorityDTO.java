package com.example.myrest1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityDTO {
    
    
    private String aname;
    private String aclearance;
    private int emps;
    private int aid;
    
}
