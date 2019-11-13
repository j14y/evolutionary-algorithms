package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import main.Configuration;

/**
 * Inverts the value of a bit
 */

public class BitFlip {
    public static Chromosome flip (Chromosome a, int i)
    {
        char [] newGene = a.getGene().clone();
        newGene[i] = (newGene[i] == '1') ? '0' : '1';
        return new Chromosome(newGene);
    }

    public static Chromosome flip (Chromosome a)
    {
        char [] newGene = a.getGene().clone();
        int i = Configuration.instance.randomGenerator.nextInt(newGene.length);
        newGene[i] = (newGene[i] == '1') ? '0' : '1';
        return new Chromosome(newGene);
    }

    public static void flip (char [] g, int i)
    {
        g[i] = (g[i] == '1') ? '0' : '1';
    }
}