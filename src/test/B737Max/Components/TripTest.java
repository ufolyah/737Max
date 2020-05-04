package B737Max.Components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {
    static Flight[] flist;

    static int randint(int minInclusive, int maxInclusive) {
        return (int)(Math.random()*(maxInclusive-minInclusive+1))+minInclusive;
    }
    
    @BeforeAll
    static void setup() {
        Airports.getInstance().setList(
                new Airport[]{new Airport("A", "A", 40, 120), new Airport("B", "B", 80, 120)}
        );
        ZonedDateTime t1 = ZonedDateTime.of(2020, 5, randint(20,30), randint(0,23), randint(0,59), randint(0,59), 0, ZoneId.systemDefault());
        ZonedDateTime t2 = t1.plusSeconds(randint(1, 1000000));
        ZonedDateTime t3 = t2.plusSeconds(randint(30*60, 4*60*60));
        ZonedDateTime t4 = t3.plusSeconds(randint(1, 1000000));
        ZonedDateTime t5 = t4.plusSeconds(randint(30*60, 4*60*60));
        ZonedDateTime t6 = t5.plusSeconds(randint(1, 1000000));
        ZonedDateTime t7 = t6.plusSeconds(randint(30*60, 4*60*60));
        ZonedDateTime t8 = t7.plusSeconds(randint(1, 1000000));
        Flight f1 = new Flight(
                t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"), Airports.getInstance().selectByCode("B"),
                "12345", new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100)
        );
        Flight f2 = new Flight(
                t3, t4, Duration.between(t1, t2), Airports.getInstance().selectByCode("B"), Airports.getInstance().selectByCode("A"),
                "12345", new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100)
        );
        Flight f3 = new Flight(
                t5, t6, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"), Airports.getInstance().selectByCode("B"),
                "12345", new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100)
        );
        Flight f4 = new Flight(
                t7, t8, Duration.between(t1, t2), Airports.getInstance().selectByCode("B"), Airports.getInstance().selectByCode("A"),
                "12345", new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100)
        );

        
        flist = new Flight[]{
                f1,f2,f3,f4
        };
    }

    @Test
    void tripAddAtMost3Flights() {
        Trip t = new Trip(flist[0], SeatClass.COACH);
        t.addFlight(flist[1]);
        t.addFlight(flist[2]);
        assertFalse(t.addFlight(flist[3]));
        assertEquals(t.getFlights().length, 3);
    }

    @Test
    void tripConstructWithAtMost3Flights() {
        Trip t = new Trip(Arrays.copyOfRange(flist, 0, 3), SeatClass.COACH);
        assertNotNull(t);
        assertThrows(IllegalArgumentException.class, () -> new Trip(flist, SeatClass.COACH));
    }
    
    @Test
    void tripWithLessThan30MinuteLayover() {
        ZonedDateTime t1 = ZonedDateTime.of(2020, 5, 10, 0, 0, 0,
                0, ZoneId.systemDefault());
        ZonedDateTime t2 = t1.plusSeconds(10000);
        ZonedDateTime t3 = t2.plusMinutes(20);
        ZonedDateTime t4 = t2.plusSeconds(10000);
        Flight f1 = new Flight(t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"),
                Airports.getInstance().selectByCode("B"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
        Flight f2 = new Flight(t3, t4, Duration.between(t3, t4), Airports.getInstance().selectByCode("B"),
                Airports.getInstance().selectByCode("A"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
        
        Trip t = new Trip(f1, SeatClass.COACH);
        assertFalse(t.addFlight(f2));
    }
    
    @Test
    void tripWithOver4HourLayover() {
        ZonedDateTime t1 = ZonedDateTime.of(2020, 5, 10, 0, 0, 0,
                0, ZoneId.systemDefault());
        ZonedDateTime t2 = t1.plusSeconds(10000);
        ZonedDateTime t3 = t2.plusHours(4).plusSeconds(100);
        ZonedDateTime t4 = t2.plusSeconds(10000);
        Flight f1 = new Flight(t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"),
                Airports.getInstance().selectByCode("B"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
        Flight f2 = new Flight(t3, t4, Duration.between(t3, t4), Airports.getInstance().selectByCode("B"),
                Airports.getInstance().selectByCode("A"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
    
        Trip t = new Trip(f1, SeatClass.COACH);
        assertFalse(t.addFlight(f2));
    }
    
    @Test
    void validLayoverTime() {
        ZonedDateTime t1 = ZonedDateTime.of(2020, 5, 10, 0, 0, 0,
                0, ZoneId.systemDefault());
        ZonedDateTime t2 = t1.plusSeconds(10000);
        ZonedDateTime t3 = t2.plusMinutes(100);
        ZonedDateTime t4 = t2.plusSeconds(10000);
        Flight f1 = new Flight(t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"),
                Airports.getInstance().selectByCode("B"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
        Flight f2 = new Flight(t3, t4, Duration.between(t3, t4), Airports.getInstance().selectByCode("B"),
                Airports.getInstance().selectByCode("A"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
    
        Trip t = new Trip(f1, SeatClass.COACH);
        assertTrue(t.addFlight(f2));
    }
    
    @Test
    void valid30MinuteLayover() {
        ZonedDateTime t1 = ZonedDateTime.of(2020, 5, 10, 0, 0, 0,
                0, ZoneId.systemDefault());
        ZonedDateTime t2 = t1.plusSeconds(10000);
        ZonedDateTime t3 = t2.plusMinutes(30);
        ZonedDateTime t4 = t2.plusSeconds(10000);
        Flight f1 = new Flight(t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"),
                Airports.getInstance().selectByCode("B"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
        Flight f2 = new Flight(t3, t4, Duration.between(t3, t4), Airports.getInstance().selectByCode("B"),
                Airports.getInstance().selectByCode("A"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
    
        Trip t = new Trip(f1, SeatClass.COACH);
        assertTrue(t.addFlight(f2));
    }
    
    @Test
    void valid4HourLayover() {
        ZonedDateTime t1 = ZonedDateTime.of(2020, 5, 10, 0, 0, 0,
                0, ZoneId.systemDefault());
        ZonedDateTime t2 = t1.plusSeconds(10000);
        ZonedDateTime t3 = t2.plusHours(4);
        ZonedDateTime t4 = t2.plusSeconds(10000);
        Flight f1 = new Flight(t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"),
                Airports.getInstance().selectByCode("B"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
        Flight f2 = new Flight(t3, t4, Duration.between(t3, t4), Airports.getInstance().selectByCode("B"),
                Airports.getInstance().selectByCode("A"), "12345",
                new Airplane("A", "A", 100, 100), 50, 50,
                BigDecimal.valueOf(Math.random() * 100), BigDecimal.valueOf(Math.random() * 100));
    
        Trip t = new Trip(f1, SeatClass.COACH);
        assertTrue(t.addFlight(f2));
    }
}