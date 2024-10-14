package com.example.JoinGoREST.Model.Entity;


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
@Table(name="TBL_Passenger")
public class Passenger {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
	generator= "joinReqId_generator"		)
	@SequenceGenerator(name = "joinReqId_generator",
	sequenceName= "joinreq_Sequence",
	allocationSize=1 )
	private Integer Id;
	private String Userid;
	private String Username;
	private String Addressline1;
	private String Addressline2;
	private String Town;
	private String Email;
	private String nic;
	private Integer Phone;






}