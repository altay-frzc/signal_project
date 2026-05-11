package data_management;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.AlertStrategy;
import com.alerts.BloodPressureStrategy;
import com.alerts.HeartRateStrategy;
import com.alerts.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class AlertStrategyTest {

    private DataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
    }

    @Test
    void testBloodPressureStrategyHighSystolic() {
        storage.addPatientData(1, 185.0, "SystolicPressure", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        AlertStrategy strategy = new BloodPressureStrategy();
        assertDoesNotThrow(() -> strategy.checkAlert(patient, storage));
    }

    @Test
    void testBloodPressureStrategyLowSystolic() {
        storage.addPatientData(1, 85.0, "SystolicPressure", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        AlertStrategy strategy = new BloodPressureStrategy();
        assertDoesNotThrow(() -> strategy.checkAlert(patient, storage));
    }

    @Test
    void testHeartRateStrategyHigh() {
        storage.addPatientData(1, 110.0, "ECG", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        AlertStrategy strategy = new HeartRateStrategy();
        assertDoesNotThrow(() -> strategy.checkAlert(patient, storage));
    }

    @Test
    void testHeartRateStrategyLow() {
        storage.addPatientData(1, 40.0, "ECG", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        AlertStrategy strategy = new HeartRateStrategy();
        assertDoesNotThrow(() -> strategy.checkAlert(patient, storage));
    }

    @Test
    void testOxygenSaturationStrategyLow() {
        storage.addPatientData(1, 90.0, "Saturation", System.currentTimeMillis());
        Patient patient = storage.getAllPatients().get(0);
        AlertStrategy strategy = new OxygenSaturationStrategy();
        assertDoesNotThrow(() -> strategy.checkAlert(patient, storage));
    }

    @Test
    void testOxygenSaturationStrategyRapidDrop() {
        long now = System.currentTimeMillis();
        storage.addPatientData(1, 98.0, "Saturation", now - 5000);
        storage.addPatientData(1, 92.0, "Saturation", now);
        Patient patient = storage.getAllPatients().get(0);
        AlertStrategy strategy = new OxygenSaturationStrategy();
        assertDoesNotThrow(() -> strategy.checkAlert(patient, storage));
    }

    @Test
    void testNormalDataNoAlert() {
        storage.addPatientData(1, 120.0, "SystolicPressure", 1000L);
        storage.addPatientData(1, 98.0, "Saturation", 1000L);
        storage.addPatientData(1, 75.0, "ECG", 1000L);
        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> new BloodPressureStrategy().checkAlert(patient, storage));
        assertDoesNotThrow(() -> new OxygenSaturationStrategy().checkAlert(patient, storage));
        assertDoesNotThrow(() -> new HeartRateStrategy().checkAlert(patient, storage));
    }
}