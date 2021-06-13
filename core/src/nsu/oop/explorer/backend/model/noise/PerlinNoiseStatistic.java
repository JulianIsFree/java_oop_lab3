package nsu.oop.explorer.backend.model.noise;

class PerlinNoiseStatistic {
    final double expectedValue;
    final double minValue;
    final double maxValue;
    final long time;
    final int seed;

    PerlinNoiseStatistic(int seed, int scale, int width, int height, boolean absoluteValue) {
        this(seed, scale, width, height, absoluteValue, 1, 1);
    }

    PerlinNoiseStatistic(int seed, int scale, int width, int height, boolean absoluteValue, int octaves, double persistence) {
        PerlinNoise perlinNoise = new PerlinNoise(seed);

        DiscreteDistribution noiseFunctionResults = new DiscreteDistribution();
        double maxValue = -2;
        double minValue = 2;

        long begin = System.currentTimeMillis();
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                double x = (double)i / width;
                double y = (double)j / height;

                double noise = perlinNoise.multiNoise(x, y, scale, octaves, persistence);
                if (absoluteValue)
                    noise = Math.abs(noise);
                noise = Math.floor(noise);
                noiseFunctionResults.countExperiment(noise);

                if (noise > maxValue)
                    maxValue = noise;

                if (noise < minValue)
                    minValue = noise;

            }
        }
        long end = System.currentTimeMillis();

        this.expectedValue = noiseFunctionResults.expectedValue();
        this.time = end - begin;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.seed = seed;
    }
}