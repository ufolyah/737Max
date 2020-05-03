package B737Max.Components;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * This class set the available layover for trips
 *
 *
 *  @author xudufy
 *  @version 2.0 2020-05-03
 *  @since 2020-03-01
 */
public class Layover {
    /**
     * Airport information of the layover
     */
    public final Airport airport;

    /**
     * Arrival time and departure time of the layover
     */
    public final ZonedDateTime startTime, endTime;

    /**
     * Stopping time of the layover
     */
    public final Duration duration;

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Layover{" +
                "airport=" + airport.getCode() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }

    /**
     * @param airport
     * @param startTime
     * @param endTime
     */
    public Layover(Airport airport, ZonedDateTime startTime, ZonedDateTime endTime) {
        this.airport = airport;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
    }

    /**
     *
     * The arrival airport of the first flight should be same as the departure airport of the second flight
     *
     * @param f1
     * @param f2
     * @return
     * @throws IllegalArgumentException
     */
    public static Layover ofFlights(Flight f1, Flight f2) throws IllegalArgumentException {
        if (!f1.getArrivalAirport().equals(f2.getDepartureAirport())) {
            throw new IllegalArgumentException("layover invalid");
        }

        /** If the duration is legal    */
        Duration d = Duration.between(f1.getArrivalTime(), f2.getDepartureTime());
        if (d.getSeconds() < 30 * 60 || d.getSeconds() > 4 * 60 * 60) {
            throw new IllegalArgumentException("layover invalid");
        }

        return new Layover(f1.getArrivalAirport(), f1.getArrivalTime(), f2.getDepartureTime());
    }

}
