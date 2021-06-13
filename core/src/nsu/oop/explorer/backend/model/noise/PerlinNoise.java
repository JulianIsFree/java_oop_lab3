package nsu.oop.explorer.backend.model.noise;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PerlinNoise extends Noise2D {
    /*
    For absolute value of perlin noise with seed from [0, 255] scale 100 is just good in terms
    of expected values distribution
     */
    private static int niceScale = 100;

    class Vector2 {
        double x;
        double y;

        Vector2(double x, double y) {
            this(x, y, false);
        }

        double norm() {
            return Math.sqrt(x*x + y*y);
        }

        Vector2(double x, double y, boolean doNormalize) {
            double norm = doNormalize ? norm() : 1d;

            if (norm == 0)
                norm = 1d;

            this.x = x / norm;
            this.y = y / norm;
        }
    }

    public PerlinNoise(int seed) {
        super(seed);
    }

    @Override
    public double noise(double x, double y, int scale) {
        return perlin(x, y, scale);
    }

    @Override
    public double noise(double x, double y) {
        return perlin(x, y, niceScale);
    }

    @Override
    public double multiNoise(double x, double y, int octaves, double persistence) {
        return super.multiNoise(x, y, niceScale, octaves, persistence);
    }

    private double perlin(double x, double y, int scale) {
        // Determine grid cell coordinate
        int x0 = (int)Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int)Math.floor(y);
        int y1 = y0 + 1;

        // Determine interpolation weights
        // Could also use higher order polynomial
        double sx = x - x0;
        double sy = y - y0;

        // Interpolate between grid point gradients
        double n0, n1, ix0, ix1;

        n0 = dotGridGradient(x0, y0, x, y);
        n1 = dotGridGradient(x1, y0, x, y);
        ix0 = interpolate(n0, n1, fade(sx));

        n0 = dotGridGradient(x0, y1, x, y);
        n1 = dotGridGradient(x1, y1, x, y);
        ix1 = interpolate(n0, n1, fade(sx));

        return interpolate(ix0, ix1, sy) * scale;
    }

    protected Vector2 randomGradient(int ix, int iy) {
        // 2D to 1D
        int n = ix + iy * 11111 + seed;

        // Hugo Elias hash ; expected value is -0.4874116824939847 for seeds [0, 1023] for map 1024x1024
        n = (n<<13)^n;
        n = (n*(n*n*15731+789221)+1376312589)>>16;

        return new Vector2(cos(n),sin(n));
    }

    private double dotGridGradient(int ix, int iy, double x, double y) {
        // Get gradient from integer coordinates
        Vector2 gradient = randomGradient(ix, iy);

        // Compute the distance vector
        double dx = x - ix;
        double dy = y - iy;

        // Compute the dot-product
        return (dx*gradient.x + dy*gradient.y);
    }

    private double interpolate(double a0, double a1, double x) {
        return a0 + (a1 - a0) * x;
    }
    // for t from [0, 1] it gives a smooth output between [0, 1]

    private double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }


    public static void main(String[] args) {

        DiscreteDistribution distributionOfExpectedValue = new DiscreteDistribution();
        DiscreteDistribution distributionOfMaxValue = new DiscreteDistribution();
        DiscreteDistribution distributionOfMinValue = new DiscreteDistribution();

        int sample = 256;
        int mul = 100;
        for (int i = 0; i < sample; ++i) {
            PerlinNoiseStatistic statistic = new PerlinNoiseStatistic(i, mul, 256, 256, true, 4, 0.25d);
            distributionOfExpectedValue.countExperiment(statistic.expectedValue);
            distributionOfMaxValue.countExperiment(statistic.maxValue);
            distributionOfMinValue.countExperiment(statistic.minValue);
        }

        System.out.println("Expected value of noise with mul=" + mul + ":   " + distributionOfExpectedValue.expectedValue());
        System.out.println("Max value of noise with mul=" + mul + ":        " + distributionOfMaxValue.expectedValue());
        System.out.println("Min value of noise with mul=" + mul + ":        " + distributionOfMinValue.expectedValue());

        try {
            FileWriter fw = new FileWriter("stat.txt");
            Set<Map.Entry<Double, Integer>> set = distributionOfExpectedValue.getElementaryOutcomes().entrySet();
            for (Map.Entry<Double, Integer> e : set) {
                fw.write(e.getKey() + " " + e.getValue() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
