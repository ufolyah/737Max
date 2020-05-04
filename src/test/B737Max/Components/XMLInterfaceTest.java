package B737Max.Components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the XMLInterface component which parses and creates XML Files.
 *
 * @author Robert Dwan
 * @version 1.0.0
 * @since 2020-05-03
 */
class XMLInterfaceTest {
    
    @BeforeAll
    static void load() throws IOException
    {
        ServiceBase.load();
    }
    
    @Test
    void parseAirports() {
        System.out.println("----- Airports -----");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Airports>\n" +
                "\t<Airport Code=\"ATL\" Name=\"Hartsfield-Jackson Atlanta International\">\n" +
                "\t\t<Latitude>33.641045</Latitude>\n" +
                "\t\t<Longitude>-84.427764</Longitude>\n" +
                "\t</Airport>\n" +
                "\t<Airport Code=\"ANC\" Name=\"Ted Stevens Anchorage International Airport\">\n" +
                "\t\t<Latitude>61.176033</Latitude>\n" +
                "\t\t<Longitude>-149.990079</Longitude>\n" +
                "\t</Airport>\n" +
                "\t<Airport Code=\"BOS\" Name=\"Logan International\">\n" +
                "\t\t<Latitude>42.365855</Latitude>\n" +
                "\t\t<Longitude>-71.009624</Longitude>\n" +
                "\t</Airport>\n" +
                "</Airports>\n";
        Airport[] result = XMLInterface.parseAirports(xml);
        
        Airport atl = new Airport("ATL", "Hartsfield-Jackson Atlanta International", 33.641045, -84.427764);
        Airport anc = new Airport("ANC", "Ted Stevens Anchorage International Airport", 61.176033, -149.990079);
        Airport bos = new Airport("BOS", "Logan International", 42.365855, -71.009624);

        Airport[] expected = {atl, anc, bos};
        
        assertArrayEquals(expected, result);
        
        for (Airport a : result) {
            System.out.println(a);
        }
    }

    @Test
    void parseAirplanes() {
        System.out.println("----- Airplanes -----");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Airplanes>\n" +
                "\t<Airplane Manufacturer=\"Airbus\" Model=\"A310\">\n" +
                "\t\t<FirstClassSeats>24</FirstClassSeats>\n" +
                "\t\t<CoachSeats>200</CoachSeats>\n" +
                "\t</Airplane>\n" +
                "\t<Airplane Manufacturer=\"Airbus\" Model=\"A320\">\n" +
                "\t\t<FirstClassSeats>12</FirstClassSeats>\n" +
                "\t\t<CoachSeats>124</CoachSeats>\n" +
                "\t</Airplane>\n" +
                "\t<Airplane Manufacturer=\"Airbus\" Model=\"A330\">\n" +
                "\t\t<FirstClassSeats>42</FirstClassSeats>\n" +
                "\t\t<CoachSeats>375</CoachSeats>\n" +
                "\t</Airplane>\n" +
                "</Airplanes>\n";
        Airplane[] result = XMLInterface.parseAirplanes(xml);
        
        Airplane a130 = new Airplane("Airbus", "A310", 24, 200);
        Airplane a320 = new Airplane("Airbus", "A320", 12, 124);
        Airplane a330 = new Airplane("Airbus", "A330", 42, 375);
        
        Airplane[] expected = {a130, a320, a330};
        
        assertArrayEquals(expected, result);
        
        for(Airplane a : result) {
            System.out.println(a);
        }
    }

