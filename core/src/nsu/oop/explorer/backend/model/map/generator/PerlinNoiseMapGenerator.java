package nsu.oop.explorer.backend.model.map.generator;

import nsu.oop.explorer.backend.model.map.Map;
import nsu.oop.explorer.backend.model.map.cells.*;
import nsu.oop.explorer.backend.model.noise.DiscreteDistribution;
import nsu.oop.explorer.backend.model.noise.PerlinNoise;

public class PerlinNoiseMapGenerator extends MapGenerator {
    @Override
    protected Cell createCellByHeight(int x, int y, double noise, DiscreteDistribution noiseDistribution) {
        double maxValue = noiseDistribution.maxValue();
        double expectedValue = noiseDistribution.expectedValue();

        int z = (int)noise;
        if (noise < expectedValue)
            return new WaterCell(x, y, z);

        double maxRelativeHeight = maxValue - expectedValue;
        double relativeHeight = noise - expectedValue;

        if (relativeHeight/maxRelativeHeight < 0.2)
            return new SandCell(x, y, z);

        if (relativeHeight/maxRelativeHeight < 0.7)
            return new DirtCell(x, y, z);

        if (relativeHeight/maxRelativeHeight < 0.9)
            return new HillCell(x, y, z);

        return new MountainCell(x, y, z);
    }

    @Override
    public Map generateMap(int seed, int width, int height) {
        DiscreteDistribution noiseDistribution = new DiscreteDistribution();
        PerlinNoise noiseGenerator = new PerlinNoise(seed);

        int[][] heightMap = new int[width][height];
        // Creating height map
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                double noise = Math.floor(noiseGenerator.noise(i, j));
                noiseDistribution.countExperiment(noise);
                heightMap[i][j] = (int)noise;
            }
        }

        Cell[][] map = new Cell[width][height];
        // Initialising cells
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                map[i][j] = createCellByHeight(i, j, heightMap[i][j], noiseDistribution);
            }
        }

        return new Map(seed, map);
    }
}
