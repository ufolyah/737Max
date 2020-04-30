package B737Max.Components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Trips {
    public interface Filter {
        boolean filter(Trip t);
    }

    private HashMap<String, Filter> filters;
    private Comparator<Trip> comparator;
    private ArrayList<Trip> trips;
    private ArrayList<Trip> lastResult;  // if no filters applied, the lastResult, trips may use the same ArrayList.
    private boolean modifiedSinceLastResult;

    public Trips() {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        trips = new ArrayList<>();
        modifiedSinceLastResult = true;
    }

    public Trips(Trip[] trips) {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        this.trips = new ArrayList<>(Arrays.asList(trips));
        modifiedSinceLastResult = true;
    }

    public void addTrip(Trip trip) {
        this.trips.add(trip);
        modifiedSinceLastResult = true;
    }

    public void sortBy(Comparator<Trip> cmp) {
        comparator = cmp;
        modifiedSinceLastResult = true;
    }

    public void filterBy(String id, Filter flt) {
        filters.put(id, flt);
        modifiedSinceLastResult = true;
    }

    public void removeFilter(String id) {
        filters.remove(id);
        modifiedSinceLastResult = true;
    }

    public void resetFilter() {
        filters.clear();
        modifiedSinceLastResult = true;
    }

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
                for (Filter f: filters.values()) {
                    if (!f.filter(t)) {
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

    public int size() {
        return trips.size();
    }
}