    @Test
    void parseFlights() throws IOException{
        System.out.println("----- Departing Flights -----");
        ServiceBase.load();
        String xmlDeparting = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Flights>\n" +
                "\t<Flight Airplane=\"757\" FlightTime=\"167\" Number=\"2751\">\n" +
                "\t\t<Departure>\n" +
                "\t\t\t<Code>BOS</Code>\n" +
                "\t\t\t<Time>2020 May 05 00:28 GMT</Time>\n" +
                "\t\t</Departure>\n" +
                "\t\t<Arrival>\n" +
                "\t\t\t<Code>FLL</Code>\n" +
                "\t\t\t<Time>2020 May 05 03:15 GMT</Time>\n" +
                "\t\t</Arrival>\n" +
                "\t\t<Seating>\n" +
                "\t\t\t<FirstClass Price=\"$396.33\">20</FirstClass>\n" +
                "\t\t\t<Coach Price=\"$74.60\">22</Coach>\n" +
                "\t\t</Seating>\n" +
                "\t</Flight>\n" +
                "</Flights>\n";
        Flight[] departing = XMLInterface.parseFlights(xmlDeparting);
        
        for(Flight f : departing) {
            System.out.println(f);
        }
    
        System.out.println("\n----- Arriving Flights -----");
        String xmlArriving = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Flights>\n" +
                "\t<Flight Airplane=\"767\" FlightTime=\"1001\" Number=\"1380\">\n" +
                "\t\t<Departure>\n" +
                "\t\t\t<Code>AUS</Code>\n" +
                "\t\t\t<Time>2020 May 04 19:24 GMT</Time>\n" +
                "\t\t</Departure>\n" +
                "\t\t<Arrival>\n" +
                "\t\t\t<Code>BOS</Code>\n" +
                "\t\t\t<Time>2020 May 05 12:05 GMT</Time>\n" +
                "\t\t</Arrival>\n" +
                "\t\t<Seating>\n" +
                "\t\t\t<FirstClass Price=\"$728.76\">65</FirstClass>\n" +
                "\t\t\t<Coach Price=\"$378.96\">189</Coach>\n" +
                "\t\t</Seating>\n" +
                "\t</Flight>\n" +
                "</Flights>\n";
        Flight[] arriving = XMLInterface.parseFlights(xmlArriving);
        
        for(Flight f : arriving) {
            System.out.println(f);
        }
    }

    @Test
    void buildReservations() {
        Trips instance = new Trips();
        ZonedDateTime t1 = ZonedDateTime.of(2019, 5, 5, 0, 28, 0, 0, ZoneId.systemDefault());
        ZonedDateTime t2 = ZonedDateTime.of(2019, 5, 5, 3, 15, 0, 0, ZoneId.systemDefault());
        instance.addTrip(new Trip(
                new Flight (
                        t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("BOS"), Airports.getInstance().selectByCode("FLL"),
                        "2751", new Airplane("Airbus", "A310", 24, 200),
                        12, 50, BigDecimal.valueOf(Math.random()*100), BigDecimal.valueOf(Math.random() * 100)
                ), SeatClass.COACH
        ));
        
        String result = XMLInterface.buildReservations(instance.getTrips());
        
        String expected = "<Flights><Flight number=\"2751\" seating=\"Coach\" /></Flights>";
        
        assertEquals(expected, result);
    }
    
    @Test
    void remainingSeatsCalculation() {
        // Load Test Airplane to list of airplanes
        String planeXMl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Airplanes>\n" +
                "\t<Airplane Manufacturer=\"A\" Model=\"A\">\n" +
                "\t\t<FirstClassSeats>24</FirstClassSeats>\n" +
                "\t\t<CoachSeats>200</CoachSeats>\n" +
                "\t</Airplane>\n" +
                "</Airplanes>";
        Airplane[] p1 = XMLInterface.parseAirplanes(planeXMl);
        Airplanes.getInstance().setList(p1);
        
        // Create a test Flight
        String flightXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Flights>\n" +
                "\t<Flight Airplane=\"A\" FlightTime=\"167\" Number=\"2751\">\n" +
                "\t\t<Departure>\n" +
                "\t\t\t<Code>BOS</Code>\n" +
                "\t\t\t<Time>2019 May 05 00:28 GMT</Time>\n" +
                "\t\t</Departure>\n" +
                "\t\t<Arrival>\n" +
                "\t\t\t<Code>FLL</Code>\n" +
                "\t\t\t<Time>2019 May 05 03:15 GMT</Time>\n" +
                "\t\t</Arrival>\n" +
                "\t\t<Seating>\n" +
                "\t\t\t<FirstClass Price=\"$396.33\">20</FirstClass>\n" +
                "\t\t\t<Coach Price=\"$74.60\">22</Coach>\n" +
                "\t\t</Seating>\n" +
                "\t</Flight>\n" +
                "</Flights>\n";
        Flight[] f1 = XMLInterface.parseFlights(flightXML);
        
        assertEquals(178, f1[0].getNumCoachRemained());
        assertEquals(4, f1[0].getNumFirstRemained());
    }
}