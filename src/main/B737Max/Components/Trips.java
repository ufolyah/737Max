package B737Max.Components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

/**
 * The class aggregates a number of trip. The aggregate is implemented as an HashMap.
 * @author xudufy
 * @version 2.0 2020-05-04
 * @since 2020-03-01
 */
public class Trips {

    private HashMap<String, Function<Trip, Boolean>> filters;
    private Comparator<Trip> comparator;
    private ArrayList<Trip> trips;
    private ArrayList<Trip> lastResult;  // if no filters applied, the lastResult, trips may use the same ArrayList.
    private boolean modifiedSinceLastResult;

    /**
     *
     */
    public Trips() {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        trips = new ArrayList<>();
        modifiedSinceLastResult = true;
    }

    /**
     * @param trips
     */
    public Trips(Trip[] trips) {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        this.trips = new ArrayList<>(Arrays.asList(trips));
        modifiedSinceLastResult = true;
    }

    /**
     * @param trip
     */
    public void addTrip(Trip trip) {
        this.trips.add(trip);
        modifiedSinceLastResult = true;
    }

    /**
     * @param cmp
     */
    public void sortBy(Comparator<Trip> cmp) {
        comparator = cmp;
        modifiedSinceLastResult = true;
    }

    /**
     * @param id
     * @param flt
     */
    public void filterBy(String id, Function<Trip, Boolean> flt) {
        filters.put(id, flt);
        modifiedSinceLastResult = true;
    }

    /**
     * @param id
     */
    public void removeFilter(String id) {
        filters.remove(id);
        modifiedSinceLastResult = true;
    }

    /**
     *
     */
    public void resetFilter() {
        filters.clear();
        modifiedSinceLastResult = true;
    }

    /**
     * @return
     */
    public Trip[] getTrips() {
        if (!modifiedSinceLastResult) {
            return lastResult.toArray(new Trip[0]);
        }
        ArrayList<Trip> result;
        if (filters.size()==0) {
            result = trips; // if no filters applied, the result, lastResult, trips will use the same ArrayList.
        } else {
            result = new ArrayList<Trip>();
            for (Trip t: trips) {
                boolean passed = true;
                for (Function<Trip, Boolean> f: filters.values()) {
                    if (!f.apply(t)) {
                        passed = false;
                        break;
                    }
                }
                if (passed) {
                    result.add(t);
                }
            }
        }
        if (comparator!=null) {
            result.sort(comparator);
        }
        lastResult = result;
        modifiedSinceLastResult = false;
        return result.toArray(new Trip[0]);
    }

    /**
     * @return
     */
    public int size() {
        return trips.size();
    }
}
