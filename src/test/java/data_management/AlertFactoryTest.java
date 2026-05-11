package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.Alert;
import com.alerts.AlertFactory;
import com.alerts.BloodPressureAlertFactory;
import com.alerts.BloodOxygenAlertFactory;
import com.alerts.ECGAlertFactory;

public class AlertFactoryTest {

    @Test
    void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "High Systolic", 1000L);
        assertEquals("1", alert.getPatientId());
        assertTrue(alert.getCondition().contains("BloodPressure"));
        assertEquals(1000L, alert.getTimestamp());
    }

    @Test
    void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("1", "Low Saturation", 2000L);
        assertEquals("1", alert.getPatientId());
        assertTrue(alert.getCondition().contains("BloodOxygen"));
        assertEquals(2000L, alert.getTimestamp());
    }

    @Test
    void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("1", "Abnormal Peak", 3000L);
        assertEquals("1", alert.getPatientId());
        assertTrue(alert.getCondition().contains("ECG"));
        assertEquals(3000L, alert.getTimestamp());
    }

    @Test
    void testFactoriesReturnDifferentConditions() {
        AlertFactory bpFactory = new BloodPressureAlertFactory();
        AlertFactory boFactory = new BloodOxygenAlertFactory();
        AlertFactory ecgFactory = new ECGAlertFactory();

        Alert bpAlert = bpFactory.createAlert("1", "test", 1000L);
        Alert boAlert = boFactory.createAlert("1", "test", 1000L);
        Alert ecgAlert = ecgFactory.createAlert("1", "test", 1000L);

        assertNotEquals(bpAlert.getCondition(), boAlert.getCondition());
        assertNotEquals(bpAlert.getCondition(), ecgAlert.getCondition());
        assertNotEquals(boAlert.getCondition(), ecgAlert.getCondition());
    }
}