package algorithm.ga.main;

import algorithm.ga.base.Chromosome;
import algorithm.ga.base.Population;
import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Tournament;
import data.Knapsack;
import main.Configuration;

public class GeneticAlgorithm {

    private Population population;
    private double currentBestFitness;
    private Chromosome bestChromosome;
    public static Knapsack knapsack;


    public GeneticAlgorithm ()
    {
    }

    public GeneticAlgorithm (Knapsack k)
    {
        knapsack = k;
    }

    public void execute ()
    {
        population = new Population ();
        bestChromosome = population.getPopulation()[0];
        currentBestFitness = 0.0;
        int iterations = 0;
        while (iterations < Configuration.instance.numberOfIterations)
        {
            population.evolution();
            bestChromosome = population.getPopulation()[0];
            if (bestChromosome.getFitness() > currentBestFitness)
            {
                currentBestFitness = bestChromosome.getFitness();
            }
            iterations++;
        }

    }

    public void setKnapsack (Knapsack k)
    {
        this.knapsack = k;
    }

    public void setPopulation (Population p)
    {
        this.population = p;
    }

    public double getCurrentBestFitness ()
    {
        return currentBestFitness;
    }

    public Chromosome getBestChromosome ()
    {
        return bestChromosome;
    }

    public int getSolutionWeight ()
    {
        int weight = 0;
        for (int i = 0; i < bestChromosome.getGene().length; i++)
        {
            if (bestChromosome.getGene()[i] == '1')
            {
                weight += knapsack.getItem(i).getWeight();
            }
        }

        return weight;
    }
}
