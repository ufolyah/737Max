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

    public Trips() {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        trips = new ArrayList<>();
    }

    public Trips(Trip[] trips) {
        filters = new HashMap<>();
        comparator = Comparator.comparing(Trip::getTravelTime);
        this.trips = new ArrayList<>(Arrays.asList(trips));
    }

    public void addTrip(Trip trip) {
        this.trips.add(trip);
    }

    public void sortBy(Comparator<Trip> cmp) {
        comparator = cmp;
    }

    public void filterBy(String id, Filter flt) {
        filters.put(id, flt);
    }

    public void removeFilter(String id) {
        filters.remove(id);
    }

    public void resetFilter() {
        filters.clear();
    }

    public Trip[] getTrips() {
        ArrayList<Trip> result;
        if (filters.size()==0) {
            result = (ArrayList<Trip>) trips.clone();
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
        return result.toArray(new Trip[0]);
    }

    public int size() {
        return trips.size();
    }
}
