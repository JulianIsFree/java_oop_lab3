package nsu.oop.explorer.backend.model.noise;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class represents discrete distribution of random value
 */
public class DiscreteDistribution {
    private HashMap<Double, Integer> elementaryOutcomes;
    private int numberOfExperiments;

    private boolean areExperiments;

    private double minValue;
    private double maxValue;

    private boolean isUpdated;
    private double expectedValue;

    public DiscreteDistribution() {
        elementaryOutcomes = new HashMap<>();
        numberOfExperiments = 0;
        minValue = 0;
        maxValue = 0;
        areExperiments = false;

        isUpdated = false;
        expectedValue = 0;
    }

    public void countExperiment(double val) {
        if (!areExperiments) {
            areExperiments = true;
            minValue = val;
            maxValue = val;
        }

        isUpdated = true;

        if (val > maxValue)
            maxValue = val;

        if (val < minValue)
            minValue = val;

        numberOfExperiments++;
        if (elementaryOutcomes.containsKey(val))
            elementaryOutcomes.put(val, elementaryOutcomes.get(val) + 1);
        else
            elementaryOutcomes.put(val, 1);
    }

    public double expectedValue() {
        if (isUpdated) {
            isUpdated = false;
            double expectedValue = 0;

            for (Map.Entry<Double, Integer> e : elementaryOutcomes.entrySet()) {
                expectedValue += e.getKey() * e.getValue();
            }

            this.expectedValue = expectedValue / numberOfExperiments;
        }
        return this.expectedValue;
    }

    public void reset() {
        elementaryOutcomes = new HashMap<>();
        numberOfExperiments = 0;
        minValue = 0;
        maxValue = 0;
        areExperiments = false;

        isUpdated = false;
        expectedValue = 0;
    }

    public double minValue() {
        return minValue;
    }

    public double maxValue() {
        return maxValue;
    }

    public int getNumberOfExperiments() {
        return numberOfExperiments;
    }

    public Map<Double, Integer> getElementaryOutcomes() {
        return Collections.unmodifiableMap(elementaryOutcomes);
    }
}