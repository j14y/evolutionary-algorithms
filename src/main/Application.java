package main;

import algorithm.aco.main.AntColonyOptimization;
import algorithm.aco.recommender.AntColonyRecommender;
import algorithm.ga.main.GeneticAlgorithm;
import algorithm.ga.recommender.GeneticAlgorithmRecommender;
import algorithm.pso.main.ParticleSwarmOptimization;
import algorithm.pso.recommender.ParticleSwarmRecommender;
import algorithm.sa.main.SimulatedAnnealing;
import algorithm.sa.recommender.SimulatedAnnealingRecommender;
import data.Item;
import data.Knapsack;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Application {
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args) {
        String algorithm = "", configuration = "";
        boolean searchBestConfiguration = false;
        Knapsack knapsack = new Knapsack();
        loadData (knapsack);

        /******************
         * TOKENIZE INPUT *
         ******************/

        String [] options = new String[3];
        Stack <String> tokens = new Stack();
        List <String> progArgs = Arrays.asList(args);
        Collections.reverse(progArgs);
        tokens.addAll(progArgs);

        while (! tokens.empty())
        {
            String token = tokens.pop();

            if (token.contains("-algorithm")) // [ga | sa | aco | pso | best-algorithm]
            {
                algorithm = tokens.pop();
            }

            if (token.contains("-configuration")) // [default | best] - choose default if none chosen
            {
                configuration = tokens.pop();
                continue;
            }

            if (token.contains("-search_best_configuration"))
            {
                System.out.println("Best configuration flag detected.");
                Configuration.instance.searchBestConfiguration = true;
                continue;
            }
        }

        /*****************
         * VALIDATE ARGS *
         *****************/

        assert (algorithm.contains("best-algorithm") && !(configuration.contains("default") || configuration.contains("best")));
        assert (algorithm.contains("best-algorithm") && !searchBestConfiguration);

        /*************
         * ALGORITHM *
         *************/

        switch (algorithm) {
            case "ga":
            {
                Configuration.instance.algorithm = Configuration.ALGORITHM.GA;

                if (Configuration.instance.searchBestConfiguration)
                {
                    GeneticAlgorithmRecommender recommender = new GeneticAlgorithmRecommender();
                    recommender.recommend(Configuration.instance.minSolutionQuality, knapsack);
                    break;
                }

                switch (configuration)
                {
                    case "default":
                    {
                        Configuration.instance.config = Configuration.CONFIG.DEFAULT;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    case "best": {
                        Configuration.instance.config = Configuration.CONFIG.BEST;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    default: {

                    }
                }

                System.out.println("=================");
                System.out.println("Genetic algorithm");
                System.out.println("=================");
                System.out.println();
                Configuration.instance.printConfig();
                GeneticAlgorithm ga = new GeneticAlgorithm(knapsack);
                ga.execute();
                System.out.println("\n\nOutput\n\n");
                System.out.println("Best chromosome: " + Arrays.toString(ga.getBestChromosome().getGene()));
                System.out.println("Total weight: " + ga.getSolutionWeight());
                System.out.println("Value: " + ga.getCurrentBestFitness());
                break;
            }

            case "sa":
            {
                Configuration.instance.algorithm = Configuration.ALGORITHM.SA;

                if (Configuration.instance.searchBestConfiguration)
                {
                    SimulatedAnnealingRecommender recommender = new SimulatedAnnealingRecommender();
                    recommender.recommend(Configuration.instance.minSolutionQuality, knapsack);
                    break;
                }

                switch (configuration)
                {
                    case "default":
                    {
                        Configuration.instance.config = Configuration.CONFIG.DEFAULT;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    case "best": {
                        Configuration.instance.config = Configuration.CONFIG.BEST;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    default: {

                    }
                }

                System.out.println("===================");
                System.out.println("Simulated Annealing");
                System.out.println("===================");
                Configuration.instance.printConfig();
                System.out.println();
                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(knapsack);
                simulatedAnnealing.anneal();

                System.out.println("Final solution: " + simulatedAnnealing.objective(simulatedAnnealing.getBest()));
                System.out.println("Weight: " + simulatedAnnealing.findBestWeight());
                break;
            }

            case "aco":
            {
                Configuration.instance.algorithm = Configuration.ALGORITHM.ACO;
                if (Configuration.instance.searchBestConfiguration)
                {
                    AntColonyRecommender recommender = new AntColonyRecommender();
                    recommender.recommend(Configuration.instance.minSolutionQuality, knapsack);
                    break;
                }

                switch (configuration)
                {
                    case "default":
                    {
                        Configuration.instance.config = Configuration.CONFIG.DEFAULT;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    case "best": {
                        Configuration.instance.config = Configuration.CONFIG.BEST;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    default: {

                    }
                }

                System.out.println("=======================");
                System.out.println("Ant Colony Optimization");
                System.out.println("=======================");
                Configuration.instance.printConfig();
                System.out.println();
                AntColonyOptimization aco = new AntColonyOptimization(knapsack);
                aco.execute();
                System.out.println("\n\nOutput\n\n");

                System.out.println("Weight: " + (Configuration.instance.maximumCapacity - aco.getAntColony().getBestAnt().getCapacity()));
                System.out.println("Value: " + aco.getAntColony().getBestFitness());
                break;
            }

            case "pso":
            {
                Configuration.instance.algorithm = Configuration.ALGORITHM.PSO;

                if (Configuration.instance.searchBestConfiguration)
                {
                    ParticleSwarmRecommender recommender = new ParticleSwarmRecommender();
                    recommender.recommend(Configuration.instance.minSolutionQuality, knapsack);
                    break;
                }

                switch (configuration)
                {
                    case "default":
                    {
                        Configuration.instance.config = Configuration.CONFIG.DEFAULT;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    case "best": {
                        Configuration.instance.config = Configuration.CONFIG.BEST;
                        loadConfiguration(Configuration.instance.algorithm, Configuration.instance.config);
                        break;
                    }

                    default: {

                    }
                }

                System.out.println("===========================");
                System.out.println("Particle Swarm Optimization");
                System.out.println("===========================");
                Configuration.instance.printConfig();
                ParticleSwarmOptimization pso = new ParticleSwarmOptimization(knapsack);
                pso.execute();
                System.out.println();
                pso.printSolution();

                break;
            }

            case "best-algorithm":
            {
                Configuration.instance.algorithm = Configuration.ALGORITHM.BEST;
                System.out.println("===============");
                System.out.println("Best Algorithm");
                System.out.println("===============");

                System.out.println("\n\nUsing default configurations for all algorithms");
                System.out.println();
                System.out.println();

                Configuration.instance.algorithm = Configuration.ALGORITHM.BEST;
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(knapsack);
                AntColonyOptimization antColonyOptimization = new AntColonyOptimization(knapsack);
                ParticleSwarmOptimization particleSwarmOptimization = new ParticleSwarmOptimization(knapsack);
                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(knapsack);

                geneticAlgorithm.execute();
                antColonyOptimization.execute();
                particleSwarmOptimization.execute();
                simulatedAnnealing.anneal();

                // compare outputs
                double max =    DoubleStream.of(  geneticAlgorithm.getCurrentBestFitness(),
                        antColonyOptimization.getAntColony().getBestFitness(),
                        particleSwarmOptimization.getBestValue(),
                        simulatedAnnealing.getBestValue()
                ).max().getAsDouble();


                System.out.print("Best algorithm: ");

                if (max == geneticAlgorithm.getCurrentBestFitness()) System.out.print("Genetic algorithm");
                else if (max == antColonyOptimization.getAntColony().getBestFitness()) System.out.print("Ant Colony Optimization");
                else if (max == particleSwarmOptimization.getBestValue()) System.out.print("Particle Swarm Optimization");
                else System.out.print("Simulated Annealing");

                System.out.println();

                System.out.print("Weight: ");
                if (max == geneticAlgorithm.getCurrentBestFitness()) System.out.print(geneticAlgorithm.getSolutionWeight());
                else if (max == antColonyOptimization.getAntColony().getBestFitness()) System.out.print((Configuration.instance.maximumCapacity - antColonyOptimization.getAntColony().getBestAnt().getCapacity()));
                else if (max == particleSwarmOptimization.getBestValue()) System.out.print(particleSwarmOptimization.getWeight());
                else System.out.print(simulatedAnnealing.findBestWeight());

                System.out.println();

                System.out.print("Value: ");
                if (max == geneticAlgorithm.getCurrentBestFitness()) System.out.print(geneticAlgorithm.getCurrentBestFitness());
                else if (max == antColonyOptimization.getAntColony().getBestFitness()) System.out.print(antColonyOptimization.getAntColony().getBestFitness());
                else if (max == particleSwarmOptimization.getBestValue()) System.out.print(particleSwarmOptimization.getBestValue());
                else System.out.print(simulatedAnnealing.getBestValue());
                System.out.println();
                System.out.println();

                System.exit(0);

                break;
            }

            default:
            {
                System.out.println("No algorithm detected. System closing.");
                System.exit(1);
            }
        }
    }

    /**
     * loads in user specified configuration
     */
    public static void loadConfiguration (Configuration.ALGORITHM a, Configuration.CONFIG c)
    {
        String path = "";
        switch (c)
        {
            case DEFAULT: {

                switch (a)
                {
                    case GA:
                    {
                        path += ("algorithm/ga/recommender/ga_default.xml");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String elitism = document.getElementsByTagName("elitismratio").item(0).getTextContent();
                            String crossover = document.getElementsByTagName("crossoverratio").item(0).getTextContent();
                            String mutation = document.getElementsByTagName("mutationratio").item(0).getTextContent();

                            Configuration.instance.elitismRatio = Double.parseDouble(elitism);
                            Configuration.instance.crossoverRatio = Double.parseDouble(crossover);
                            Configuration.instance.mutationRatio = Double.parseDouble(mutation);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case ACO:
                    {
                        path += ("algorithm/aco/recommender/aco_default.xml");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String decay = document.getElementsByTagName("decayfactor").item(0).getTextContent();
                            String numants = document.getElementsByTagName("numants").item(0).getTextContent();
                            String initpheromone = document.getElementsByTagName("initpheromone").item(0).getTextContent();
                            String alphaaco = document.getElementsByTagName("alphaaco").item(0).getTextContent();
                            String betaaco = document.getElementsByTagName("betaaco").item(0).getTextContent();

                            Configuration.instance.decayFactor = Double.parseDouble(decay);
                            Configuration.instance.numberOfAnts = Integer.parseInt(numants);
                            Configuration.instance.startPheromoneValue = Double.parseDouble(initpheromone);
                            Configuration.instance.alphaACO = Integer.parseInt(alphaaco);
                            Configuration.instance.betaACO = Integer.parseInt(betaaco);


                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case PSO: {
                        path += ("algorithm/pso/recommender/pso_default.xml");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String particles = document.getElementsByTagName("particles").item(0).getTextContent();
                            String c1 = document.getElementsByTagName("c1").item(0).getTextContent();
                            String c2 = document.getElementsByTagName("c2").item(0).getTextContent();
                            String inertia = document.getElementsByTagName("inertia").item(0).getTextContent();
                            String delta = document.getElementsByTagName("delta").item(0).getTextContent();
                            String vmin = document.getElementsByTagName("vmin").item(0).getTextContent();
                            String vmax = document.getElementsByTagName("vmax").item(0).getTextContent();

                            Configuration.instance.numberOfParticles = Integer.parseInt(particles);
                            Configuration.instance.C1 = Double.parseDouble(c1);
                            Configuration.instance.C2 = Double.parseDouble(c2);
                            Configuration.instance.inertiaWeight = Double.parseDouble(inertia);
                            Configuration.instance.delta = Double.parseDouble(delta);
                            Configuration.instance.vMin = Double.parseDouble(vmin);
                            Configuration.instance.vMax = Double.parseDouble(vmax);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        break;
                    }

                    case SA: {

                        path += ("algorithm/sa/recommender/sa_default.xml");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String maxtemp = document.getElementsByTagName("maxtemp").item(0).getTextContent();
                            String mintemp = document.getElementsByTagName("mintemp").item(0).getTextContent();
                            String alpha = document.getElementsByTagName("alpha").item(0).getTextContent();

                            Configuration.instance.maxTemperature = Double.parseDouble(maxtemp);
                            Configuration.instance.minTemperature = Double.parseDouble(mintemp);
                            Configuration.instance.alpha = Double.parseDouble(alpha);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        break;
                    }
                }

                break;
            }

            case BEST: {
                switch (a)
                {
                    case GA:
                    {
                        path += ("algorithm/ga/recommender/ga_best.xml");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String elitism = document.getElementsByTagName("elitismratio").item(0).getTextContent();
                            String crossover = document.getElementsByTagName("crossoverratio").item(0).getTextContent();
                            String mutation = document.getElementsByTagName("mutationratio").item(0).getTextContent();

                            Configuration.instance.elitismRatio = Double.parseDouble(elitism);
                            Configuration.instance.crossoverRatio = Double.parseDouble(crossover);
                            Configuration.instance.mutationRatio = Double.parseDouble(mutation);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case ACO:
                    {
                        path += ("algorithm/aco/recommender/aco_best.xml");
                        System.out.println("Path");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String decay = document.getElementsByTagName("decayfactor").item(0).getTextContent();
                            String numants = document.getElementsByTagName("numants").item(0).getTextContent();
                            String initpheromone = document.getElementsByTagName("initpheromone").item(0).getTextContent();
                            String alphaaco = document.getElementsByTagName("alphaaco").item(0).getTextContent();
                            String betaaco = document.getElementsByTagName("betaaco").item(0).getTextContent();

                            Configuration.instance.decayFactor = Double.parseDouble(decay);
                            Configuration.instance.numberOfAnts = Integer.parseInt(numants);
                            Configuration.instance.startPheromoneValue = Double.parseDouble(initpheromone);
                            Configuration.instance.alphaACO = Integer.parseInt(alphaaco);
                            Configuration.instance.betaACO = Integer.parseInt(betaaco);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case PSO: {
                        path += ("algorithm/pso/recommender/pso_best.xml");
                        System.out.println("Path");
                        //System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String particles = document.getElementsByTagName("particles").item(0).getTextContent();
                            String c1 = document.getElementsByTagName("c1").item(0).getTextContent();
                            String c2 = document.getElementsByTagName("c2").item(0).getTextContent();
                            String inertia = document.getElementsByTagName("inertia").item(0).getTextContent();
                            String delta = document.getElementsByTagName("delta").item(0).getTextContent();
                            String vmin = document.getElementsByTagName("vmin").item(0).getTextContent();
                            String vmax = document.getElementsByTagName("vmax").item(0).getTextContent();

                            Configuration.instance.numberOfParticles = Integer.parseInt(particles);
                            Configuration.instance.C1 = Double.parseDouble(c1);
                            Configuration.instance.C2 = Double.parseDouble(c2);
                            Configuration.instance.inertiaWeight = Double.parseDouble(inertia);
                            Configuration.instance.delta = Double.parseDouble(delta);
                            Configuration.instance.vMin = Double.parseDouble(vmin);
                            Configuration.instance.vMax = Double.parseDouble(vmax);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        break;
                    }

                    case SA:
                    {
                        path += ("algorithm/sa/recommender/sa_best.xml");
                        System.out.println("Path");
                        System.out.println(Configuration.instance.sourceDirectory.concat(path));
                        try {
                            File file = new File(Configuration.instance.sourceDirectory + path);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                                    .newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(file);
                            String maxtemp = document.getElementsByTagName("maxtemp").item(0).getTextContent();
                            String mintemp = document.getElementsByTagName("mintemp").item(0).getTextContent();
                            String alpha = document.getElementsByTagName("alpha").item(0).getTextContent();
                            Configuration.instance.maxTemperature = Double.parseDouble(maxtemp);
                            Configuration.instance.minTemperature = Double.parseDouble(mintemp);
                            Configuration.instance.alpha = Double.parseDouble(alpha);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }

            default: {
                break;
            }
        }
    }

    public static void best (Knapsack knapsack)
    {
        System.out.println("===============");
        System.out.println("Best Algorithm");
        System.out.println("===============");

        System.out.println("\n\nUsing default configurations for all algorithms");

        Configuration.instance.algorithm = Configuration.ALGORITHM.BEST;
        System.out.println("Best Algorithm");
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(knapsack);
        AntColonyOptimization antColonyOptimization = new AntColonyOptimization(knapsack);
        ParticleSwarmOptimization particleSwarmOptimization = new ParticleSwarmOptimization(knapsack);
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(knapsack);

        geneticAlgorithm.execute();
        antColonyOptimization.execute();
        particleSwarmOptimization.execute();
        simulatedAnnealing.anneal();

        // compare outputs
        double max =    DoubleStream.of(  geneticAlgorithm.getCurrentBestFitness(),
                        antColonyOptimization.getAntColony().getBestFitness(),
                        particleSwarmOptimization.getBestValue(),
                        simulatedAnnealing.getBestValue()
        ).max().getAsDouble();


        System.out.println("Best algorithm: ");

        if (max == geneticAlgorithm.getCurrentBestFitness()) System.out.print("Genetic algorithm");
        else if (max == antColonyOptimization.getAntColony().getBestFitness()) System.out.print("Ant Colony Optimization");
        else if (max == particleSwarmOptimization.getBestValue()) System.out.print("Particle Swarm Optimization");
        else System.out.print("Simulated Annealing");
        System.out.println("Weight: ");
        System.out.println("Value: ");
        System.exit(0);
    }

    /**
     * Loads knapsack data
     */
    public static void loadData (Knapsack knapsack)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(Configuration.instance.dataFilePath));
            br.readLine(); // remove comment line
            String line = "";

            while ((line = br.readLine()) != null)
            {
                String [] tokens  = line.split(Configuration.instance.dataDelimiter);
                Item item = new Item(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                knapsack.addItem(item);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}