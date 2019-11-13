package algorithm.aco.base;

import data.Item;
import data.Knapsack;
import main.Configuration;

import java.util.ArrayList;

public class AntColony {

    private static double [] pheromones;
    private Ant [] antArray;
    private Knapsack knapsack;
    private Ant bestAnt;
    private int bestFitness;
    private ArrayList<Item> bestSolution;

    public AntColony (Knapsack k)
    {
        knapsack = k;
        pheromones = new double [Configuration.instance.numberOfItems];
        bestAnt =  new Ant(knapsack);
        // init pheromones
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
        {
            pheromones [i] = Configuration.instance.startPheromoneValue;
        }
        // init ant array
        antArray = new Ant [Configuration.instance.numberOfAnts];
        for (int i = 0; i < Configuration.instance.numberOfAnts; i++)
        {
            antArray[i] = new Ant(knapsack);
        }
    }

    public void execute ()
    {
        for (int i = 0; i < antArray.length; i++)
        {
            antArray[i].newRound(knapsack);
            antArray[i].lookForWay();
        }
    }

    public void work ()
    {
        for (Ant a : antArray)
        {
            a.work();
        }
    }

    public void updateGlobalSolution ()
    {
        for (int i = 0; i < antArray.length; i++)
        {
            if (bestFitness < antArray[i].getValue())
            {
                bestFitness = antArray[i].getValue();
                bestAnt = antArray[i];
                bestSolution = antArray[i].getBestSolution();
            }
        }
    }

    public void decay ()
    {
        for (int i = 0; i < pheromones.length; i++)
            pheromones[i] *= (1.0 - Configuration.instance.decayFactor);
    }


    public void updatePheromones ()
    {
        bestAnt.layPheromone();
    }

    public static void addPheromone (int i, double p)
    {
        pheromones[i] += p;
    }

    public static double getPheromone (int i)
    {
        return pheromones[i];
    }

    public Ant getBestAnt ()
    {
        return this.bestAnt;
    }

    public int getBestFitness ()
    {
        return bestFitness;
    }
}