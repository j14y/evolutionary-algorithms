package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import main.Configuration;

/**
 * Displacement mutation
 * =====================
 * Randomly selected section of the chromosome is moved to another location in the chromosome.
 * Reference: https://www.youtube.com/watch?v=UgXDhdPe72M
 */

public class Displacement {

    public static Chromosome mutate (Chromosome a)
    {
        int p0 = Configuration.instance.randomGenerator.nextInt(a.getGene().length);
        int p1 = Configuration.instance.randomGenerator.nextInt(a.getGene().length);
        int len = p1 - p0;
        if (len == 0)
        {
            mutate(a); // recurse operation if indices are the same
        }
        else if (len < 0)
        {
            len = Math.abs(len);
            int temp = p0;
            p0 = p1;
            p1 = temp;
        }

        // new position
        int newPos = Configuration.instance.randomGenerator.nextInt(a.getGene().length - len);

        char [] subGene = new char[len];
        char [] newGene = new char[a.getGene().length];
        System.arraycopy(a.getGene(), p0, subGene, 0, len);

        // set new gene
        System.arraycopy(a.getGene(), 0, newGene, 0, p0);
        System.arraycopy(a.getGene(), p0, newGene, p0, len);
        System.arraycopy(a.getGene(), p0 + len, newGene, p0+len,Math.abs(len - p1));
        return new Chromosome(newGene);
    }
}