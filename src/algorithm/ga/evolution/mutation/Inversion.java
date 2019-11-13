package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import main.Configuration;

/**
 * Inversion mutation
 * ==================
 * Pick two alleles at random and invert the substring between them
 */

public class Inversion {

    public static Chromosome mutate (Chromosome c)
    {
        char [] newGene = c.getGene().clone();
        int from = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int to = from + Math.abs(Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems - from));
        for (int i = from + 1; i < to; i++)
        {
            BitFlip.flip(c.getGene(), i);
        }
        return new Chromosome(newGene);
    }

    public static Chromosome mutate (Chromosome c, int from, int to)
    {
        char [] newGene = c.getGene().clone();
        for (int i = from; i < to; i++)
        {
            BitFlip.flip(c.getGene(), i);
        }
        return new Chromosome(newGene);
    }
}