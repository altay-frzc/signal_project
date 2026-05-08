package com.alerts;

import java.util.List;
import java.util.stream.Collectors;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
                long now = System.currentTimeMillis();
        long tenMinutesAgo = now - 10 * 60 * 1000;

        List<PatientRecord> systolicRecords = dataStorage.getRecords(
            patient.getPatientId(), 0, now);
        List<PatientRecord> diastolicRecords = dataStorage.getRecords(
            patient.getPatientId(), 0, now);
        List<PatientRecord> saturationRecords = dataStorage.getRecords(
            patient.getPatientId(), tenMinutesAgo, now);
        List<PatientRecord> ecgRecords = dataStorage.getRecords(
            patient.getPatientId(), 0, now);

        checkBloodPressure(patient, systolicRecords);
        checkSaturation(patient, saturationRecords);
        checkHypotensiveHypoxemia(patient, systolicRecords, saturationRecords);
        checkECG(patient, ecgRecords);
        checkTriggeredAlert(patient, dataStorage.getRecords(patient.getPatientId(), 0, now));

    }
    private void checkBloodPressure(Patient patient, List<PatientRecord> records) {
        List<PatientRecord> systolic = records.stream()
            .filter(r -> r.getRecordType().equals("SystolicPressure"))
            .collect(Collectors.toList());

        List<PatientRecord> diastolic = records.stream()
            .filter(r -> r.getRecordType().equals("DiastolicPressure"))
            .collect(Collectors.toList());

        checkTrend(patient, systolic, "SystolicPressure");
        checkTrend(patient, diastolic, "DiastolicPressure");

        for (PatientRecord record : systolic) {
            if (record.getMeasurementValue() > 180) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Systolic High", record.getTimestamp()));
            } else if (record.getMeasurementValue() < 90) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Systolic Low", record.getTimestamp()));
            }
        }

        for (PatientRecord record : diastolic) {
            if (record.getMeasurementValue() > 120) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Diastolic High", record.getTimestamp()));
            } else if (record.getMeasurementValue() < 60) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Diastolic Low", record.getTimestamp()));
            }
        }
    }

    private void checkTrend(Patient patient, List<PatientRecord> records, String type) {
        if (records.size() < 3) return;
        for (int i = 2; i < records.size(); i++) {
            double diff1 = records.get(i-1).getMeasurementValue() - records.get(i-2).getMeasurementValue();
            double diff2 = records.get(i).getMeasurementValue() - records.get(i-1).getMeasurementValue();
            if (diff1 > 10 && diff2 > 10) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), type + " Increasing Trend", records.get(i).getTimestamp()));
            } else if (diff1 < -10 && diff2 < -10) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), type + " Decreasing Trend", records.get(i).getTimestamp()));
            }
        }
    }

    private void checkSaturation(Patient patient, List<PatientRecord> records) {
        List<PatientRecord> satRecords = records.stream()
            .filter(r -> r.getRecordType().equals("Saturation"))
            .collect(Collectors.toList());

        for (int i = 0; i < satRecords.size(); i++) {
            double value = satRecords.get(i).getMeasurementValue();
            if (value < 92) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Low Saturation", satRecords.get(i).getTimestamp()));
            }
            if (i > 0 && satRecords.get(i-1).getMeasurementValue() - value >= 5) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Rapid Saturation Drop", satRecords.get(i).getTimestamp()));
            }
        }
    }

    private void checkHypotensiveHypoxemia(Patient patient, List<PatientRecord> allRecords, List<PatientRecord> recentRecords) {
        boolean lowSystolic = allRecords.stream()
            .filter(r -> r.getRecordType().equals("SystolicPressure"))
            .anyMatch(r -> r.getMeasurementValue() < 90);

        boolean lowSaturation = recentRecords.stream()
            .filter(r -> r.getRecordType().equals("Saturation"))
            .anyMatch(r -> r.getMeasurementValue() < 92);

        if (lowSystolic && lowSaturation) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia", System.currentTimeMillis()));
        }
    }

    private void checkECG(Patient patient, List<PatientRecord> records) {
        List<PatientRecord> ecgRecords = records.stream()
            .filter(r -> r.getRecordType().equals("ECG"))
            .collect(Collectors.toList());

        int windowSize = 10;
        for (int i = windowSize; i < ecgRecords.size(); i++) {
            double sum = 0;
            for (int j = i - windowSize; j < i; j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double average = sum / windowSize;
            if (ecgRecords.get(i).getMeasurementValue() > average * 1.5) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "ECG Abnormal Peak", ecgRecords.get(i).getTimestamp()));
            }
        }
    }

    private void checkTriggeredAlert(Patient patient, List<PatientRecord> records) {
    records.stream()
        .filter(r -> r.getRecordType().equals("Alert") && r.getMeasurementValue() == 1.0)
        .forEach(r -> triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Manual Alert Triggered", r.getTimestamp())));
    }
    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT - Patient: " + alert.getPatientId()
            + " | Condition: " + alert.getCondition()
            + " | Time: " + alert.getTimestamp());
    }
}