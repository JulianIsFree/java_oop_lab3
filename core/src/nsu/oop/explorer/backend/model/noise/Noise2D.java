package nsu.oop.explorer.backend.model.noise;

public abstract class Noise2D {
    protected final int seed;

    protected Noise2D(int seed) {
        this.seed = seed;
    }


    public abstract double noise(double x, double y, int scale);

    public double noise(double x, double y) {
        return noise(x, y, 1);
    }

    public double multiNoise(double x, double y, int octaves, double persistence) {
        return multiNoise(x, y, 1, octaves, persistence);
    }

    public double multiNoise(double x, double y, int scale, int octaves, double persistence) {
        double amplitude = 1; // сила применения шума к общей картине, будет уменьшаться с "мельчанием" шума
        // как сильно уменьшаться - регулирует persistence
        double max = 0; // необходимо для нормализации результата
        double result = 0; // накопитель результата

        while (octaves-- > 0) {
            max += amplitude;
            result += noise(x, y, scale) * amplitude;
            amplitude *= persistence;
            x *= 2; // удваиваем частоту шума (делаем его более мелким) с каждой октавой
            y *= 2;
        }

        return result / max;
    }
}
