package algorithm.ga.evolution.crossover;

import algorithm.ga.base.Chromosome;
import main.Configuration;

import java.util.Arrays;

/**
 *
 */

public class OnePointCrossover {

    public static Chromosome [] doCrossover (Chromosome a, Chromosome b)
    {
        // randomly generate crossover point
        int crossoverPoint = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);

        char [] offspringOne = new char [Configuration.instance.numberOfItems];
        char [] offspringTwo = new char [Configuration.instance.numberOfItems];
        // array copy (srcArr, srcPos, dest, destPos, length)

        // first offspring
        System.arraycopy(a.getGene(), 0, offspringOne, 0, crossoverPoint);
        System.arraycopy(b.getGene(), crossoverPoint, offspringOne, crossoverPoint, b.getGene().length - crossoverPoint);

        // second offspring
        System.arraycopy(b.getGene(), 0, offspringTwo, 0, crossoverPoint);
        System.arraycopy(a.getGene(), crossoverPoint, offspringTwo, crossoverPoint, b.getGene().length - crossoverPoint);

        return new Chromosome[] {new Chromosome (offspringOne), new Chromosome (offspringTwo)};
    }

}
