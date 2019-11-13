package algorithm.ga.evolution.crossover;

import algorithm.ga.base.Chromosome;
import main.Configuration;

/**
 *
 */

public class TwoPointCrossover {

    public static Chromosome [] doCrossover (Chromosome a, Chromosome b)
    {
        int startPoint = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int endPoint   = startPoint + Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems - startPoint);

        char [] offspringOne = new char [Configuration.instance.numberOfItems];
        char [] offstringTwo = new char [Configuration.instance.numberOfItems];

        /* 0        sp      ep      len
         * --------------------------
         * |        |       |       |
         * --------------------------
         *
         * Remember to verify how arraycopy handles indices
         */

        System.arraycopy(a.getGene(), 0, offspringOne, 0, startPoint);
        System.arraycopy(b.getGene(), startPoint, offspringOne, startPoint, endPoint - startPoint);
        System.arraycopy(a.getGene(), endPoint, offspringOne, endPoint, Configuration.instance.numberOfItems - endPoint);

        System.arraycopy(b.getGene(), 0, offstringTwo, 0, startPoint);
        System.arraycopy(a.getGene(), startPoint, offstringTwo, startPoint, endPoint - startPoint);
        System.arraycopy(b.getGene(), endPoint, offstringTwo, endPoint, Configuration.instance.numberOfItems - endPoint);

        return new Chromosome[] {new Chromosome(offspringOne), new Chromosome (offstringTwo)};
    }

}
