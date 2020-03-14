package B737Max.Components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLInterface {
    /**
     * @param xml
     * @return
     */
    public static Airport[] parseAirports(String xml) {
        Document doc = buildDomDoc(xml);
        if (doc==null) {
            return new Airport[0];
        }
        NodeList airportsDom = doc.getElementsByTagName("Airport");
        Airport[] ans = new Airport[airportsDom.getLength()];
        for (int i=0; i<airportsDom.getLength(); i++) {
            Element airportDom = (Element)airportsDom.item(i);
            String code = airportDom.getAttribute("Code");
            String name = airportDom.getAttribute("Name");
            double lat = Double.parseDouble(getOnlyChildTextByTagName(airportDom, "Latitude"));
            double lng = Double.parseDouble(getOnlyChildTextByTagName(airportDom, "Longitude"));
            Airport airport = new Airport(code,name,lat,lng);
            ans[i] = airport;
        }
        return ans;
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
            System.out.println(String.format("build doc failed.\n%s\n-----\n", xmlString));
            return null;
        }
    }

    private static String getOnlyChildTextByTagName(Element ele, String tag) {
        Element target = (Element) ele.getElementsByTagName(tag).item(0);
        return getElementText(target);
    }

    private static String getElementText(Element ele) {
        if (ele==null) {
            System.out.println("null Element\n");
            return "";
        }
        NodeList nodes = ele.getChildNodes();
        for (int i = 0; i<nodes.getLength(); i++) {
            Node text = nodes.item(i);
            if (text.getNodeType()== Node.TEXT_NODE) {
                return text.getNodeValue();
            }
        }
        return "";
    }
}
