package com.example.JoinGoREST.Model.DTO;

//import com.example.JoinGoREST.Model.Entity.Passenger;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class JoinPassengerInfo{
//	static JoinPassengerInfo from(Passenger passenger) {
//	return new JoinPassengerInfo(passenger.getUserid(),passenger.getUsername(),passenger.getPhone(),passenger.getTown());
//}
//	
//	String UserId;
//	String Username;
//	Integer Phone;
//	String Town;
//	
//}

public interface JoinPassengerInfo{
	String getUserId();
	String getUsername();
	Integer getPhone();
	String getTown();
}