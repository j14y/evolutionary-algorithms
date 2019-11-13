package algorithm.ga.evolution.selection;

import algorithm.ga.base.Chromosome;
import algorithm.ga.base.Population;
import main.Configuration;

import java.util.Arrays;

public class RouletteWheel {


    /**
     * Spins the roulette wheel once
     */
    public void spin ()
    {

    }

    public static Chromosome select (Population p)
    {
        int totalFitness = 0;
        for (int i = 0; i < p.getPopulationSize(); i++)
            totalFitness += p.getIndividual(i).getFitness();

        int x = Configuration.instance.randomGenerator.nextInt(totalFitness);
        int currentCount = 0;
        for (int i = 0; i < p.getPopulationSize(); i++)
        {
            currentCount = currentCount + p.getIndividual(i).getFitness();
            if (currentCount >= x)
            {
                return p.getIndividual(i);
            }
        }
        return null;
    }

    /*
    public static Chromosome select (Population population)
    {
        Chromosome selection = null;
        // find sum of population fitness
        double sum = 0.0;
        for (Chromosome c : population.getPopulation())
        {
            sum += (double)c.getFitness();
        }
        int limit = Configuration.instance.randomGenerator.nextInt(population.getPopulationSize());
        double sumExpectedValues = 0.0;

        // loop through population
        for (int i = 0; i < population.getPopulationSize(); i++)
        {
            sumExpectedValues += ((double) population.getIndividual(i).getFitness() / sum);
            if (sumExpectedValues >= limit)
            {
                selection = new Chromosome(population.getIndividual(i).getGene().clone());
            }
        }
        System.out.println(limit);
        System.out.println(sumExpectedValues);
        return selection;
    }*/

    public static Chromosome spin (Population p)
    {
        // calculate sum of fitness values
        int populationFitness = 0;
        for (Chromosome c : p.getPopulation())
        {
            populationFitness += c.calculateFitness();
        }
        double  [] individualFitness = new double [p.getPopulationSize()];
        double pr = 0.0, prSum = 0.0;
        for (int i = 0; i < individualFitness.length; i++)
        {
            individualFitness[i] = prSum + p.getIndividual(i).calculateFitness() / populationFitness;
            prSum += individualFitness[i];
        }

        // partition roulette wheel



        // select chromosome
        double r = Configuration.instance.randomGenerator.nextDouble();
        for (int i = 0; i < p.getPopulationSize(); i++)
        {
            if (r > pr)
            {

            }
        }

        return new Chromosome();
    }

}
