package algorithm.sa.recommender;

import algorithm.sa.main.SimulatedAnnealing;
import data.Knapsack;
import main.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Simulated Annealing recommender
 */

public class SimulatedAnnealingRecommender {

    public static void recommend (double quality, Knapsack k)
    {
        System.out.println("Generating recommendation...");
        SimulatedAnnealing sa = new SimulatedAnnealing(k);
        double currentQuality = 0.0;
        while (quality > currentQuality)
        {
            randomiseMaxTemp(Configuration.instance.maxTemperature - 2000.0, Configuration.instance.maxTemperature + 2000.0);
            randomiseMinTemperature(Configuration.instance.minTemperature, Configuration.instance.minTemperature + 100.0);
            randomiseAlpha(Configuration.instance.alpha, Configuration.instance.alpha + 0.01);
            sa.anneal();
            currentQuality = sa.getBestValue() / Configuration.instance.bestKnownOptimum;
            System.out.println("Quality: " + currentQuality);
        }

        saveToXML("SA-recommender-" + new Date().toString() + ".xml");
        System.out.println("Saved to file!");
    }

    public static void randomiseMaxTemp(double min, double max)
    {
        Configuration.instance.maxTemperature = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public static void randomiseMinTemperature(double min, double max)
    {
        Configuration.instance.minTemperature = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public static void randomiseAlpha(double min, double max)
    {
        Configuration.instance.alpha = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public static void saveToXML(String xml) {
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
            Element rootEle = dom.createElement("sa");

            // create data elements and place them under root
            e = dom.createElement("maxtemp");
            e.appendChild(dom.createTextNode("" + Configuration.instance.elitismRatio));
            rootEle.appendChild(e);

            e = dom.createElement("mintemp");
            e.appendChild(dom.createTextNode("" + Configuration.instance.crossoverRatio));
            rootEle.appendChild(e);

            e = dom.createElement("alpha");
            e.appendChild(dom.createTextNode("" + Configuration.instance.mutationRatio));
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
