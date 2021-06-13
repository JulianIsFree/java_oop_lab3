package nsu.oop.explorer.frontend.visual.fogFactory;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nsu.oop.explorer.backend.model.noise.DiscreteDistribution;

import java.util.LinkedList;
import java.util.List;

public class FogFactory {
    class FactoriesCounter {
        private Integer counter;
        private boolean isStarted;
        FactoriesCounter() {
            counter = 0;
            isStarted = false;
        }

        void increment() {
            synchronized (counter) {
                if (!isStarted) {
                    isStarted = true;
                }
                counter++;
            }
        }

        void decrement() {
            synchronized (counter) {
                counter--;
            }
        }

        boolean isReady() {
            boolean res = false;
            synchronized (counter) {
                res = (counter == 0) && isStarted;
            }
            return res;
        }
    }
    private List<FogGenerator> generators;

    private int width;
    private int height;
    private int N;

    private double[][] heightMap;
    private Pixmap pixmap;
    private DiscreteDistribution distribution;

    private FactoriesCounter counter;

    public FogFactory(int width, int height, int N) {
        generators = new LinkedList<>();
        distribution = new DiscreteDistribution();

        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        heightMap = new double[width][height];

        counter = new FactoriesCounter();

        this.width = width;
        this.height = height;
        this.N = N;
    }

    private void initGenerators() {
        counter = new FactoriesCounter();
        generators.clear();
        int offset = 0;
        for (int i = 0; i < N; ++i) {
            int dwidth = (width / N) + ((i < (width % N)) ? 1 : 0);
            System.out.println(offset + " " + dwidth);
            FogGenerator generator = new FogGenerator(offset, width, dwidth, height, distribution,
                    heightMap, pixmap, counter);
            generators.add(generator);
            offset += dwidth;
        }
    }

    public void drawFog(SpriteBatch batch, Texture fogTexture) {
        initGenerators();
        distribution.reset();
        for (FogGenerator generator : generators)
            generator.start();

        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(!counter.isReady());

        for(int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (heightMap[i][j] < 0) {
                    batch.draw(fogTexture, i, j);
                }
            }
        }
    }
}
