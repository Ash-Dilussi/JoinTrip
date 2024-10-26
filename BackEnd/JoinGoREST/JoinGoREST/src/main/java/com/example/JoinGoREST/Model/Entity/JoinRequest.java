package com.example.JoinGoREST.Model.Entity;



import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TBL_Join_Request")
public class JoinRequest {


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
	generator= "joinReqId_generator"		)
	@SequenceGenerator(name = "joinReqId_generator",
	sequenceName= "joinreq_Sequence",
	allocationSize=1 )
	private String Id;
	private String userid; 
	private String joinreqid;
	private String desplace_id;
	private float startLon =0;
	private float startLat =0;
	private float destLon =0;
	private float destLat =0; 
	
	private int requestStatus;
	private int reqVehicletype=0;

	private Date scheduleTime;
	private int tripType=0;
	private int SegmentDistance=0; 



}
