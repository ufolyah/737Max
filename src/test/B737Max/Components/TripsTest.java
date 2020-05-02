package B737Max.Components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TripsTest {

    static Trips instance;

    static int randint(int minInclusive, int maxInclusive) {
        return (int)(Math.random()*(maxInclusive-minInclusive+1))+minInclusive;
    }

    @BeforeAll
    static void setUp() throws IOException {
        Airports.getInstance().setList(
                new Airport[]{new Airport("A", "A", 40, 120), new Airport("B", "B", 80, 120)}
        );
        instance = new Trips();
        for (int i=0; i<50; i++) {
            ZonedDateTime t1 = ZonedDateTime.of(2020, 5, randint(20,30), randint(0,23), randint(0,59), randint(0,59), 0, ZoneId.systemDefault());
            ZonedDateTime t2 = t1.plusSeconds(randint(1, 86400));
            instance.addTrip(new Trip(
                    new Flight (
                            t1, t2, Duration.between(t1, t2), Airports.getInstance().selectByCode("A"), Airports.getInstance().selectByCode("B"),
                            "12345", new Airplane("A", "A", 100, 100), 50, 50,
                            BigDecimal.valueOf(Math.random()*100), BigDecimal.valueOf(Math.random() * 100)
                    ), SeatClass.COACH
            ));
        }

    }

    @Test
    void sortByPrice() {
        instance.sortBy(Comparator.comparing(Trip::getPrice));
        Trip[] list = instance.getTrips();
        for (int i=1; i<list.length; i++) {
            assertTrue(list[i-1].getPrice().compareTo(list[i].getPrice()) <= 0);
        }
    }

    @Test
    void sortByTravelTime() {
        instance.sortBy(Comparator.comparing(Trip::getTravelTime));
        Trip[] list = instance.getTrips();
        for (int i=1; i<list.length; i++) {
            assertTrue(list[i-1].getTravelTime().compareTo(list[i].getTravelTime()) <= 0);
        }
    }

    @Test
    void sortByArrivalTime() {
        instance.sortBy(Comparator.comparing(Trip::getArrivalTime));
        Trip[] list = instance.getTrips();
        for (int i=1; i<list.length; i++) {
            assertTrue(list[i-1].getArrivalTime().compareTo(list[i].getArrivalTime()) <= 0);
        }
    }

    @Test
    void sortByDepartureTime() {
        instance.sortBy(Comparator.comparing(Trip::getDepartureTime));
        Trip[] list = instance.getTrips();
        for (int i=1; i<list.length; i++) {
            assertTrue(list[i-1].getDepartureTime().compareTo(list[i].getDepartureTime()) <= 0);
        }
    }

    @Test
    void filterTestGroup1() {
        Trip[] reference = instance.getTrips();

        instance.filterBy("price", (Trip t)-> t.getPrice().compareTo(BigDecimal.valueOf(50)) < 0);
        Trip[] result = instance.getTrips();
        HashSet<Trip> ret = new HashSet<>(Arrays.asList(result));

        for (Trip t: reference) {
            if (ret.contains(t)) {
                assertTrue(t.getPrice().doubleValue()<50);
            } else {
                assertFalse(t.getPrice().doubleValue()<50);
            }
        }

        instance.filterBy("travel time", (Trip t) -> t.getTravelTime().compareTo(Duration.ofHours(12)) < 0);
        Trip[] result2 = instance.getTrips();
        HashSet<Trip> ret2 = new HashSet<>(Arrays.asList(result2));

        for (Trip t: reference) {
            if (ret2.contains(t)) {
                assertTrue(t.getPrice().doubleValue()<50);
                assertTrue(t.getTravelTime().compareTo(Duration.ofHours(12)) < 0);
            } else {
                assertFalse(t.getPrice().doubleValue()<50 && t.getTravelTime().compareTo(Duration.ofHours(12)) < 0);
            }
        }

        instance.resetFilter();
        Trip[] result3 = instance.getTrips();
        assertEquals(result3.length, reference.length);
        for (int i=0; i<result3.length; i++) {
            assertEquals(result3[i], reference[i]);
        }
    }

    @Test
    void filterTestGroup2() {
        Trip[] reference = instance.getTrips();

        LocalTime six = LocalTime.of(06, 00);
        LocalTime eighteen = LocalTime.of(18, 00);

        instance.filterBy("a", (Trip t)-> t.getDepartureTime().toLocalTime().isAfter(six) && t.getDepartureTime().toLocalTime().isBefore(eighteen));
        Trip[] result = instance.getTrips();
        HashSet<Trip> ret = new HashSet<>(Arrays.asList(result));

        for (Trip t: reference) {
            if (ret.contains(t)) {
                assertTrue(t.getDepartureTime().toLocalTime().isAfter(six) && t.getDepartureTime().toLocalTime().isBefore(eighteen));
            } else {
                assertFalse(t.getDepartureTime().toLocalTime().isAfter(six) && t.getDepartureTime().toLocalTime().isBefore(eighteen));
            }
        }
        
        instance.resetFilter();
        instance.filterBy("a", (Trip t)-> t.getArrivalTime().toLocalTime().isAfter(six) && t.getArrivalTime().toLocalTime().isBefore(eighteen));
        Trip[] result2 = instance.getTrips();
        HashSet<Trip> ret2 = new HashSet<>(Arrays.asList(result2));

        for (Trip t: reference) {
            if (ret2.contains(t)) {
                assertTrue(t.getArrivalTime().toLocalTime().isAfter(six) && t.getArrivalTime().toLocalTime().isBefore(eighteen));
            } else {
                assertFalse(t.getArrivalTime().toLocalTime().isAfter(six) && t.getArrivalTime().toLocalTime().isBefore(eighteen));
            }
        }

    }

}