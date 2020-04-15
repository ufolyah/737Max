package B737Max.Components;

import java.time.ZonedDateTime;

class SearchContext {
    public SearchConfig config;
    public ZonedDateTime departureTimeStart, departureTimeEnd, arrivalTimeStart, arrivalTimeEnd;
    public Flight[] beginningFlights = null;
    public Flight[] endingFlights = null;
    public Trips finalResult = null;
    public int numTrip = 0;
    public int numTripLimit=50;

    public SearchContext(SearchConfig config) {
        this.config = config;
        //TODO:calc Time Windows in ZonedDateTime
        finalResult = new Trips();
    }

    public SearchContext(SearchConfig config, ZonedDateTime departureTimeStart, ZonedDateTime departureTimeEnd, ZonedDateTime arrivalTimeStart, ZonedDateTime arrivalTimeEnd) {
        this.config = config;
        this.departureTimeStart = departureTimeStart;
        this.departureTimeEnd = departureTimeEnd;
        this.arrivalTimeStart = arrivalTimeStart;
        this.arrivalTimeEnd = arrivalTimeEnd;
        finalResult = new Trips();
    }


}

public class SearchService {

    public static void searchTripsWithZeroLayover(SearchContext context) {

    }

    public static void searchTripsWithOneLayover(SearchContext context) {

    }

    public static void searchTripsWithTwoLayover(SearchContext context) {

    }

    public static Trips searchTrips(SearchConfig config) throws IllegalArgumentException {
        config.check();
        SearchContext context = new SearchContext(config);
        searchTripsWithZeroLayover(context);
        if (context.numTrip>=context.numTripLimit) {
            return context.finalResult;
        }
        searchTripsWithOneLayover(context);
        if (context.numTrip>=context.numTripLimit) {
            return context.finalResult;
        }
        searchTripsWithTwoLayover(context);
        return context.finalResult;
    }

}
