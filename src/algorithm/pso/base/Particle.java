package algorithm.pso.base;

import algorithm.pso.main.ParticleSwarmOptimization;
import data.Knapsack;
import main.Configuration;

import java.util.Arrays;

public class Particle {

    private int [] position; // X_id in {0, 1}
    private double [] velocity;
    private int [] bestPosition;
    private int bestValue;
    private int currentValue;

    private double C1 = Configuration.instance.C1, C2 = Configuration.instance.C2;

    public Particle ()
    {
    }

    public Particle (int [] p, double [] v)
    {
        position = p;
        velocity = v;
        bestValue = 0;
    }

    public Particle (int p, int v)
    {
        position = new int [p];
        velocity = new double [v];
        randomizePosition();
        bestPosition = position.clone();
        bestValue = 0;
    }

    public void evaluateObjective ()
    {
        int w = 0;
        int v = 0;
        for (int i = 0; i < position.length; i++)
        {
            if (position[i] == 1)
            {
                v += ParticleSwarmOptimization.knapsack.getItem(i).getValue();
                w += ParticleSwarmOptimization.knapsack.getItem(i).getWeight();
            }
        }

        if (w > Configuration.instance.maximumCapacity)
            currentValue = 0;
        else
            currentValue = v;
    }

    public void updateSolution ()
    {
        if (currentValue > bestValue)
        {
            bestValue = currentValue;
            bestPosition = position.clone();
        }
    }

    public void updateVelocity ()
    {
        for (int i = 0; i < velocity.length; i++)
        {
            double R1 = Configuration.instance.randomGenerator.nextDouble();
            double R2 = Configuration.instance.randomGenerator.nextDouble();
            velocity[i] = clamp(Configuration.instance.inertiaWeight * velocity[i] + (C1 * R1) * (bestPosition[i] - position[i]) + (C2 * R2) * (Swarm.best[i] - position[i]));
        }
    }

    public void updatePosition ()
    {
        for (int i = 0; i < position.length; i++)
        {
            position[i] = (Configuration.instance.randomGenerator.nextDouble() < sigmoid(velocity[i])) ? 1 : 0;
        }
    }

    public void randomizePosition ()
    {
        for (int i = 0; i < position.length; i++)
        {
            position[i] = (Configuration.instance.randomGenerator.nextDouble() < 0.23) ? 1 : 0;
        }
    }

    public double clamp (double v)
    {
        if (v >= Configuration.instance.vMin && v <= Configuration.instance.vMax)
            return v;
        else if (v < Configuration.instance.vMin)
            return Configuration.instance.vMin;
        else
            return Configuration.instance.vMax;
    }

    /**
     * Sigmoidal function
     * @param v
     * @return
     */
    public static double sigmoid (double v)
    {
        return (1.0 / (1.0 + Math.exp( (-Configuration.instance.delta) * v)));
    }

    public int getBestValue ()
    {
        return this.bestValue;
    }

    public int [] getBestPosition ()
    {
        return this.bestPosition;
    }
}
