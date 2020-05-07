package B737Max.Components;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * This class holds values pertaining to a single Flight. Class member attributes
 * are from Class Airport, Airplane and the same as defined by the CS509 server API
 *
 *
 *  @author xudufy
 *  @version 2.0 2020-05-03
 *  @since 2020-03-01
 */
public class Flight {
    private ZonedDateTime departureTime, arrivalTime;
    private Duration travelTime;
    private Airport departureAirport, arrivalAirport;
    private String flightNo;
    private Airplane airplane;
    private int numFirstRemained, numCoachRemained;
    private BigDecimal firstPrice, coachPrice;

    /**
     * get a string of basic flight information
     * @return basic flight information including departureTime, arrivalTime, travelTime,departureAirport,
     * arrivalAirport,flightNo,airplane,number of FirstRemained, number of CoachRemained, price of FirstSeat,
     * price of coachSeat
     */
    @Override
    public String toString() {
        return "Flight{" +
                "departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", travelTime=" + travelTime +
                ", departureAirport=" + departureAirport.getCode() +
                ", arrivalAirport=" + arrivalAirport.getCode() +
                ", \nflightNo='" + flightNo + '\'' +
                ", airplane=" + airplane.getModel() +
                ", numFirstRemained=" + numFirstRemained +
                ", numCoachRemained=" + numCoachRemained +
                ", firstPrice=" + firstPrice +
                ", coachPrice=" + coachPrice +
                '}';
    }

    /**
     * get hte departure time of the flight
     * @return the departure time
     */
    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    /**
     * get arrival time of the flight
     * @return the arrival time
     */
    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * get the whole travel time of the flight
     * @return the travel time
     */
    public Duration getTravelTime() {
        return travelTime;
    }

    /**
     * get the departure airport of the flight
     * @return the departure airport
     */
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    /**
     * get the arrival airport of the flight
     * @return the arrival airport
     */
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * get the flight number
     * @return the flight number
     */
    public String getFlightNo() {
        return flightNo;
    }

    /**
     * get the airplane of the flight
     * @return airplane
     */
    public Airplane getAirplane() {
        return airplane;
    }

    /**
     * get the number of remained firstSeat in the flight
     * @return the number of remained firstSeat
     */
    public int getNumFirstRemained() {
        return numFirstRemained;
    }

    /**
     * get the number of remained coachSeat in the flight
     * @return the number of remained coachSeat
     */
    public int getNumCoachRemained() {
        return numCoachRemained;
    }

    /**
     * get the price of firstSeat in the flight
     * @return the price of firstSeat in the flight
     */
    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    /**
     * get the price of coachSeat in the flight
     * @return price of coachSeat in the flight
     */
    public BigDecimal getCoachPrice() {
        return coachPrice;
    }

    /**
     * construct a flight instance
     * @param departureTime departure time of the flight
     * @param arrivalTime arrival time of the flight
     * @param travelTime whole travel time of the flight
     * @param departureAirport departure airport of the flight
     * @param arrivalAirport arrival airport of the flight
     * @param flightNo flight number
     * @param airplane airplane of the flight
     * @param numFirstRemained remained number of firstSeat in the flight
     * @param numCoachRemained remained number of coachSeat in the flight
     * @param firstPrice price of firstSeat of the flight
     * @param coachPrice price of coach Seat  of the flight
     * @throws IllegalArgumentException throw a warning when there is not available seat in both firstClass and coachClass
     */
    public Flight(ZonedDateTime departureTime, ZonedDateTime arrivalTime, Duration travelTime, Airport departureAirport,
                  Airport arrivalAirport, String flightNo, Airplane airplane, int numFirstRemained,
                  int numCoachRemained, BigDecimal firstPrice, BigDecimal coachPrice) throws IllegalArgumentException {
        this.departureTime = departureTime.withZoneSameInstant(departureAirport.getTimeZone());
        this.arrivalTime = arrivalTime.withZoneSameInstant(arrivalAirport.getTimeZone());
        this.travelTime = travelTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.flightNo = flightNo;
        this.airplane = airplane;
        this.numFirstRemained = numFirstRemained;
        this.numCoachRemained = numCoachRemained;
        this.firstPrice = firstPrice;
        this.coachPrice = coachPrice;
        if (this.numFirstRemained<=0 && this.numCoachRemained<=0) {
            throw new IllegalArgumentException("No Seats available on this flight.");
        }
    }

    /**
     * Is there available seats
     * @return whether there is available seat
     */
    public boolean checkAvailable() {
        return numFirstRemained>0 || numCoachRemained>0;
    }

    /**
     * Return the available classes
     * @param seat seatClass
     * @return the available classes
     */
    public SeatClass checkSeatClass(SeatClass seat) {
        if (numFirstRemained>0 && numCoachRemained>0) {
            return seat;
        }

        if (numFirstRemained>0) {
            return SeatClass.FIRST;
        }

        return SeatClass.COACH;
    }

    /**
     * Return the single price of available Seat type
     * @param seat SeatClass
     * @return the price of corresponding class
     */
    public BigDecimal getPrice(SeatClass seat) {
        if (seat==SeatClass.FIRST) {
            return firstPrice;
        }
        return coachPrice;
    }
}
