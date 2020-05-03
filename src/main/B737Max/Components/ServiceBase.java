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
     * @throws IOException
     */
    public static void load() throws IOException {
        ServerAPIAdapter.getInstance().getAirports();
        ServerAPIAdapter.getInstance().getAirplanes();
    }

    /**
     * @param cfg
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public static Trips searchFlights(SearchConfig cfg) throws IllegalArgumentException, IOException {
        return SearchService.searchTrips(cfg);
    }

    /**
     * @param trips
     * @throws IOException
     */
    public static void reserve(Trip[] trips) throws IOException {
        ServerAPIAdapter.getInstance().lock();
        ServerAPIAdapter.getInstance().reserve(trips);
        ServerAPIAdapter.getInstance().unlock();
    }
}
