package com.alerts;

/**
 * Factory that creates blood pressure related alerts.
 */
public class BloodPressureAlertFactory extends AlertFactory {

    /**
     * Creates a blood pressure alert for the given patient.
     *
     * @param patientId the id of the patient
     * @param condition the blood pressure condition detected
     * @param timestamp when the alert was triggered
     * @return a new Alert for blood pressure
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "BloodPressure: " + condition, timestamp);
    }
}