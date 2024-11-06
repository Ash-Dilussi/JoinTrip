package com.example.JoinGoREST.Model.Entity;

 
import com.example.JoinGoREST.Model.DTO.Coordinate;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TBL_Farroute_Segment")
public class LongDistanceSegment{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Integer Id;
	private String tripReqId;
	private int segNo;
	
	   @Embedded
	    @AttributeOverrides({
	        @AttributeOverride(name = "latitude", column = @Column(name = "start_lat")),
	        @AttributeOverride(name = "longitude", column = @Column(name = "start_lng"))
	    })
	  private Coordinate start;
	   
	   @Embedded
	    @AttributeOverrides({
	        @AttributeOverride(name = "latitude", column = @Column(name = "end_lat")),
	        @AttributeOverride(name = "longitude", column = @Column(name = "end_lng"))
	    })
	    private Coordinate end;
	
}