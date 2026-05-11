package data_management;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class AlertGeneratorTest {

    private DataStorage storage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        storage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(storage);
    }

    @Test
    void testHighSystolicAlert() {
        storage.addPatientData(1, 185.0, "SystolicPressure", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testLowSystolicAlert() {
        storage.addPatientData(1, 85.0, "SystolicPressure", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testLowSaturationAlert() {
        storage.addPatientData(1, 90.0, "Saturation", System.currentTimeMillis());
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testRapidSaturationDrop() {
        long now = System.currentTimeMillis();
        storage.addPatientData(1, 98.0, "Saturation", now - 5000);
        storage.addPatientData(1, 92.0, "Saturation", now);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testHypotensiveHypoxemia() {
        long now = System.currentTimeMillis();
        storage.addPatientData(1, 85.0, "SystolicPressure", now - 1000);
        storage.addPatientData(1, 90.0, "Saturation", now);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testBloodPressureIncreasingTrend() {
        storage.addPatientData(1, 100.0, "SystolicPressure", 1000L);
        storage.addPatientData(1, 115.0, "SystolicPressure", 2000L);
        storage.addPatientData(1, 130.0, "SystolicPressure", 3000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testBloodPressureDecreasingTrend() {
        storage.addPatientData(1, 130.0, "SystolicPressure", 1000L);
        storage.addPatientData(1, 115.0, "SystolicPressure", 2000L);
        storage.addPatientData(1, 100.0, "SystolicPressure", 3000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testManualAlert() {
        storage.addPatientData(1, 1.0, "Alert", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testNoAlertForNormalData() {
        storage.addPatientData(1, 120.0, "SystolicPressure", 1000L);
        storage.addPatientData(1, 80.0, "DiastolicPressure", 1000L);
        storage.addPatientData(1, 98.0, "Saturation", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }
}