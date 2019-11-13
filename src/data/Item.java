package data;

/**
 * Represents an item in the knapsack
 */

public class Item {

    int id;
    int weight;
    int value;

    public Item()
    {

    }

    public Item (int i, int w, int v)
    {
        this.id = i;
        this.weight = w;
        this.value = v;
    }

    public void setWeight (int w)
    {
        this.weight = w;
    }

    public void setId (int i)
    {
        this.id = i;
    }

    public void setValue (int v)
    {
        this.value = v;
    }

    public int getId ()
    {
        return this.id;
    }

    public double getWeight () { return this.weight; }

    public double getValue ()
    {
        return this.value;
    }
}
