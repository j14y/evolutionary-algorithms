package algorithm.ga.base;

import algorithm.ga.evolution.mutation.*;
import algorithm.ga.main.GeneticAlgorithm;
import data.Knapsack;
import main.Configuration;

/**
 * Binary encoded representation of the items within a knapsack
 */

public class Chromosome implements Comparable<Chromosome>{
    private char [] gene; // each chromosome is represented as an array of chars
    private int fitness; // fitness value for each chromosome

    public Chromosome ()
    {
        gene = new char [Configuration.instance.numberOfItems];
        fitness = 0;
    }

    public Chromosome (char [] g)
    {
        this.gene = g;
        fitness(GeneticAlgorithm.knapsack);
    }

    public void fitness (Knapsack k)
    {
        int weight = 0;
        int value = 0;
        for (int i = 0; i < gene.length; i++)
        {
            if (gene[i] == '1')
            {
                weight += k.getItem(i).getWeight();
                value += k.getItem(i).getValue();
            }
        }

        if (weight > Configuration.instance.maximumCapacity)
        {
            int index = Configuration.instance.randomGenerator.nextInt(getGene().length);
            while (gene[index] != '1')
                index = Configuration.instance.randomGenerator.nextInt(getGene().length);
            BitFlip.flip(gene, index);
            fitness(k);
        }

        fitness = value;
    }


    /**
     * Define fitness relative to best-known optimum
     * @return
     */
    public int calculateFitness ()
    {
        int fitness = 0;
        for (char c : gene)
        {
            this.fitness += (c == '1') ? 1 : 0;
        }
        return this.fitness;
    }

    public void calculateFitness(char [] g)
    {
        for (char c : g)
        {
            if (c == '1')
            {

            }
        }
    }

    public char [] getGene ()
    {
        return this.gene;
    }

    public void setAllele (int i, char v)
    {
        gene [i] = v;
    }

    public void setGene (char [] g)
    {
        this.gene = g;
    }

    /**
     * Randomly generates and returns a new chromosome
     * @return
     */
    public static Chromosome generateRandom()
    {
        Chromosome chromosome = new Chromosome();
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
        {
            if (Configuration.instance.randomGenerator.nextDouble() < 0.5)
            {
                chromosome.setAllele(i, '1');
            } else {
                chromosome.setAllele(i, '0');
            }
        }
        return chromosome;
    }

    public int getFitness ()
    {
        return this.fitness;
    }

    // note that mutation implementation was changed to use all mutation operators
    // as per the notification by Geoff
    public Chromosome doMutation ()
    {
        switch (Configuration.instance.randomGenerator.nextInt(5))
        {
            // bit Flip
            case 0:
            {
                return BitFlip.flip(this);
            }

            // Displacement
            case 1:
            {
                return Inversion.mutate(this);
            }

            // Exchange
            case 2:
            {
                return Exchange.mutate(this);
            }

            // insertion
            case 3:
            {
                return Insertion.mutate(this);
            }

            // inversion
            case 4:
            {
                return Inversion.mutate(this);
            }

            default:
            {
                return new Chromosome();
            }
        }
    }

    @Override
    public int compareTo(Chromosome o) {
        if (this.getFitness() < o.getFitness())         return -1;
        else if (this.getFitness() > o.getFitness())    return  1;
        else                                            return  0;
    }
}
