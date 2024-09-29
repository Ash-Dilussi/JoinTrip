import jade.util.leap.Serializable;

public class passengerDTO implements Serializable {

    private String pasName;
    private String pasId;
    private double startLon; // Use double for decimal values
    private double startLat; // Use double for decimal values
    private double destLon;  // Use double for decimal values
    private double destLat;

    
    // Getters and setters
    public String getPasName() {
        return pasName;
    }

    public void setPasName(String pasName) {
        this.pasName = pasName;
    }

    public String getPasId() {
        return pasId;
    }

    public void setPasId(String pasId) {
        this.pasId = pasId;
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
}
