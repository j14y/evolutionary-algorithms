package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import main.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Insertion mutation
 * ==================
 * Gene is selected randomly and inserted randomly into another position in the sequence.
 * Reference: http://ijcsit.com/docs/Volume%205/vol5issue03/ijcsit20140503404.pdf
 */

public class Insertion {

    /**
     * Picks two allele values at random. Then moves the second allele to follow the first, shifting the
     * rest along to accommodate.
     * @param a Chromosome instance
     */
    public static Chromosome mutate (Chromosome a)
    {
        // select gene indices
        int p0 = Configuration.instance.randomGenerator.nextInt(a.getGene().length);
        int p1 = Configuration.instance.randomGenerator.nextInt(a.getGene().length);
        if (p0 == p1)
            mutate(a);

        // for sake of ease
        ArrayList<Character> temp = new ArrayList<>(a.getGene().length);
        for (char l : a.getGene().clone())
        {
            temp.add(l);
        }
        char val = temp.get(p0);
        temp.add(p1 + 1, val);
        temp.remove(p0);
        char [] newGene = new char[a.getGene().length];
        for (int i = 0; i < temp.size() - 1; i++)
            newGene[i] = temp.get(i);
        return new Chromosome(newGene);
    }

    public static Chromosome mutate ()
    {
        return null;
    }
}