package B737Max.Components;

import java.io.IOException;

/**
 *
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
 */
public class ServiceBase {
    /**
     * load the airports and airplanes from the server
     * @throws IOException throw an IOException on input error
     */
    public static void load() throws IOException {
        ServerAPIAdapter.getInstance().getAirports();
        ServerAPIAdapter.getInstance().getAirplanes();
    }

    /**
     * search flights by special criteria
     * @param cfg  search criteria
     * @return results that satisfy the search criteria
     * @throws IllegalArgumentException throw an IllegalArgumentException when search criteria is illegal
     * @throws IOException throw an IOException on input error
     */
    public static Trips searchFlights(SearchConfig cfg) throws IllegalArgumentException, IOException {
        return SearchService.searchTrips(cfg);
    }

    /**
     * lock the server database during the reservation
     * @param trips the trips that need to be reserved
     * @throws IOException throw an IOException when there is an error
     */
    public static void reserve(Trip[] trips) throws IOException {
        ServerAPIAdapter.getInstance().lock();
        ServerAPIAdapter.getInstance().reserve(trips);
        ServerAPIAdapter.getInstance().unlock();
    }
}
