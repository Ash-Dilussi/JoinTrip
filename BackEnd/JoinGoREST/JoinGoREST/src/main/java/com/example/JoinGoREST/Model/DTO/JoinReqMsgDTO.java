package com.example.JoinGoREST.Model.DTO;

import com.example.JoinGoREST.Model.Enum.JoinReqStatus;

public class JoinReqMsgDTO{
	
	
	public String senderUserId;
	public String senderJoinReqId;
	public String receiverUserId;
	public String reveiverJoinReqId;
	
	public JoinReqStatus reqStatus;
	
}