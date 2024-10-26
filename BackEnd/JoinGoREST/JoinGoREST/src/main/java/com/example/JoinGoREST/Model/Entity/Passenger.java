package com.example.JoinGoREST.Model.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@NamedNativeQuery(name = "Passenger.getResponsePassforreqId", query = "SELECT t1.userid , t1.username, t1.phone, t1.town  FROM tbl_passenger t1 INNER JOIN tbl_join_request t2 ON t1.userid = t2.userid WHERE t2.joinreqid = :joinreqId",
//resultSetMapping = "Mapping.ResponsePassengerDTO")
//@SqlResultSetMapping(
//	       name="Mapping.ResponsePassengerDTO",
//	       classes={
//	          @ConstructorResult(
//	               targetClass=ResponsePassengerDTO.class,
//	                 columns={
//	                    @ColumnResult(name="id"),
//	                    @ColumnResult(name="name"),
//	                    @ColumnResult(name="orderCount"),
//	                    @ColumnResult(name="avgOrder", type=Double.class)
//	                    }
//	          )
//	       }
//	      )
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TBL_Passenger")
public class Passenger {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Integer Id;
	private String Userid;
	private Integer userType;
	private String FirstName;
	private String LastName;
	private String Addressline1;
	private String Addressline2;
	private String Town;
	private String Email="";
	private char Gender;

 
	@Column(nullable = false)
	private String nic;

	@Column(nullable = false)
	private String Phone;





}


