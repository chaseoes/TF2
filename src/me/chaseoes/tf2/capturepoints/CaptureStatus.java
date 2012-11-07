package me.chaseoes.tf2.capturepoints;

public class CaptureStatus {
    
    public static CaptureStatus CAPTURED = new CaptureStatus("captured");
    public static CaptureStatus UNCAPTURED = new CaptureStatus("uncaptured");
    public static CaptureStatus CAPTURING = new CaptureStatus("capturing");

    String status;
    
    public CaptureStatus(String s) {
        status = s;
    }

    public String string() {
        return status;
    }
}
