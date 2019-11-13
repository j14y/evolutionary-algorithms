package data;

import data.Item;
import main.Configuration;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a knapsack. Consists of a set of items indexed by an id value and containing weight and value members.
 */

public class Knapsack {

    // items contains the list of possible items
    private static ArrayList<Item> items;
    // items are switched from items to itemsInSack when added to the knapsack
    ArrayList <Item> sack;
    public final int capacity = Configuration.instance.maximumCapacity;
    private int value;


    public Knapsack ()
    {

        items = new ArrayList<>();
        sack = new ArrayList<>();
    }
    public Knapsack (ArrayList<Item> i)
    {
        items = i;
        sack = new ArrayList<>();
    }

    /**
     * Initializes knapsack with data
     */
    public void init ()
    {

    }

    public void generate (Knapsack k)
    {
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            items.add(i, k.getItem(i));
        Collections.shuffle(items);
    }

    public void setItem(int i, Item item)
    {
        items.set(i, item);
    }

    public void addItem (Item ki)
    {
        items.add(ki);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int bagSize ()
    {
        return items.size();
    }

    public Item getItem (int i)
    {
        return items.get(i);
    }

    public int getWeight ()
    {
        int totalWeight = 0;
        for (Item item : items)
        {
            totalWeight += item.getWeight();
        }
        return totalWeight;
    }

    public int getValue ()
    {
        int value = 0;
        for (Item item : items)
        {
            value += item.getValue();
        }
        return value;
    }

    @Override
    public String toString ()
    {
        String toString = "";
        for (Item item : items)
        {
            toString += item.getId() + " : " + item.getWeight() + " : " + item.getValue() + "\n";
        }

        return toString;
    }

    /********************************
            NEW REPRESENTATION      *
     ********************************/

    /**
     * Adds a new items to the knapsack
     * @param index
     */
    public void add (int index)
    {
        for (Item i : items)
        {
            if (i.getId() == index)
            {
                sack.add(i);
                items.remove(i);
                break;
            }
        }

    }

    /**
     * Removes an item from the sack and adds it to the list of possible items
     * @param index
     */
    public void remove (int index)
    {
        for (Item i : sack)
        {
            if (i.getId() == index)
            {
                items.add(i);
                sack.remove(i);
                break;
            }
        }
    }

    public void removeAll (int w)
    {
        for (int i = 0; i < items.size(); i++)
        {

        }
    }
}
