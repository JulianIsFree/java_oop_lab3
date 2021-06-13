package nsu.oop.explorer.frontend.visual.fogFactory;

import com.badlogic.gdx.graphics.Pixmap;
import nsu.oop.explorer.backend.model.noise.DiscreteDistribution;
import nsu.oop.explorer.backend.model.noise.Noise2D;
import nsu.oop.explorer.backend.model.noise.PerlinNoiseFast;
import nsu.oop.explorer.frontend.visual.fogFactory.FogFactory.FactoriesCounter;

public class FogGenerator extends Thread {
    private int widthOffset;
    private int width;
    private int dwidth;
    private int height;

    private final DiscreteDistribution distribution;
    private final double[][] heightMap;
    private final Pixmap paper;

    private FactoriesCounter counter;
    public FogGenerator(int widthOffset, int width, int dwidth, int height, DiscreteDistribution distribution,
                        double[][] heightMap, Pixmap paper, FactoriesCounter counter) {
        this.widthOffset = widthOffset;
        this.width = width;
        this.dwidth = dwidth;
        this.height = height;
        this.distribution = distribution;

        this.heightMap = heightMap;
        this.paper = paper;

        this.counter = counter;
    }

    @Override
    public void run() {
        counter.increment();
        Noise2D noiseGenerator = new PerlinNoiseFast((int)System.currentTimeMillis());

        for (int i = widthOffset; i < widthOffset + dwidth; ++i) {
            for (int j = 0; j < height; ++j) {
                double noise = noiseGenerator.multiNoise((double)i/width, (double)j/height, 1, 0.25d);
                noise = Math.abs(noise);
                heightMap[i][j] = noise;
                synchronized (distribution) {
                    distribution.countExperiment(noise);
                }
            }
        }

        double length = distribution.maxValue() - distribution.minValue();
        for (int i = widthOffset; i < widthOffset + dwidth; ++i) {
            for (int j = 0; j < height; ++j) {
                int color = 0x0F000000 + (int)(0x00000fF0 * heightMap[i][j] / length);
                paper.drawPixel(i, j, color);
            }
        }

        counter.decrement();
    }
}
