import java.util.ArrayList;
import java.util.List;

import jade.util.leap.Serializable;

public class returnpasDTO implements Serializable {


	private String pasId;

	private List<passengerDTO> joinPassengerList = new ArrayList<>();

	public String getPasId() {
		return pasId;
	}

	public void setPasId(String pasId) {
		this.pasId = pasId;
	}

	public List<passengerDTO> getJoinPassengerList() {
		return joinPassengerList;
	}

	public void setJoinPassengerList(List<passengerDTO> joinPassengerList) {
		this.joinPassengerList = joinPassengerList;
	}
}
