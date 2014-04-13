package com.chaseoes.tf2.capturepoints;

public enum CaptureStatus {

    CAPTURED("captured"), UNCAPTURED("uncaptured"), CAPTURING("capturing");

    private String status;

    private CaptureStatus(String s) {
        status = s;
    }

    public String string() {
        return status;
    }

}
