package com.hydration.exception;

public class WaterIntakeNotFoundException extends RuntimeException {
    public WaterIntakeNotFoundException() {
        super("Water Intake Not Found");
    }
}
