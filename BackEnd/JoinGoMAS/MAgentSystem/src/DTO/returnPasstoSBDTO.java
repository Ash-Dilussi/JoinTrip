package DTO;
import java.util.ArrayList;
import java.util.List;

public class returnPasstoSBDTO {


	private JoinRequestDTO currentPassenger;

	private List<JoinRequestDTO> joinPassengerList = new ArrayList<>();


	public List<JoinRequestDTO> getJoinPassengerList() {
		return joinPassengerList;
	}

	public void setJoinPassengerList(List<JoinRequestDTO> joinPassengerList) {
		this.joinPassengerList = joinPassengerList;
	}

	public JoinRequestDTO getCurrentPassenger() {
		return currentPassenger;
	}

	public void setCurrentPassenger(JoinRequestDTO currentPassenger) {
		this.currentPassenger = currentPassenger;
	}
}
