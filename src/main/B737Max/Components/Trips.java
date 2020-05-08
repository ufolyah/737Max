package B737Max.Components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Trips can handle multiple filters and one sort method. And the filter and sorting are implemented with functors.
 * <p>
 * One filter is defined by one name and a function (Trip) -&gt; bool, and only the trips returned true from the function will be selected. When multiple filters are applied, each filter can be delete independently by its name.
 * </p><p>
 * One sort method is defined by a standard Comparator&lt;Trip&gt;. For easier use, you can sort the trips by key with something like Comparator.comparing(Trip::getTravelTime)
 * </p><p>
 * The Trips is lazy evaluated, which means the list will get filtering and sorting only when you call getTrips().
 * </p><p>
 * And if you call getTrips() multiple times without change the filters and sorting, the method will do the filter and sort only in the first call and save the result for latter calls.
 * </p>
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
     * Construct a empty Trips instance.
     */
    public Trips() {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        trips = new ArrayList<>();
        modifiedSinceLastResult = true;
    }

    /**
     *
     * Construct a Trips instance with a list of Trip instances.
     * @param trips list of Trip instances
     */
    public Trips(Trip[] trips) {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        this.trips = new ArrayList<>(Arrays.asList(trips));
        modifiedSinceLastResult = true;
    }

    /**
     * Add a new trip into list
     * @param trip the new trip
     */
    public void addTrip(Trip trip) {
        this.trips.add(trip);
        modifiedSinceLastResult = true;
    }

    /**
     * Specify a comparator used for trip sorting
     * @param cmp the comparator
     */
    public void sortBy(Comparator<Trip> cmp) {
        comparator = cmp;
        modifiedSinceLastResult = true;
    }

    /**
     * Add a filter on the trips.
     * @param id id of the filter
     * @param flt the filter function
     */
    public void filterBy(String id, Function<Trip, Boolean> flt) {
        filters.put(id, flt);
        modifiedSinceLastResult = true;
    }

    /**
     * Remove the filter with the id.
     * @param id the id of the filter.
     */
    public void removeFilter(String id) {
        filters.remove(id);
        modifiedSinceLastResult = true;
    }

    /**
     * Reset the filter options
     */
    public void resetFilter() {
        filters.clear();
        modifiedSinceLastResult = true;
    }

    /**
     * get the latest trip list
     * @return latest list of trips
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
     * return the size of the list
     * @return the size of trip list
     */
    public int size() {
        return trips.size();
    }
}
