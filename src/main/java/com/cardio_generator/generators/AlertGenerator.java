package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated alert data for patients in the health monitoring system.
 * Alerts are triggered probabilistically using an exponential distribution,
 * and once triggered, have a 90% chance of being resolved in each subsequent period.
 */

public class AlertGenerator implements PatientDataGenerator {

    public static final Random RANDOM_GENERATOR = new Random(); //I change from randomGenerator to RANDOM_GENERATOR to follow Java naming conventions
    private boolean[] alertStates; // false = resolved, true = pressed //I change from AlertStates to alertStates to follow Java naming conventions


    /**
     * Constructs an AlertGenerator and initializes alert states for all patients.
     * All alerts start in the resolved (false) state.
     *
     * @param patientCount the total number of patients to track alert states for
     */

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

       /**
     * Generates and outputs an alert event for the specified patient.
     * If the patient currently has an active alert, there is a 90% chance it
     * will be resolved. Otherwise, a new alert may be triggered based on a
     * Poisson process with rate lambda = 0.1.
     *
     * @param patientId      the unique identifier of the patient
     * @param outputStrategy the strategy used to output the generated alert data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency //I change from Lambda to lambda to follow Java naming conventions
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
