package nsu.oop.explorer.backend.model.noise;

public class PerlinNoiseFast extends PerlinNoise {

    Vector2 left = new Vector2(-1, 0);
    Vector2 right = new Vector2(1, 0);
    Vector2 down = new Vector2(0, -1);
    Vector2 up = new Vector2(0, 1);

    public PerlinNoiseFast(int seed) {
        super(seed);
    }

    @Override
    protected Vector2 randomGradient(int ix, int iy) {
        int n = ix + 11111*iy + seed;
        n = n & 3;

        switch (n) {
            case 0:
                return right;
            case 1:
                return left;
            case 2:
                return down;
            default:
                return up;
        }
    }
}
