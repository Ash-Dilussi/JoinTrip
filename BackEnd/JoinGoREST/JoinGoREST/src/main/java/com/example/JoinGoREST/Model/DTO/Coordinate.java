package com.example.JoinGoREST.Model.DTO;

 
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable 
 public class Coordinate {
    public double latitude;
    public double longitude;
    }
