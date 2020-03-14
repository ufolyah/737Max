package B737Max.Components;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLInterface {
    public static Airport[] parseAirports(String xml) {
        System.out.println(xml);
        return null;
    }

    public static Airplane[] parseAirplanes(String xml) {
        return null;
    }

    public static Flight[] parseFlights(String xml) {
        return null;
    }

    public static String buildReservations(Trip[] trips) {
        return null;
    }

    static private Document buildDomDoc (String xmlString) {
        /*
         * load the xml string into a DOM document and return the Document
         */
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputStream docStream = new ByteArrayInputStream(xmlString.getBytes());

            return docBuilder.parse(docStream);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
