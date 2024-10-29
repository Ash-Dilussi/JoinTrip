package DTO;

import jade.util.leap.Serializable;

public class longrouteSegmentDTO implements Serializable {
	private String tripReqId;
	private int segNo; 
    private Coordinate start;
    private Coordinate end;
    

    public longrouteSegmentDTO(String tripid, int segno, Coordinate start, Coordinate end) {
    	this.setTripReqId(tripid);
    	this.segNo = segno;
        this.start = start;
        this.end = end;
    }

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

	public int getSegNo() {
		return segNo;
	}

	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}

	public String getTripReqId() {
		return tripReqId;
	}

	public void setTripReqId(String tripReqId) {
		this.tripReqId = tripReqId;
	}
}