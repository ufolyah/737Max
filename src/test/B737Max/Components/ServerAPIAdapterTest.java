package B737Max.Components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServerAPIAdapterTest {
    static ServerAPIAdapter instance;
    
    @BeforeEach
    void setup() throws IOException
    {
        instance = ServerAPIAdapter.getInstance();
    }
    
    @Test
    void httpGet() throws IOException{
        assertTrue(Airplanes.getInstance().getList().length == 0);
        instance.getAirplanes();
        assertTrue(Airplanes.getInstance().getList().length > 0);
        
        assertTrue(Airports.getInstance().getList().length == 0);
        instance.getAirports();
        assertTrue(Airports.getInstance().getList().length > 0);
        
        Flight[] departingFlights = instance.getDepartureFlights(Airports.getInstance().selectByCode("BOS"),
                LocalDate.of(2019, 5, 10));
        for(Flight f: departingFlights) System.out.println(f);
        assertTrue(departingFlights.length > 0);
    
        Flight[] arrivingFlights = instance.getArrivalFlights(Airports.getInstance().selectByCode("BOS"),
                LocalDate.of(2019, 5, 10));
        assertTrue(arrivingFlights.length > 0);
    }

    @Test
    void httpPost() {
    }
}