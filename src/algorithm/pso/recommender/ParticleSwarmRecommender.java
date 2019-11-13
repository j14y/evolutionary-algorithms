package algorithm.pso.recommender;

import algorithm.pso.main.ParticleSwarmOptimization;
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
 * Particle Swarm Optimization recommender
 */

public class ParticleSwarmRecommender {

    public void recommend (double quality, Knapsack k)
    {
        System.out.println("Generating recommendation...");
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization(k);
        double currentQuality = 0.0;

        while (quality > currentQuality)
        {
            randomiseParticles(20, 200);
            randomiseInertia(1.0, 5.0);
            randomiseC1(1.0, 4.0);
            randomiseC2(1.0, 4.0);
            randomiseVelocityExtrema(5.0);
            randomiseDelta(1.0, 5.0);
            pso.execute();
            currentQuality = pso.getBestValue() / Configuration.instance.bestKnownOptimum;
        }

        saveToXML("PSO-recommender-" + new Date().toString() + ".xml");
        System.out.println("Saved to file!");

    }

    public void randomiseParticles (int min, int max)
    {
        Configuration.instance.numberOfParticles =  min + Configuration.instance.randomGenerator.nextInt(max);
    }

    public void randomiseInertia (double min, double max)
    {
        Configuration.instance.inertiaWeight = min + Configuration.instance.randomGenerator.nextDouble() * max;
    }

    public void randomiseC1 (double min, double max)
    {
        Configuration.instance.C1 = min + Configuration.instance.randomGenerator.nextDouble() * max;;
    }

    public void randomiseC2 (double min, double max)
    {
        Configuration.instance.C2 = min + Configuration.instance.randomGenerator.nextDouble() * max;;
    }

    public void randomiseVelocityExtrema (double extrema)
    {
        Configuration.instance.vMax =  Configuration.instance.randomGenerator.nextDouble() * extrema;
        Configuration.instance.vMin = - Configuration.instance.vMax;
    }

    public void randomiseDelta (double min, double max)
    {
        Configuration.instance.delta = min + Configuration.instance.randomGenerator.nextDouble() * max;
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
            e = dom.createElement("particles");
            e.appendChild(dom.createTextNode("" + Configuration.instance.numberOfParticles));
            rootEle.appendChild(e);

            e = dom.createElement("inertia");
            e.appendChild(dom.createTextNode("" + Configuration.instance.inertiaWeight));
            rootEle.appendChild(e);

            e = dom.createElement("c1");
            e.appendChild(dom.createTextNode("" + Configuration.instance.C1));
            rootEle.appendChild(e);

            e = dom.createElement("c2");
            e.appendChild(dom.createTextNode("" + Configuration.instance.C2));
            rootEle.appendChild(e);

            e = dom.createElement("delta");
            e.appendChild(dom.createTextNode("" + Configuration.instance.delta));
            rootEle.appendChild(e);

            e = dom.createElement("vmax");
            e.appendChild(dom.createTextNode("" + Configuration.instance.vMax));
            rootEle.appendChild(e);

            e = dom.createElement("vmin");
            e.appendChild(dom.createTextNode("" + Configuration.instance.vMin));
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
