package B737Max.Components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the ServiceAPIAdapter class which interacts with the SystemServer vis HTTP.
 *
 * @author Robert Dwan
 * @version 1.0.0
 * @since 2020-05-04
 */
class ServerAPIAdapterTest {
    static ServerAPIAdapter instance;
    
    @BeforeEach
    void setup() throws IOException
    {
        instance = ServerAPIAdapter.getInstance();
        Airplanes.getInstance().setList(new Airplane[0]);
        Airports.getInstance().setList(new Airport[0]);
    }
    
    @Test
    void httpGet() throws IOException{
        // GET request for Airplanes
        System.out.println("----- GET Airplanes -----");
        assertTrue(Airplanes.getInstance().getList().length == 0);
        instance.getAirplanes();
        assertTrue(Airplanes.getInstance().getList().length > 0);
        
        // GET request for Airports
        System.out.println("----- GET Airports -----");
        assertTrue(Airports.getInstance().getList().length == 0);
        instance.getAirports();
        assertTrue(Airports.getInstance().getList().length > 0);
        
        // GET request for departing flights
        System.out.println("----- GET Departing Flights -----");
        Flight[] departingFlights = instance.getDepartureFlights(Airports.getInstance().selectByCode("BOS"),
                LocalDate.of(2020, 5, 10));
        assertTrue(departingFlights.length > 0);
    
        // GET request for arriving flights
        System.out.println("----- GET Arriving Flights -----");
        Flight[] arrivingFlights = instance.getArrivalFlights(Airports.getInstance().selectByCode("BOS"),
                LocalDate.of(2020, 5, 10));
        assertTrue(arrivingFlights.length > 0);
    
        // Reset DB
        System.out.println("----- Reset DB -----");
        instance.reset();
    }

    @Test
    void httpPost() throws IOException{
        // LocK DB
        System.out.println("----- Lock DB -----");
        instance.lock();
        
        // Make a reservation
        System.out.println("----- Resrvation HTTP POST -----");
        instance.getAirports();
        instance.getAirplanes();
        Flight[] departingFlights = instance.getDepartureFlights(Airports.getInstance().selectByCode("BOS"),
                LocalDate.of(2020, 5, 10));
        Trips trips = new Trips();
        trips.addTrip(new Trip(departingFlights[0], SeatClass.COACH));
        instance.reserve(trips.getTrips());
        
        // Unlock DB
        System.out.println("----- Unlock DB -----");
        instance.unlock();
    }
}