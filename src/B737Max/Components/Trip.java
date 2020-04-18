package B737Max.Components;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Trip {
    private int numFlights;
    private ArrayList<SeatClass> seatClass;
    private ArrayList<Flight> flights;
    private ArrayList<Layover> layovers;
    private SeatClass preferredSeatClass;

    private static Layover getLayover(Flight f1, Flight f2) throws IllegalArgumentException {
        if (!f1.getArrivalAirport().equals(f2.getDepartureAirport())) {
            throw new IllegalArgumentException("layover invalid");
        }

        Duration d = Duration.between(f1.getArrivalTime(), f2.getDepartureTime());
        if (d.getSeconds() < 30 * 60 || d.getSeconds() > 4 * 60 * 60) {
            throw new IllegalArgumentException("layover invalid");
        }

        return new Layover(f1.getArrivalAirport(), f1.getArrivalTime(), f2.getDepartureTime());
    }

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
            this.layovers.add(getLayover(flights[i-1], flights[i]));
        }

    }

    public  boolean addFlight(Flight f) {
        assert f!=null;
        Layover l;
        try {
            l = getLayover(flights.get(flights.size()-1), f);
        } catch (IllegalArgumentException e) {
            return false;
        }

        this.flights.add(f);
        this.seatClass.add(f.checkSeatClass(preferredSeatClass));
        this.layovers.add(l);
        this.numFlights = this.flights.size();

        return true;
    }

    public Duration getTravelTime() {
        return Duration.between(flights.get(0).getDepartureTime(), flights.get(flights.size()-1).getArrivalTime());
    }

    public ZonedDateTime getDepartureTime() {
        return flights.get(0).getDepartureTime();
    }

    public ZonedDateTime getArrivalTime() {
        return flights.get(flights.size() - 1).getArrivalTime();
    }

    public BigDecimal getPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (int i = 0; i<flights.size(); i++) {
            price = price.add(flights.get(i).getPrice(seatClass.get(i)));
        }
        return price;
    }

    public Flight[] getFlights() {
        return flights.toArray(new Flight[0]).clone();
    }

    public Layover[] getLayovers() {
        return layovers.toArray(new Layover[0]).clone();
    }

    public SeatClass[] getSeatClass() {
        return seatClass.toArray(new SeatClass[0]).clone();
    }

    public int getNumFlights() {
        return numFlights;
    }

}
