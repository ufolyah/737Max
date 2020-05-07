package B737Max.Components;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;


class Pair<K, V> {
    private K k;
    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }
}

class SearchTask implements Runnable {
    private volatile Flight[] taskResult;
    private Thread thread = null;

    enum Type {
        DEPARTURE, ARRIVAL
    }

    private final Airport air;
    private final ZonedDateTime wBegin;
    private final ZonedDateTime wEnd;
    private final Type t;
    private volatile IOException err = null;

    public SearchTask(Type t, Airport air, ZonedDateTime wBegin, ZonedDateTime wEnd) {
        this.air = air;
        this.wBegin = wBegin;
        this.wEnd = wEnd;
        this.t = t;
    }


    @Override
    public void run() {
        if (t==Type.DEPARTURE) {
            try {
                taskResult = ServerAPIAdapter.getInstance().getDepartureFlightsByTimeWindow(air, wBegin, wEnd);
            } catch (IOException e) {
                err = e;
            }
        } else {
            try {
                taskResult = ServerAPIAdapter.getInstance().getArrivalFlightsByTimeWindow(air, wBegin, wEnd);
            } catch (IOException e) {
                err = e;
            }
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public Flight[] join() throws IOException {
        try {
            thread.join();
        } catch (Exception ignored) {}
        if (err!=null) {
            throw err;
        }
        return taskResult;
    }
}

/**
 *
 */
public class SearchService {

    private SearchService(){}

    static class AirportFlightsMap extends HashMap<Airport, ArrayList<Flight>> {

        interface KeyExtractor extends Function<Flight, Airport> {
        }

        private AirportFlightsMap() {
            super();
        }

        static AirportFlightsMap build(Flight[] flights, KeyExtractor keyExtractor) {
            AirportFlightsMap n = new AirportFlightsMap();
            for (Flight f : flights) {
                n.put(keyExtractor.apply(f), f);
            }
            return n;
        }

        void put(Airport s, Flight f) {
            if (!containsKey(s)) {
                put(s, new ArrayList<>());
            }
            get(s).add(f);
        }

    }

    static class SearchContext {
        Airport departureAirport, arrivalAirport;
        SeatClass preferredSeatClass;
        ZonedDateTime departureTimeStart, departureTimeEnd, arrivalTimeStart, arrivalTimeEnd;
        AirportFlightsMap beginningFlights = null;
        AirportFlightsMap endingFlights = null;
        Trips finalResult;
        int numTripLimit=100;

        SearchContext(SearchConfig config) throws IllegalArgumentException {
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

    /**
     * Search possible trips without layovers from the server satisfying the search config
     *
     * @param context
     * @throws IOException
     */
    static void searchTripsWithZeroLayover(SearchContext context) throws IOException {
        populateFlightMaps(context);
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
        SearchTask task1 = null;
        if (context.beginningFlights==null) {
            task1 = new SearchTask(SearchTask.Type.DEPARTURE, context.departureAirport, context.departureTimeStart, context.departureTimeEnd);
            task1.start();
        }
        if (context.endingFlights == null) {
            Flight[] tempFlights = ServerAPIAdapter.getInstance().getArrivalFlightsByTimeWindow(context.arrivalAirport, context.arrivalTimeStart, context.arrivalTimeEnd);
            context.endingFlights = AirportFlightsMap.build(tempFlights, Flight::getDepartureAirport);
        }
        if (task1!=null) {
            Flight[] tempFlights = task1.join();
            context.beginningFlights = AirportFlightsMap.build(tempFlights, Flight::getArrivalAirport);
        }
    }

    /**
     * Search possible trips with one layovers from the server satisfying the search config
     *
     * @param context
     * @throws IOException
     */
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

    /**
     * Search possible trips with 2 layovers from the server satisfying the search config
     * @param context
     * @throws IOException
     */
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

        ArrayList<SearchTask> tasks = new ArrayList<>();

        for (Pair<Airport, ZonedDateTime[]> e: firstLayoverDepartureRange) {
            SearchTask thisTask = new SearchTask(SearchTask.Type.DEPARTURE, e.getKey(), e.getValue()[0], e.getValue()[1]);
            tasks.add(thisTask);
            thisTask.start();
        }

        int count = 8;
        for (SearchTask task:tasks) {
            count--;
            Flight[] f2s = task.join();
            for (Flight f2: f2s) {
                for (Flight f1: context.beginningFlights.get(f2.getDepartureAirport())) {
                    ArrayList<Flight> f3s = context.endingFlights.get(f2.getArrivalAirport());
                    if (f3s!=null) {
                        for (Flight f3:f3s) {
                            try {
                                Trip t = new Trip(new Flight[]{f1, f2, f3}, context.preferredSeatClass);
                                context.finalResult.addTrip(t);
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                    }
                }
            }
            if (count<0 && context.finalResult.size()>=context.numTripLimit) {
                return;
            }
        }
    }

    /**
     *
     * @param a1 departure airport
     * @param a2 arrival airport
     * @return distance between two airports
     */
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

    /**
     *
     * Search possible trips with at most 2 layovers from the server satisfying the search config
     *
     * @param config
     * @return trips
     * @throws IllegalArgumentException
     * @throws IOException
     */
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
