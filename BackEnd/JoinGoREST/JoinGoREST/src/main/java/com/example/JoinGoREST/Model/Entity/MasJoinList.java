package com.example.JoinGoREST.Model.Entity;


import java.util.List;

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
@Table(name="TBL_Masjoinlist")
public class MasJoinList {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer Id;
	String askingReqid;
	List<String> joinlistReqid;

}