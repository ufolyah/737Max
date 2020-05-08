package B737Max.Components;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;


/**
 * A helper class representing a Tuple of Two objects.
 * @param <K> The type of the first object.
 * @param <V> The type of the second object.
 */
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


/**
 * Wrapper for a thread to perform getDepartureFlightsByTimeWindow or getArrivalFlightsByTimeWindow.
 */
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
 <p>Flight Searching Service  </p>
 <h3 id="flight-searching-algorithm">Flight Searching Algorithm</h3>
 <h4 id="level-0-functions-provide-by-server-api-">Level 0 Functions (Provide by Server API)</h4>
 <p><b>SearchDepartureFlightsInGMTDate</b><br><b>SearchArrivalFlightsInGMTDate</b>  </p>
 <h4 id="level-1-functions">Level 1 Functions</h4>
 <p><b>SearchDepartureFlightsInTimeWindow</b> (Provided in {@link ServerAPIAdapter})<br>input the time window and the airport.<br>return all flights depart from that airport within the time window.<br>Method:  </p>
 <ul>
 <li>expand the time window to GMT date.  </li>
 <li>call SearchDepartureFlightsInGMTDate.  </li>
 <li>filter the result by the remaining seats and the accurate time window.  </li>
 </ul>
 <p><b>SearchArrivalFlightsInTimeWindow</b> (Provided in {@link ServerAPIAdapter})<br>input the time window and the airport.<br>return all flights arrive at that airport within the time window.<br>Method:  </p>
 <ul>
 <li>Identical to SearchDepartureFlightsInTimeWindow  </li>
 </ul>
 <p><b>BuildTrip</b> (Provided in {@link Trip})<br>input a set of at most 3 flights<br>return a trip if the combination of the flights are valid, otherwise throw exception<br>Method:  </p>
 <ul>
 <li>check the layover airports between every 2 adjacent flights are consistent. (the arrival airport of the former should be exactly the same as the departure airport of the latter.)  </li>
 <li>check the layover time between every 2 adjacent flights are within 0.5-4 hours.  </li>
 <li>if both checks passed, return a trip, otherwise throw exception.  </li>
 </ul>
 <h4 id="level-2-functions">Level 2 Functions</h4>
 <p><b>SearchTripsWithZeroLayover</b><br>input departure airport and time window, arrival airport and time window.<br>return trips that matching the input and has only one flight inside.<br>Method:  </p>
 <ul>
 <li>call SearchDepartureFlightsInTimeWindow  </li>
 <li>filter the results by arrival airport and time window  </li>
 <li>run BuildTrip upon each flight of the results.  </li>
 <li>return trips.  </li>
 </ul>
 <p><b>SearchTripsWithOneLayover</b><br>input departure airport and time window, arrival airport and time window.<br>return trips that matching the input and has 2 flights inside.<br>Method:  </p>
 <ul>
 <li>call SearchDepartureFlightsInTimeWindow, get results as result 1.  </li>
 <li>call SearchArrivalFlightsInTimeWindow, get results as result 2.  </li>
 <li>Find all of the possible layover airports by calculating the arrival airports of result 1 intersect the departure airports of result 2.  </li>
 <li>For every possible layover airport, choose all of the combination of flights arrives at that airport in result1 and flights depart from that airport in result 2, run BuildTrip.  </li>
 <li>return all of the valid trips return from BuildTrip.  </li>
 </ul>
 <p><b>SearchTripsWithTwoLayover</b><br>input departure airport and time window, arrival airport and time window.<br>return trips that matching the input and has 3 flights inside.<br>Method:  </p>
 <ul>
 <li>call SearchDepartureFlightsInTimeWindow, get results as result 1.  </li>
 <li>call SearchArrivalFlightsInTimeWindow, get results as result 2.  </li>
 <li>Find all of the possible first layover airports by getting the arrival airport of result 1.  </li>
 <li>Find all of the possible second layover airports by getting the departure airport of result 2.  </li>
 <li>For every possible layover airport, find a loose bound on the possible time window that another flight connect to it within the time window has the probability to build a valid layover with a flight in existing flights.  </li>
 <li>For every possible combination of the first layover airports and second layover airports where the two are not identical,  </li>
 <li>call SearchTripsWithZeroLayover between the two airposts with the loose time windows, get the result as result 3.  </li>
 <li>Build trip on all the combinations of flights from result 1 that arrive at the first airport, flights from result 2 that depart from the second airport, and flights in result 3 that connect the two airports.  </li>
 <li>return all of the valid trips return from BuildTrip.  </li>
 </ul>
 <h4 id="level-3-functions-">Level 3 functions:</h4>
 <p><b>SearchTrips</b><br>input {@link SearchConfig search config}<br>output candidate trips<br>Method:  </p>
 <ul>
 <li>Check search options. If arrival time window missing, guess one.  </li>
 <li>Call SearchTripsWithZeroLayover  </li>
 <li>Call SearchTripsWithOneLayover  </li>
 <li>Call SearchTripsWithTwoLayover  </li>
 <li>return candidate trips.  </li>
 </ul>
 <h4 id="helper-class-">Helper Class:</h4>
 <p><b>SearchContext</b><br>Save all of the temporary data of one search request, including:  </p>
 <ul>
 <li>content of request: airports, time windows, preferred seat class;  </li>
 <li>result cache of replicate server API calls: SearchDepartureFlightsInTimeWindow and SearchArrivalFlightsInTimeWindow in Level 2 functions;  </li>
 <li>current search result and its size.  </li>
 </ul>
 A search context will be passed along Level 2 and 3 functions for one search request.
 *
 */
public class SearchService {

    private SearchService(){}

    /**
     * A utility for airport-flights mapping.
     */
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
     * @param context search context for this search request
     * @throws IOException thrown when any connection error happened in the search
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
     * @param context search context for this search request
     * @throws IOException thrown when any connection error happened in the search
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
     * @param context search context for this search request
     * @throws IOException thrown when any connection error happened in the search
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
     * Calculate the great circle distance between two airports.
     * The distance is relative, meaning that the result is not multiplied by the radius of the earth.
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
     * @param config the search config
     * @return trips the search result
     * @throws IllegalArgumentException happens when config is invalid.
     * @throws IOException happens when connection error occurs.
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
