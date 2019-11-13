package algorithm.aco.base;

import data.Item;
import data.Knapsack;
import main.Configuration;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

// pseudocode reference:
// ANT COLONY OPTIMIZATION ALGORITHM FOR THE 0-1 KNAPSACK PROBLEM

public class Ant {
    // best value
    private int value;
    // available capacity
    private int capacity;
    // current solution
    private ArrayList<Item> solution;
    // remember the best solution
    private ArrayList<Item> bestSolution;
    // items that can be added to the sack
    private ArrayList <Item> availableItems;



    public Ant (Knapsack k)
    {
        capacity = Configuration.instance.maximumCapacity;
        availableItems = k.getItems();
        solution = new ArrayList<>();
        bestSolution = new ArrayList<>();
        value = 0;
    }

    public void newRound (Knapsack k)
    {
        value = 0;
        availableItems = new ArrayList<>();
        for (int i = 0; i < k.bagSize(); i++)
        {
            availableItems.add(k.getItem(i));
        }
    }

    public void lookForWay ()
    {
        // while still capacity
        while (capacity > 0) {
            // only missing selection mechanism
            int index = Configuration.instance.randomGenerator.nextInt(availableItems.size() -1);

            // transition probability
            double sum = 0.0;
            for (int i = 0; i < availableItems.size() - 1; i++)
            {
                // pheromone multiplied by desire
                sum += Math.pow(AntColony.getPheromone(availableItems.get(i).getId()), Configuration.instance.alphaACO)
                        * Math.pow(1/availableItems.get(i).getValue(), Configuration.instance.betaACO);
            }

            double num = Math.pow(AntColony.getPheromone(availableItems.get(index).getId()), Configuration.instance.alphaACO)
                    * Math.pow(1/availableItems.get(index).getValue(), Configuration.instance.betaACO);
            double probability = num / sum;
            if (Configuration.instance.randomGenerator.nextDouble() <= probability)
            {
                Item i = availableItems.remove(index);
                solution.add(i);
                capacity -= i.getWeight();
                value += i.getValue();
                updateNeighbourhood();
            }
        }

        // randomly remove excess items from solution
        while (capacity < 0)
        {
            int index = Configuration.instance.randomGenerator.nextInt(solution.size() -1);
            Item i = solution.remove(index);
            availableItems.add(i);
            capacity += i.getWeight();
            value -= i.getValue();
        }
        // update local solution
        evaluate();
    }

    public void evaluate ()
    {
        int best = 0;
        for (int i = 0; i < solution.size(); i++)
        {
            value += solution.get(i).getValue();
        }

        if (best > value) {
            value = best;
            bestSolution = solution;
        }
    }

    public int getWeight ()
    {
        int weight = 0;
        for (int i = 0; i < bestSolution.size(); i++)
            weight += bestSolution.get(i).getWeight();
        return weight;
    }

    public void work ()
    {
        while (capacity >= 0)
        {
            // select object from knapsack
            Item s = select();
            // add to partial solution
            solution.add(s);
            // update capacity and profit
            capacity -= s.getWeight();
            value += s.getValue();
            // restrict neighbourhood
            updateNeighbourhood();
        }
    }

    public Item select ()
    {
        return null;
    }

    public void layPheromone ()
    {
        double pheromone = Configuration.instance.decayFactor / (double) value;
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
        {
            AntColony.addPheromone(i, pheromone);
        }
    }

    /**
     * Remove all items from available items list that are greater than the available capacity
     */
    public void updateNeighbourhood ()
    {
        for (int i = 0; i < availableItems.size(); i++)
        {
            if (availableItems.get(i).getWeight() > this.capacity)
            {
                availableItems.remove(i);
            }
        }
    }

    public void addToSolution (Item i)
    {
        solution.add(i);
    }

    public void printSolution ()
    {
        System.out.println(bestSolution.toString());
    }

    public ArrayList<Item> getBestSolution()
    {
        return bestSolution;
    }

    public int getCapacity ()
    {
        return capacity;
    }

    public int getValue ()
    {
        return value;
    }

}