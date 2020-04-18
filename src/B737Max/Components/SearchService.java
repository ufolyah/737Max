package B737Max.Components;

import java.time.ZonedDateTime;

public class SearchService {

    private static class SearchContext {
        public Airport departureAirport=null, arrivalAirport=null;
        public SeatClass preferredSeatClass = SeatClass.COACH;
        public ZonedDateTime departureTimeStart, departureTimeEnd, arrivalTimeStart, arrivalTimeEnd;
        public Flight[] beginningFlights = null;
        public Flight[] endingFlights = null;
        public Trips finalResult = null;
        public int numTrip = 0;
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

        public SearchContext(SearchConfig config, ZonedDateTime departureTimeStart, ZonedDateTime departureTimeEnd, ZonedDateTime arrivalTimeStart, ZonedDateTime arrivalTimeEnd) {
            this.departureAirport = config.departureAirport;
            this.arrivalAirport = config.arrivalAirport;
            this.preferredSeatClass = config.preferredSeatClass;
            this.departureTimeStart = departureTimeStart;
            this.departureTimeEnd = departureTimeEnd;
            this.arrivalTimeStart = arrivalTimeStart;
            this.arrivalTimeEnd = arrivalTimeEnd;
            assert !(this.arrivalAirport==null || this.departureAirport==null || this.preferredSeatClass==null);
            finalResult = new Trips();
        }
    }

    public static void searchTripsWithZeroLayover(SearchContext context) {

    }

    public static void searchTripsWithOneLayover(SearchContext context) {

    }

    public static void searchTripsWithTwoLayover(SearchContext context) {

    }

    public static Trips searchTrips(SearchConfig config) throws IllegalArgumentException {
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
