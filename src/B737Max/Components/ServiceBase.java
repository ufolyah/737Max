package B737Max.Components;

import java.io.IOException;

public class ServiceBase {
    public static void load() throws IOException {
        ServerAPIAdapter.getInstance().getAirports();
        ServerAPIAdapter.getInstance().getAirplanes();
    }

    public static Trips searchFlights(SearchConfig cfg) throws IllegalArgumentException, IOException {
        return SearchService.searchTrips(cfg);
    }

    public static void reserve(Trip[] trips) throws IOException {
        ServerAPIAdapter.getInstance().lock();
        ServerAPIAdapter.getInstance().reserve(trips);
        ServerAPIAdapter.getInstance().unlock();
    }
}
