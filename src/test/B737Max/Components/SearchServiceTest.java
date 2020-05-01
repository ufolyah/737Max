package B737Max.Components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SearchServiceTest {

    void checkTrip(Trip t, SearchConfig cfg) {
        Flight[] flist = t.getFlights();
        assertNotNull(flist);
        assertTrue(flist.length<=3);
        assertSame(flist[0].getDepartureAirport(), cfg.departureAirport);
        assertSame(flist[flist.length-1].getArrivalAirport(), cfg.arrivalAirport);

        SeatClass[] slist = t.getSeatClass();

        for (int i=0; i<t.getNumFlights(); i++) {
            Flight f = flist[i];
            assertTrue(f.checkAvailable());
            assertSame(f.checkSeatClass(cfg.preferredSeatClass), slist[i]);
        }

        Layover[] llist = t.getLayovers();
        for (Layover l: llist) {
            assertTrue(l.duration.compareTo(Duration.ofMinutes(30))>=0);
            assertTrue(l.duration.compareTo(Duration.ofHours(4))<=0);
        }

        if (cfg.departureDate!=null) {
            assertTrue(flist[0].getDepartureTime().isAfter(ZonedDateTime.of(cfg.departureDate, cfg.departureWindowStart, cfg.departureAirport.getTimeZone())));
            assertTrue(flist[0].getDepartureTime().isBefore(ZonedDateTime.of(cfg.departureDate, cfg.departureWindowEnd, cfg.departureAirport.getTimeZone())));
        }

        if (cfg.arrivalDate!=null) {
            assertTrue(flist[flist.length-1].getArrivalTime().isAfter(ZonedDateTime.of(cfg.arrivalDate, cfg.arrivalWindowStart, cfg.arrivalAirport.getTimeZone())));
            assertTrue(flist[flist.length-1].getArrivalTime().isBefore(ZonedDateTime.of(cfg.arrivalDate, cfg.arrivalWindowEnd, cfg.arrivalAirport.getTimeZone())));
        }

    }

    @BeforeAll
    static void load() throws IOException {
        ServiceBase.load();
    }

    static int randint(int minInclusive, int maxInclusive) {
        return (int)(Math.random()*(maxInclusive-minInclusive+1))+minInclusive;
    }

    @RepeatedTest(3)
    void searchTripsWithDepartureDate() throws IOException {
        SearchConfig cfg = new SearchConfig()
                .setDepartureDate(LocalDate.of(2020, 5, randint(20,29)))
                .setDepartureAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalAirport(Airports.getInstance().selectByCode("BOS"));

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

    @RepeatedTest(3)
    void searchTripsWithDepartureWindow() throws IOException {
        SearchConfig cfg = new SearchConfig()
                .setDepartureDate(LocalDate.of(2020, 5, randint(20,30)))
                .setDepartureAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalAirport(Airports.getInstance().selectByCode("BOS"))
                .setDepartureWindow(LocalTime.of(6,23), LocalTime.of(17,36));

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

    @RepeatedTest(3)
    void searchTripsWithArrivalDate() throws IOException {
        SearchConfig cfg = new SearchConfig()
                .setArrivalDate(LocalDate.of(2020, 5, randint(21,30)))
                .setDepartureAirport(Airports.getInstance().selectByCode("SEA"))
                .setArrivalAirport(Airports.getInstance().selectByCode("PHX"));

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

    @RepeatedTest(3)
    void searchTripsWithArrivalWindow() throws IOException {
        SearchConfig cfg = new SearchConfig()
                .setArrivalDate(LocalDate.of(2020, 5, randint(20,30)))
                .setDepartureAirport(Airports.getInstance().selectByCode("DCA"))
                .setArrivalAirport(Airports.getInstance().selectByCode("BOS"))
                .setArrivalWindow(LocalTime.of(6,23), LocalTime.of(17,36));

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

    @RepeatedTest(3)
    void searchTripsWithBothDate() throws IOException {
        int d = randint(20,29);
        int dd = randint(d, d+1);
        SearchConfig cfg = new SearchConfig()
                .setDepartureDate(LocalDate.of(2020, 5, d))
                .setArrivalDate(LocalDate.of(2020,5, dd))
                .setDepartureAirport(Airports.getInstance().selectByCode("PIT"))
                .setArrivalAirport(Airports.getInstance().selectByCode("BOS"))
                .setArrivalWindow(LocalTime.of(6,23), LocalTime.of(17,36));

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

    @RepeatedTest(3)
    void searchTripsWithBothWindow() throws IOException {
        int d = randint(20,29);
        int dd = randint(d, d+1);
        SearchConfig cfg = new SearchConfig()
                .setDepartureDate(LocalDate.of(2020, 5, d))
                .setArrivalDate(LocalDate.of(2020,5, dd))
                .setDepartureAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalAirport(Airports.getInstance().selectByCode("BOS"))
                .setArrivalWindow(LocalTime.of(0,23), LocalTime.of(17,36))
                .setDepartureWindow(LocalTime.of(6,23), LocalTime.of(23,0));

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

    @RepeatedTest(5)
    void searchTripsWithFirstClass() throws IOException {
        int d = randint(20,29);
        SearchConfig cfg = new SearchConfig()
                .setDepartureDate(LocalDate.of(2020, 5, d))
                .setDepartureAirport(Airports.getInstance().selectByCode("MCO"))
                .setArrivalAirport(Airports.getInstance().selectByCode("BOS"))
                .setPreferredSeatClass(SeatClass.FIRST);

        Trips trips = SearchService.searchTrips(cfg);
        assertTrue(trips.size()>0);
        for (Trip t:trips.getTrips()) {
            checkTrip(t, cfg);
        }
    }

}