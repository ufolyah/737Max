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
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 */
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

    /**
     * @param xml
     * @return
     */
    public static Airplane[] parseAirplanes(String xml) {
        Document doc = buildDomDoc(xml);
        if (doc==null) {
            return new Airplane[0];
        }
        NodeList airplanesDom = doc.getElementsByTagName("Airplane");
        Airplane[] ans = new Airplane[airplanesDom.getLength()];
        for (int i = 0; i<airplanesDom.getLength(); i++) {
            Element airplaneDom = (Element)airplanesDom.item(i);
            Airplane air = new Airplane(
                    airplaneDom.getAttribute("Manufacturer"),
                    airplaneDom.getAttribute("Model"),
                    Integer.parseInt(getOnlyChildTextByTagName(airplaneDom, "FirstClassSeats")),
                    Integer.parseInt(getOnlyChildTextByTagName(airplaneDom, "CoachSeats"))
            );
            ans[i] = air;
        }
        return ans;
    }

    /**
     * @param xml
     * @return
     */
    public static Flight[] parseFlights(String xml) {
        Document doc = buildDomDoc(xml);
        if (doc == null) {
            return new Flight[0];
        }

        NodeList flightsDom = doc.getElementsByTagName("Flight");
        ArrayList<Flight> ans = new ArrayList<Flight>();
        for (int i=0; i<flightsDom.getLength(); i++) {
            Element flightDom = (Element)flightsDom.item(i);

            Airplane airplane = Airplanes.getInstance().selectByModel(
                    flightDom.getAttribute("Airplane")
            );
            Duration flightTime = Duration.ofMinutes(
                    Integer.parseInt(flightDom.getAttribute("FlightTime"))
            );
            String flightNum = flightDom.getAttribute("Number");

            Element departDom = (Element)flightDom.getElementsByTagName("Departure").item(0);
            Airport departureAirport = Airports.getInstance().selectByCode(
              getOnlyChildTextByTagName(departDom, "Code")
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm z");
            ZonedDateTime departureTime = ZonedDateTime.parse(
                    getOnlyChildTextByTagName(departDom,"Time"),
                    formatter
            );

            Element arrivalDom = (Element)flightDom.getElementsByTagName("Arrival").item(0);
            Airport arrivalAirport = Airports.getInstance().selectByCode(
                    getOnlyChildTextByTagName(arrivalDom, "Code")
            );
            ZonedDateTime arrivalTime = ZonedDateTime.parse(
                    getOnlyChildTextByTagName(arrivalDom,"Time"),
                    formatter
            );

            Element firstDom = (Element)flightDom.getElementsByTagName("FirstClass").item(0);
            BigDecimal firstPrice = new BigDecimal(
                    firstDom.getAttribute("Price").replaceAll("[$,]", "")
            );
            int firstReserved = Integer.parseInt(getElementText(firstDom));
            int firstRemained = airplane.getFirstSeats() - firstReserved;

            Element coachDom = (Element)flightDom.getElementsByTagName("Coach").item(0);
            BigDecimal coachPrice = new BigDecimal(
                    coachDom.getAttribute("Price").replaceAll("[$,]", "")
            );
            int coachReserved = Integer.parseInt(getElementText(coachDom));
            int coachRemained = airplane.getCoachSeats() - coachReserved;

            try {
                Flight thisFlight = new Flight (
                        departureTime,
                        arrivalTime,
                        flightTime,
                        departureAirport,
                        arrivalAirport,
                        flightNum,
                        airplane,
                        firstRemained,
                        coachRemained,
                        firstPrice,
                        coachPrice
                );
                ans.add(thisFlight);
            } catch (IllegalArgumentException ignored) {

            }
        }

        return ans.toArray(new Flight[0]);
    }

    /**
     * @param trips
     * @return
     */
    public static String buildReservations(Trip[] trips) {
        StringBuilder s = new StringBuilder();
        s.append("<Flights>");
        for (Trip t: trips) {
            for (int i=0; i<t.getNumFlights(); i++) {
                s.append("<Flight number=\"").append(t.getFlights()[i].getFlightNo()).append("\" seating=\"").append(t.getSeatClass()[i]).append("\" />");
            }
        }
        s.append("</Flights>");
        return s.toString();
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
