package com.cardio_generator;

import com.data_management.DataStorage;

/**
 * Entry point that allows running either the HealthDataSimulator or DataStorage
 * from the command line depending on the argument passed.
 */
public class Main {

    /**
     * Runs either DataStorage or HealthDataSimulator based on the first argument.
     * If no argument is given, runs HealthDataSimulator by default.
     *
     * @param args command line arguments, pass "DataStorage" to run DataStorage
     * @throws Exception if something goes wrong during execution
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.main(new String[]{});
        } else {
            HealthDataSimulator.main(args);
        }
    }
}
