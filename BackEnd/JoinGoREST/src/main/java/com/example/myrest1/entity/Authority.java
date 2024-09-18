package com.example.myrest1.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class Authority {
    
    
    private String aname;
    private String aclearance;
    private int emps;
    @Id
    private int aid;

}
