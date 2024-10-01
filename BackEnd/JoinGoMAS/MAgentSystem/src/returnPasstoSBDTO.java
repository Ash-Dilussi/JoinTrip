import java.util.ArrayList;
import java.util.List;

import jade.util.leap.Serializable;

public class returnPasstoSBDTO implements Serializable {


	private passengerDTO currentPassenger;

	private List<passengerDTO> joinPassengerList = new ArrayList<>();


	public List<passengerDTO> getJoinPassengerList() {
		return joinPassengerList;
	}

	public void setJoinPassengerList(List<passengerDTO> joinPassengerList) {
		this.joinPassengerList = joinPassengerList;
	}

	public passengerDTO getCurrentPassenger() {
		return currentPassenger;
	}

	public void setCurrentPassenger(passengerDTO currentPassenger) {
		this.currentPassenger = currentPassenger;
	}
}
