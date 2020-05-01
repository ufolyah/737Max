package B737Max.Components;

import java.io.IOException;

/**
 *
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
