package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import algorithm.ga.base.Population;
import main.Configuration;

/**
 * Exchange mutation
 * =================
 * Select two genes randomly and exchange/swap them.
 */

public class Exchange {

    public static Chromosome mutate (Chromosome a)
    {
        int i = Configuration.instance.randomGenerator.nextInt(a.getGene().length);
        int j = Configuration.instance.randomGenerator.nextInt(a.getGene().length);
        char [] newGene = a.getGene();
        char temp = newGene[i];
        newGene[i] = newGene[j];
        newGene[j] = temp;
        return new Chromosome(newGene);
    }
}