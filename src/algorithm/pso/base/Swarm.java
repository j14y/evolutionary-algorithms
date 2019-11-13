package algorithm.pso.base;

import data.Knapsack;
import main.Configuration;

public class Swarm {

    private Particle [] swarm;
    public static int [] best; // best position
    public int swarmBestValue;

    public Swarm (int s)
    {
        swarm = new Particle[s];
        best = new int [Configuration.instance.numberOfItems];
        for (int i = 0; i < swarm.length; i ++)
        {
            swarm[i] = new Particle(Configuration.instance.numberOfItems, Configuration.instance.numberOfItems);
        }
        swarmBestValue = 0;
    }

    public void evaluateParticles ()
    {
        for (Particle p : swarm)
            p.evaluateObjective();
    }

    public void updateBestParticleSolutions ()
    {
        for (Particle p : swarm)
            p.updateSolution();
    }

    public void updateBestGlobalSolution ()
    {
        for (Particle p : swarm)
        {
            if (p.getBestValue() > this.swarmBestValue)
            {
                swarmBestValue = p.getBestValue();
                Swarm.best = p.getBestPosition().clone();
            }
        }
    }

    public void updateParticles ()
    {
        for (Particle p : swarm)
        {
            p.updateVelocity();
            p.updatePosition();
        }
    }

    public int getSwarmBestValue ()
    {
        return swarmBestValue;
    }

    public int [] getBest ()
    {
        return best;
    }
}
