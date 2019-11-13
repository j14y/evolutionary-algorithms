package algorithm.ga.base;

import java.util.Comparator;

public class Comparators {
    public static final Comparator<Chromosome> FITNESS = (Chromosome o1, Chromosome o2) -> (Integer.compare(o1.getFitness(), o2.getFitness()));
}
