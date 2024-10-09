import jade.util.leap.Serializable;

public class JoinRequestDTO implements Serializable {

	//private String pasName;
	private String userId;
	private String joinReqId;
	private double startLon; // Use double for decimal values
	private double startLat; // Use double for decimal values
	private double destLon;  // Use double for decimal values
	private double destLat;
	private String desplace_id;

 

	public String getJoinReqId() {
		return joinReqId;
	}

	public void setJoinReqId(String pasId) {
		this.joinReqId = pasId;
	}

	public double getStartLon() {
		return startLon;
	}

	public void setStartLon(double startLon) {
		this.startLon = startLon;
	}

	public double getStartLat() {
		return startLat;
	}

	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}

	public double getDestLon() {
		return destLon;
	}

	public void setDestLon(double destLon) {
		this.destLon = destLon;
	}

	public double getDestLat() {
		return destLat;
	}

	public void setDestLat(double destLat) {
		this.destLat = destLat;
	}

	public String getDesplace_id() {
		return desplace_id;
	}

	public void setDesplace_id(String desplace_id) {
		this.desplace_id = desplace_id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
