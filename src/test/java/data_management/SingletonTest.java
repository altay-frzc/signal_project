package data_management;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

public class SingletonTest {

    @Test
    public void testDataStorageReturnsSameInstance() {
        DataStorage first = DataStorage.getInstance();
        DataStorage second = DataStorage.getInstance();
        assertSame(first, second);
    }

    @Test
    public void testDataStorageNotNull() {
        assertNotNull(DataStorage.getInstance());
    }

    @Test
    public void testHealthDataSimulatorReturnsSameInstance() {
        HealthDataSimulator first = HealthDataSimulator.getInstance();
        HealthDataSimulator second = HealthDataSimulator.getInstance();
        assertSame(first, second);
    }

    @Test
    public void testHealthDataSimulatorNotNull() {
        assertNotNull(HealthDataSimulator.getInstance());
    }
}
