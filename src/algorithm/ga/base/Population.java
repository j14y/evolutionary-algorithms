package algorithm.ga.base;

import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.TwoPointCrossover;
import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Tournament;
import algorithm.ga.main.GeneticAlgorithm;
import main.Configuration;

import java.util.Arrays;

public class Population {
    private double elitismRatio;
    private double mutationRatio;
    private double crossoverRatio;
    private Chromosome[] population;
    private int numberOfCrossoverOperations = 0;
    private int numberOfMutationOperations = 0;

    /**
     * Population consists of an array of Chromosomes representing candidate solutions.
     */

    public Population(Population p)
    {
        elitismRatio = Configuration.instance.elitismRatio;
        mutationRatio = Configuration.instance.mutationRatio;
        crossoverRatio = Configuration.instance.crossoverRatio;
        population = p.getPopulation().clone();
    }

    public Population ()
    {
        elitismRatio = Configuration.instance.elitismRatio;
        mutationRatio = Configuration.instance.mutationRatio;
        crossoverRatio = Configuration.instance.crossoverRatio;
        population = new Chromosome[Configuration.instance.populationSize];
        // randomly generate n
        for (int i = 0; i < Configuration.instance.populationSize; i++) {
            population[i] = Chromosome.generateRandom();
            population[i].fitness(GeneticAlgorithm.knapsack);
        }
        Arrays.sort(population);
    }

    public Population(int size, double crossoverRatio, double elitismRatio, double mutationRatio) {
        this.crossoverRatio = crossoverRatio;
        this.elitismRatio = elitismRatio;
        this.mutationRatio = mutationRatio;
        population = new Chromosome[size];

        // randomly generate n
        for (int i = 0; i < size; i++) {
            population[i] = Chromosome.generateRandom();
        }
        Arrays.sort(population);
    }

    public void evolution ()
    {
        Chromosome [] chromosomes = new Chromosome[population.length];
        int index = (int) Math.round(population.length * Configuration.instance.elitismRatio);
        System.arraycopy(population, 0, chromosomes, 0, index);

        while (index < chromosomes.length)
        {
            if (Configuration.instance.randomGenerator.nextDouble() < crossoverRatio)
            {
                Chromosome[] parents = selectParents();
                Chromosome[] children = doCrossover(parents);

                if (Configuration.instance.randomGenerator.nextDouble() < mutationRatio)
                {
                    chromosomes[(index++)] = children[0].doMutation();
                }
                else
                {
                    chromosomes[(index++)] = children[0];
                }

                if (index < chromosomes.length)
                {
                    if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio)
                    {
                        chromosomes[index] = children[1].doMutation();
                        numberOfMutationOperations++;
                    }
                    else {
                        chromosomes[index] = children[1];
                    }
                }
            }
            else if (Configuration.instance.randomGenerator.nextDouble() < mutationRatio)
            {
                chromosomes[index] = population[index].doMutation();
                numberOfMutationOperations++;
            }
            else
            {
                chromosomes[index] = population[index];
            }
            index++;
        }
        Arrays.sort(chromosomes);
        population = chromosomes;
    }

    public Chromosome [] doCrossover (Chromosome [] parents)
    {

        if (Configuration.instance.randomGenerator.nextDouble() < 0.4)
            return TwoPointCrossover.doCrossover(parents[0], parents[1]);
        else
            return OnePointCrossover.doCrossover(parents[0], parents[1]);
    }

    public void evolve ()
    {
        Chromosome [] chromosomes = new Chromosome[population.length];
        int index = (int) Math.round(population.length * Configuration.instance.elitismRatio);
        System.arraycopy(population, 0, chromosomes, 0, index);

        while (index < chromosomes.length)
        {
            if (Configuration.instance.randomGenerator.nextDouble() <= crossoverRatio)
            {
                Chromosome[] parents = this.selectParents();
                Chromosome[] children = TwoPointCrossover.doCrossover(parents[0], parents[1]);
                numberOfCrossoverOperations++;

                if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                    chromosomes[(index++)] = children[0].doMutation();
                    numberOfMutationOperations++;
                } else
                    chromosomes[(index++)] = children[0];

                // add second child

                if (index < chromosomes.length)
                {
                    if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio)
                    {
                        chromosomes[index] = children[1].doMutation();
                        numberOfMutationOperations++;
                    }
                    else
                        chromosomes[index] = children[1];
                }
            }
            else if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio)
            {
                chromosomes[index] = population[index].doMutation();
                numberOfMutationOperations++;
            } else {
                chromosomes[index] = population[index];
            }

            index++;
        }
        Arrays.sort(chromosomes);
        population = chromosomes;
    }


    public Chromosome [] selectParents ()
    {
        if (Configuration.instance.randomGenerator.nextDouble() < 0.8)
        {
            return new Chromosome[] { RouletteWheel.select(new Population(this)), RouletteWheel.select(new Population(this))};
        }
        else
        {
            return Tournament.select(this, 4);
        }
     }


    public Chromosome [] getPopulation ()
    {
        return this.population;
    }

    public Chromosome getIndividual (int i)
    {
        return population[i];
    }

    public int getPopulationSize ()
    {
        return this.population.length;
    }
}
