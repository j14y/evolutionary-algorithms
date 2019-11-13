package algorithm.sa.main;

import algorithm.ga.evolution.mutation.BitFlip;
import data.Knapsack;
import main.Configuration;

public class SimulatedAnnealing {

    private Knapsack knapsack;
    private char [] current, next, best;

    public SimulatedAnnealing (Knapsack k)
    {
        knapsack = k;
    }

    public void anneal ()
    {
        double temperature = Configuration.instance.maxTemperature;
        double endingTemperature = Configuration.instance.minTemperature;
        double alpha = Configuration.instance.alpha;
        current = randomSolution(Configuration.instance.numberOfItems);
        best = current.clone();
        int iterations = 0;
        while (temperature > endingTemperature)
        {
            iterations++;
            next = createNeighbour(current, Configuration.instance.neighbourChanges);
            int currentEnergy = objective(current);
            int newEnergy = objective(next);

            if (acceptByProbability(currentEnergy, newEnergy, temperature))
            {
                current = next.clone();
            }

            if (objective(current) > objective(best))
            {
                best = current.clone();
            }
            temperature *= 1 - alpha;
        }
    }

    public int findBestWeight()
    {
        int weight = 0;
        for (int i = 0; i < best.length; i++)
        {
            if (best[i] == '1')
                weight += knapsack.getItem(i).getWeight();
        }
        return weight;
    }

    public int objective (char [] s)
    {
        int w = 0, v = 0;
        for (int i = 0; i < s.length; i++)
        {
            if (s[i] == '1')
            {
                v  +=  knapsack.getItem(i).getValue();
                w +=  knapsack.getItem(i).getWeight();
            }
        }

        if (w > Configuration.instance.maximumCapacity)
        {
            int index = Configuration.instance.randomGenerator.nextInt(s.length);
            while (s[index] != '1')
                index = Configuration.instance.randomGenerator.nextInt(s.length);
            BitFlip.flip(s, index);
            objective(s);
        }

        return v;

        /*
        if (w > Configuration.instance.maximumCapacity)
            return 0;
        else
            return v;
         */

    }

    public int getBestValue ()
    {
        return objective(best);
    }

    public char [] getBest() { return best; }

    public char [] randomSolution (int len)
    {
        char [] solution = new char[len];
        for (int i = 0; i < len; i++)
        {
            solution[i] = (Configuration.instance.randomGenerator.nextDouble() > Configuration.instance.randomWeight) ? '1' : '0';
        }
        return solution;
    }

    public char [] createNeighbour (char [] s, int c)
    {
        char [] n = s.clone();
        for (int i = 0; i < c; i++)
        {
            int p = Configuration.instance.randomGenerator.nextInt(n.length);
            n[i] = (n[i] == '0') ? '1' : '0';
        }
        return n;
    }


    public boolean acceptByProbability(double currentFitness, double newFitness, double temperature) {
        if (newFitness > currentFitness)
            return true;
        else {
            double delta = currentFitness - newFitness;
            double probability = Math.exp(delta / temperature);
            return probability > Configuration.instance.randomGenerator.nextDouble();
        }
    }
}
