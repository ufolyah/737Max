package B737Max.Components;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class holds values pertaining to a single trip. Class member attributes
 * are composed of several flights, airports and layovers.
 *
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
 */
public class Trip {
    private int numFlights;
    private ArrayList<SeatClass> seatClass;
    private ArrayList<Flight> flights;
    private ArrayList<Layover> layovers;
    private SeatClass preferredSeatClass;

    /**
     * @param flight
     * @param preferredSeatClass
     */
    public Trip(Flight flight, SeatClass preferredSeatClass) {
        assert flight!=null;
        this.numFlights = 1;
        this.preferredSeatClass = preferredSeatClass;
        this.seatClass = new ArrayList<>();
        this.seatClass.add(flight.checkSeatClass(preferredSeatClass));
        this.flights = new ArrayList<>();
        this.flights.add(flight);
        this.layovers = new ArrayList<>();
    }

    /**
     * @param flights
     * @param preferredSeatClass
     * @throws IllegalArgumentException
     */
    public Trip(Flight[] flights, SeatClass preferredSeatClass) throws IllegalArgumentException {
        assert flights!=null;
        if (flights.length == 0 || flights.length > 3) {
            throw new IllegalArgumentException("flights number invalid");
        }
        assert flights[0] != null;

        this.preferredSeatClass = preferredSeatClass;
        this.numFlights = flights.length;
        this.seatClass = new ArrayList<>();
        this.seatClass.add(flights[0].checkSeatClass(preferredSeatClass));
        this.flights = new ArrayList<>(Arrays.asList(flights));
        this.layovers = new ArrayList<>();

        for (int i=1; i<flights.length; i++) {
            assert flights[i] != null;
            this.layovers.add(Layover.ofFlights(flights[i-1], flights[i]));
            this.seatClass.add(flights[i].checkSeatClass(preferredSeatClass));
        }

    }

    /**
     * add a flight with relevant departure airport and arrival airport.
     * One trip could only have at most 3 flights.
     *
     * @param f
     * @return
     */
    public boolean addFlight(Flight f) {
        assert f!=null;

        if (flights.size()>=3) return false;

        Layover l;
        try {
            l = Layover.ofFlights(flights.get(flights.size()-1), f);
        } catch (IllegalArgumentException e) {
            return false;
        }

        this.flights.add(f);
        this.seatClass.add(f.checkSeatClass(preferredSeatClass));
        this.layovers.add(l);
        this.numFlights = this.flights.size();

        return true;
    }

    /**
     * @return total travel time of the trip
     */
    public Duration getTravelTime() {
        return Duration.between(flights.get(0).getDepartureTime(), flights.get(flights.size()-1).getArrivalTime());
    }

    /**
     * @return departure time of the trip
     */
    public ZonedDateTime getDepartureTime() {
        return flights.get(0).getDepartureTime();
    }

    /**
     * @return arrival time of the trip
     */
    public ZonedDateTime getArrivalTime() {
        return flights.get(flights.size() - 1).getArrivalTime();
    }

    /**
     * @return total price of the trip
     */
    public BigDecimal getPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (int i = 0; i < flights.size(); i++) {
            price = price.add(flights.get(i).getPrice(seatClass.get(i)));
        }
        return price;
    }

    /**
     * @return all the flights of the trip
     */
    public Flight[] getFlights() {
        return flights.toArray(new Flight[0]).clone();
    }

    /**
     * @return all the layovers of the trip
     */
    public Layover[] getLayovers() {
        return layovers.toArray(new Layover[0]).clone();
    }

    /**
     * @return seat types of the trip
     */
    public SeatClass[] getSeatClass() {
        return seatClass.toArray(new SeatClass[0]).clone();
    }

    /**
     * @return number of flights in the trip
     */
    public int getNumFlights() {
        return numFlights;
    }

    /**
     * @return details of the trip
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Trip{\n");
        for (int i =0; i< flights.size(); i++) {
            s.append(flights.get(i).toString());
            s.append("SeatClass=").append(seatClass.get(i).toString());
            s.append("\n");
            if (i<layovers.size()) {
                s.append(layovers.get(i).toString());
                s.append("\n");
            }
        }
        s.append("TravelTime=").append(getTravelTime()).append(", ");
        s.append("Price=").append(getPrice()).append(",");
        s.append("Departure=").append(getDepartureTime()).append(", ");
        s.append("Arrival=").append(getArrivalTime()).append(", ");
        s.append("}");
        return s.toString();
    }
}
