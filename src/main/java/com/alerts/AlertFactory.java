package com.alerts;

/**
 * Base factory class for creating alerts.
 * Subclasses define which specific alert type gets created.
 */
public abstract class AlertFactory {

    /**
     * Creates an alert for the given patient and condition.
     *
     * @param patientId the id of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp when the alert was triggered
     * @return a new Alert object
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}