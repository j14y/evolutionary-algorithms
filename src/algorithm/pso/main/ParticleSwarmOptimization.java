package algorithm.pso.main;

import algorithm.pso.base.Particle;
import algorithm.pso.base.Swarm;
import data.Knapsack;
import main.Configuration;

import java.util.Arrays;

public class ParticleSwarmOptimization {

    Swarm swarm;
    public static Knapsack knapsack;

    public ParticleSwarmOptimization ()
    {
        swarm = new Swarm(Configuration.instance.numberOfParticles);
    }

    public ParticleSwarmOptimization (Knapsack k)
    {
        swarm = new Swarm(Configuration.instance.numberOfParticles);
        knapsack = k;
    }

    public void execute ()
    {
        int iterations = 0;
        while (iterations < Configuration.instance.numberOfIterations)
        {
            iterations++;
            swarm.evaluateParticles();
            swarm.updateBestParticleSolutions();
            swarm.updateBestGlobalSolution();
            swarm.updateParticles();
        }
    }

    public double getBestValue ()
    {
        return swarm.getSwarmBestValue();
    }

    public int getWeight ()
    {
        int w = 0;
        for (int i = 0; i < swarm.getBest().length; i++)
        {
            if (swarm.getBest()[i] == 1)
                w += knapsack.getItem(i).getWeight();
        }
        return w;
    }

    public void printSolution ()
    {
        int w = 0;
        for (int i = 0; i < swarm.getBest().length; i++)
        {
            if (swarm.getBest()[i] == 1)
                w += knapsack.getItem(i).getWeight();
        }
        System.out.println("Output\n\n");
        System.out.println("Knapsack value: " + swarm.getSwarmBestValue());
        System.out.println("Knapsack weight: " + w);
        System.out.println("Globally optimal particle: " + Arrays.toString(swarm.getBest()));
    }



    public void setKnapsack(Knapsack k)
    {
        knapsack = k;
    }
}
