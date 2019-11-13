package algorithm.aco.recommender;


import algorithm.aco.main.AntColonyOptimization;
import data.Knapsack;
import main.Configuration;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Ant Colony Optimization recommender
 */

public class AntColonyRecommender {

    public AntColonyRecommender()
    {

    }

    public void recommend (double quality, Knapsack k)
    {
        System.out.println("Generating recommendation...");
        double currentQuality = 0.0;
        AntColonyOptimization aco = new AntColonyOptimization(k);
        while (quality > currentQuality)
        {
            randomiseNumAnts(10, 100);
            randomiseDecayFactor(0.0001, 0.05);
            randomiseInitialPheromoneValue(0.0001, 0.0005);
            randomiseAlpha(5);
            randomiseBeta(5);
            aco.execute();
            currentQuality = (double) aco.getAntColony().getBestFitness() / (double) Configuration.instance.bestKnownOptimum;

        }

        saveToXML("ACO-recommender-" + new Date().toString() + ".xml");
        System.out.println("Saved to file!");
    }

    public void randomiseNumAnts (int min, int max)
    {
        Configuration.instance.numberOfAnts = (min + Configuration.instance.randomGenerator.nextInt(max));
    }

    public void randomiseDecayFactor (double min, double max)
    {
        Configuration.instance.decayFactor = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public void randomiseInitialPheromoneValue (double min, double max)
    {
        Configuration.instance.startPheromoneValue = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public void randomiseAlpha (int max)
    {
        Configuration.instance.alphaACO = Configuration.instance.randomGenerator.nextInt(max);
    }

    public void randomiseBeta (int max)
    {
        Configuration.instance.randomGenerator.nextInt(max);
    }

    public void saveToXML(String xml) {
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement("aco");

            // create data elements and place them under root
            e = dom.createElement("decayfactor");
            e.appendChild(dom.createTextNode("" + Configuration.instance.decayFactor));
            rootEle.appendChild(e);

            e = dom.createElement("numants");
            e.appendChild(dom.createTextNode("" + Configuration.instance.numberOfAnts));
            rootEle.appendChild(e);

            e = dom.createElement("initpheromone");
            e.appendChild(dom.createTextNode("" + Configuration.instance.startPheromoneValue));
            rootEle.appendChild(e);

            e = dom.createElement("alphaaco");
            e.appendChild(dom.createTextNode("" + Configuration.instance.alphaACO));
            rootEle.appendChild(e);

            e = dom.createElement("betaaco");
            e.appendChild(dom.createTextNode("" + Configuration.instance.betaACO));
            rootEle.appendChild(e);

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(xml)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }
}
