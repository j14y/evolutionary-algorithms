package algorithm.aco.main;

import algorithm.aco.base.AntColony;
import data.Knapsack;
import main.Configuration;

public class AntColonyOptimization {

    AntColony antColony;

    public AntColonyOptimization (Knapsack k)
    {
        antColony = new AntColony (k);
    }

    public void execute ()
    {

        int iterations = 0;
        while (iterations < Configuration.instance.numberOfIterations)
        {
            iterations++;
            antColony.execute();
            antColony.updateGlobalSolution();
            antColony.decay();
            antColony.updatePheromones();
        }
    }

    public AntColony getAntColony ()
    {
        return antColony;
    }

}
