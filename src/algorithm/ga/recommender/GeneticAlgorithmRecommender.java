package algorithm.ga.recommender;

import algorithm.ga.main.GeneticAlgorithm;
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
 * Genetic Algorithm recommender
 */

public class GeneticAlgorithmRecommender {

    public void recommend (double quality, Knapsack k)
    {
        System.out.println("Generating recommendation...");
        double currentQuality = 0.0;
        GeneticAlgorithm ga = new GeneticAlgorithm(k);
        while (quality > currentQuality)
        {
            randomiseCrossoverRatio(0.6, 0.4);
            randomiseMutationRatio(0.001, 0.005);
            randomiseElitismRatio(1.0, 1.0);
            ga.execute();
            currentQuality = ga.getCurrentBestFitness() / Configuration.instance.bestKnownOptimum;
        }

        saveToXML("GA-recommender-" + new Date().toString() + ".xml");
        System.out.println("Saved to file!");
    }

    public void randomiseCrossoverRatio (double min, double max)
    {
        Configuration.instance.crossoverRatio = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public void randomiseMutationRatio (double min, double max)
    {
        Configuration.instance.mutationRatio = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public void randomiseElitismRatio (double min, double max)
    {
        Configuration.instance.elitismRatio = max + Configuration.instance.randomGenerator.nextDouble(true, true) * 0;
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
            Element rootEle = dom.createElement("pso");

            // create data elements and place them under root
            e = dom.createElement("elitismratio");
            e.appendChild(dom.createTextNode("" + Configuration.instance.elitismRatio));
            rootEle.appendChild(e);

            e = dom.createElement("crossoverratio");
            e.appendChild(dom.createTextNode("" + Configuration.instance.crossoverRatio));
            rootEle.appendChild(e);

            e = dom.createElement("mutationratio");
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
