package B737Max.Components;

import javafx.util.Pair;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class SearchService {

    static class AirportFlightsMap extends HashMap<Airport, ArrayList<Flight>> {

        public interface KeyExtractor {
            Airport getKey(Flight f);
        }

        private AirportFlightsMap() {
            super();
        }

        public static AirportFlightsMap build(Flight[] flights, KeyExtractor ke) {
            AirportFlightsMap n = new AirportFlightsMap();
            for (Flight f : flights) {
                n.put(ke.getKey(f), f);
            }
            return n;
        }

        public void put(Airport s, Flight f) {
            if (!containsKey(s)) {
                put(s, new ArrayList<>());
            }
            get(s).add(f);
        }

    }

    static class SearchContext {
        public Airport departureAirport, arrivalAirport;
        public SeatClass preferredSeatClass;
        public ZonedDateTime departureTimeStart, departureTimeEnd, arrivalTimeStart, arrivalTimeEnd;
        public AirportFlightsMap beginningFlights = null;
        public AirportFlightsMap endingFlights = null;
        public Trips finalResult;
        public int numTripLimit=50;

        public SearchContext(SearchConfig config) throws IllegalArgumentException {
            config.check();
            this.departureAirport = config.departureAirport;
            this.arrivalAirport = config.arrivalAirport;
            this.preferredSeatClass = config.preferredSeatClass;
            if (config.departureDate!=null) {
                departureTimeStart = ZonedDateTime.of(config.departureDate, config.departureWindowStart, config.departureAirport.getTimeZone());
                departureTimeEnd = ZonedDateTime.of(config.departureDate, config.departureWindowEnd, config.departureAirport.getTimeZone());
            }
            if (config.arrivalDate!=null) {
                arrivalTimeStart = ZonedDateTime.of(config.arrivalDate, config.arrivalWindowStart, config.arrivalAirport.getTimeZone());
                arrivalTimeEnd = ZonedDateTime.of(config.arrivalDate, config.arrivalWindowEnd, config.arrivalAirport.getTimeZone());
            }
            if (config.departureDate==null) {
                assert arrivalTimeStart != null;
                departureTimeStart = arrivalTimeStart.minusDays(1);
                departureTimeEnd = arrivalTimeEnd;
            }
            if (config.arrivalDate==null) {
                assert departureTimeEnd != null;
                arrivalTimeStart = departureTimeStart;
                arrivalTimeEnd = departureTimeEnd.plusDays(1);
            }
            finalResult = new Trips();
        }

    }

    static void searchTripsWithZeroLayover(SearchContext context) throws IOException {
        if (context.beginningFlights==null) {
            Flight[] tempFlights = ServerAPIAdapter.getInstance().getDepartureFlightsByTimeWindow(context.departureAirport, context.departureTimeStart, context.departureTimeEnd);
            context.beginningFlights = AirportFlightsMap.build(tempFlights, Flight::getArrivalAirport);
        }
        for (ArrayList<Flight> arrF:context.beginningFlights.values()) {
            for (Flight f: arrF) {
                if (
                        f.getArrivalTime().isBefore(context.arrivalTimeEnd) &&
                        f.getArrivalTime().isAfter(context.arrivalTimeStart) &&
                        f.getArrivalAirport().equals(context.arrivalAirport)
                ) {
                    context.finalResult.addTrip(new Trip(f, context.preferredSeatClass));
                }
            }
        }
    }

    private static void populateFlightMaps(SearchContext context) throws IOException {
        if (context.beginningFlights==null) {
            Flight[] tempFlights = ServerAPIAdapter.getInstance().getDepartureFlightsByTimeWindow(context.departureAirport, context.departureTimeStart, context.departureTimeEnd);
            context.beginningFlights = AirportFlightsMap.build(tempFlights, Flight::getArrivalAirport);
        }
        if (context.endingFlights == null) {
            Flight[] tempFlights = ServerAPIAdapter.getInstance().getArrivalFlightsByTimeWindow(context.arrivalAirport, context.arrivalTimeStart, context.arrivalTimeEnd);
            context.endingFlights = AirportFlightsMap.build(tempFlights, Flight::getDepartureAirport);
        }
    }

    static void searchTripsWithOneLayover(SearchContext context) throws IOException {
        populateFlightMaps(context);
        for (Map.Entry<Airport, ArrayList<Flight>> e:context.beginningFlights.entrySet()) {
            ArrayList<Flight> secondLeg = context.endingFlights.get(e.getKey());
            if (secondLeg!=null) {
                for (Flight f1: e.getValue()) {
                    Trip trip = new Trip(f1, context.preferredSeatClass);
                    for (Flight f2: secondLeg) {
                        if (trip.addFlight(f2)) {
                            context.finalResult.addTrip(trip);
                            if (context.finalResult.size()>=context.numTripLimit) {
                                return;
                            }
                            trip = new Trip(f1, context.preferredSeatClass);
                        }
                    }
                }
            }
        }
    }

    static void searchTripsWithTwoLayover(SearchContext context) throws IOException {
        populateFlightMaps(context);
        ArrayList<Pair<Airport, ZonedDateTime[]>> firstLayoverDepartureRange = new ArrayList<>();

        for (Map.Entry<Airport, ArrayList<Flight>> e: context.beginningFlights.entrySet()) {
            ZonedDateTime[] range = new ZonedDateTime[2];
            range[0] = null;
            range[1] = null;
            for (Flight f: e.getValue()) {
                if (range[0] == null || range[0].isAfter(f.getArrivalTime())) {
                    range[0] = f.getArrivalTime();
                }
                if (range[1] == null || range[1].isBefore(f.getArrivalTime())) {
                    range[1] = f.getArrivalTime();
                }
            }
            assert range[0] != null;
            assert range[1] != null;
            range[0] = range[0].plusMinutes(30);
            range[1] = range[1].plusHours(4);
            firstLayoverDepartureRange.add(new Pair<>(e.getKey(), range));
        }

        firstLayoverDepartureRange.sort(Comparator.comparingDouble(
                (Pair<Airport, ZonedDateTime[]> e) -> calcGreatCircleDistance(context.departureAirport, e.getKey()) + calcGreatCircleDistance(e.getKey(), context.arrivalAirport)
        ));


        int count = 8;
        for (Pair<Airport, ZonedDateTime[]> e: firstLayoverDepartureRange) {
            count--;
            Flight[] middleLegs = ServerAPIAdapter.getInstance().getDepartureFlightsByTimeWindow(e.getKey(), e.getValue()[0], e.getValue()[1]);
            for (Flight f1: context.beginningFlights.get(e.getKey())) { // will not be null because e.key is extracted from beginningFlights
                for (Flight f2: middleLegs) {

                    ArrayList<Flight> potentialF3 = context.endingFlights.get(f2.getArrivalAirport());
                    if (potentialF3 == null) continue;
                    for (Flight f3: potentialF3) {
                        try {
                            Trip t = new Trip(new Flight[]{f1, f2, f3}, context.preferredSeatClass);
                            context.finalResult.addTrip(t);
                            if (count<0 && context.finalResult.size()>=context.numTripLimit) {
                                return;
                            }
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }
        }
    }

    static double calcGreatCircleDistance(Airport a1, Airport a2) {
        double lat1 = Math.toRadians(a1.getLatitude());
        double lat2 = Math.toRadians(a2.getLatitude());
        double lng1 = Math.toRadians(a1.getLongitude());
        double lng2 = Math.toRadians(a2.getLongitude());

        if ((lat1 == lat2) && (lng1 == lng2)) {
            return 0;
        }

        return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng1 - lng2));

    }

    public static Trips searchTrips(SearchConfig config) throws IllegalArgumentException, IOException {
        SearchContext context = new SearchContext(config);
        searchTripsWithZeroLayover(context);
        if (context.finalResult.size()>=context.numTripLimit) {
            return context.finalResult;
        }
        searchTripsWithOneLayover(context);
        if (context.finalResult.size()>=context.numTripLimit) {
            return context.finalResult;
        }
        searchTripsWithTwoLayover(context);
        return context.finalResult;
    }

}
