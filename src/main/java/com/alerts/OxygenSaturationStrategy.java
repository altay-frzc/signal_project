package com.alerts;

import java.util.List;
import java.util.stream.Collectors;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy that checks oxygen saturation for critical drops.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    private AlertFactory factory = new BloodOxygenAlertFactory();

    /**
     * Checks saturation records for low values and rapid drops.
     * Triggers alert if saturation is below 92% or drops 5% or more.
     *
     * @param patient the patient to check
     * @param dataStorage storage to get records from
     */
    @Override
    public void checkAlert(Patient patient, DataStorage dataStorage) {
        List<PatientRecord> records = dataStorage.getRecords(
            patient.getPatientId(), 0, System.currentTimeMillis());

        List<PatientRecord> satRecords = records.stream()
            .filter(r -> r.getRecordType().equals("Saturation"))
            .collect(Collectors.toList());

        for (int i = 0; i < satRecords.size(); i++) {
            double value = satRecords.get(i).getMeasurementValue();
            if (value < 92) {
                Alert alert = factory.createAlert(
                    String.valueOf(patient.getPatientId()), "Low Saturation", satRecords.get(i).getTimestamp());
                System.out.println("ALERT: " + alert.getCondition());
            }
            if (i > 0 && satRecords.get(i - 1).getMeasurementValue() - value >= 5) {
                Alert alert = factory.createAlert(
                    String.valueOf(patient.getPatientId()), "Rapid Saturation Drop", satRecords.get(i).getTimestamp());
                System.out.println("ALERT: " + alert.getCondition());
            }
        }
    }
}