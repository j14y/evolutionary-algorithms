package main;

import random.MersenneTwisterFast;

public enum Configuration {
    instance;

    // command line arg configuration
    public enum ALGORITHM {
        GA, PSO, ACO, SA, BEST
    };

    public enum CONFIG {
        DEFAULT, BEST
    }

    public ALGORITHM algorithm;
    public CONFIG config;
    public boolean searchBestConfiguration;

    public String fileSeparator = System.getProperty("file.separator");
    public String userDirectory = System.getProperty("user.dir");
    public String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public String dataFilePath = dataDirectory + "knapsack_instance.csv";
    public String dataRDirectory = userDirectory;
    public String dataDelimiter = ";";

    public String sourceDirectory = userDirectory + fileSeparator + "src" + fileSeparator;

    public MersenneTwisterFast randomGenerator = new MersenneTwisterFast(System.nanoTime());

    public int numberOfItems = 150;
    public int maximumCapacity = 822;
    public int bestKnownOptimum = 1013;

    /*
     *  Genetic Algorithm
     */

    public double mutationRatio = 0.02;
    public double elitismRatio = 1.0; // 1.0
    public double crossoverRatio = 0.8;
    public int populationSize = 150; // 150

    /*
     *  Simulated Annealing
     */

    public double maxTemperature = 100000.0;
    public double minTemperature = 1000.0;
    public double alpha = 0.002;
    public int neighbourChanges = 150;// this.randomGenerator.nextInt(150);
    public double randomWeight = 0.1;

    /*
     * Particle Swarm Optimization
     */

    public double inertiaWeight = 1.0;
    public double C1 = 1.8;
    public double C2 = 1.8;
    public int numberOfParticles = 20;
    public double vMax = 3.0;
    public double vMin = -3.0;
    public double delta = 1.0;

    /*
     * Ant Colony Optimization
     */

    public int numberOfIterations = 10000;
    public double decayFactor = 0.03;
    public int numberOfAnts = 25;
    public double startPheromoneValue = 0.0005;

    public int alphaACO = 2;
    public int betaACO = 1;

    /*
     * Recommenders
     */

    public double minSolutionQuality = 0.8;

    public void printConfig ()
    {
        System.out.println("\nInput configuration\n");
        switch (algorithm)
        {
            case GA: {
                System.out.println("Elitism ratio: " + elitismRatio);
                System.out.println("Mutation ratio: " + mutationRatio);
                System.out.println("Crossover ratio: " + crossoverRatio);
                break;
            }

            case PSO: {
                System.out.println("Inertia weight: " + inertiaWeight);
                System.out.println("Number of particles: " + numberOfParticles);
                System.out.println("C1: " + C1);
                System.out.println("C2: " + C2);
                System.out.println("Delta: " + delta);
                System.out.println("[vMin, vMax]: " + "[" + vMin + ", " + vMax + "]");
                break;
            }

            case SA: {
                System.out.println("* Maximum temperature - " + maxTemperature);
                System.out.println("* Minimum temperature - " + minTemperature);
                System.out.println("* Alpha - " + alpha);
                System.out.println("* Random weight - " + randomWeight);
                System.out.println("* Neighbourhood changes - " + neighbourChanges);
                break;
            }

            case ACO: {
                System.out.println("* Number of ants: " + numberOfAnts);
                System.out.println("* Decay factor: " + decayFactor);
                System.out.println("* Initial pheromone value: " + startPheromoneValue);
                break;
            }

            case BEST: {
                System.out.println("Running best algorithm");
                break;
            }

            default: {
                System.out.println("No algorithm selected.");
            }
        }
    }
}