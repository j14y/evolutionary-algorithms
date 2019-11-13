package algorithm.ga.evolution.selection;

import algorithm.ga.base.Chromosome;
import algorithm.ga.base.Comparators;
import algorithm.ga.base.Population;
import main.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tournament {

    public static Chromosome [] select (Population population, int tournamentSize)
    {
        Chromosome [] tournament = new Chromosome[tournamentSize];
        ArrayList<Chromosome> candidates = new ArrayList(Arrays.asList(population.getPopulation()));
        for (int i = 0; i < tournamentSize; i++)
        {
            Chromosome c = candidates.remove(Configuration.instance.randomGenerator.nextInt(candidates.size()));
            tournament[i] = c;
        }
        Arrays.sort(tournament);
        return new Chromosome[] {tournament[0], tournament[1]};
    }
}
