package B737Max.Components;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

//flight search config for one-way.
//builder design pattern used.
public class SearchConfig {

    public LocalDate departureDate=null, arrivalDate=null;
    public LocalTime departureWindowStart=null, departureWindowEnd=null, arrivalWindowStart=null, arrivalWindowEnd=null;
    public Airport departureAirport=null, arrivalAirport=null;
    public SeatClass preferredSeatClass = null;

    public SearchConfig setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public SearchConfig setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
        return this;
    }

    public SearchConfig setDepartureWindow(LocalTime departureWindowStart, LocalTime departureWindowEnd) {
        this.departureWindowStart = departureWindowStart;
        this.departureWindowEnd = departureWindowEnd;
        return this;
    }

    public SearchConfig setArrivalWindow(LocalTime arrivalWindowStart, LocalTime arrivalWindowEnd) {
        this.arrivalWindowStart = arrivalWindowStart;
        this.arrivalWindowEnd = arrivalWindowEnd;
        return this;
    }

    public SearchConfig setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
        return this;
    }

    public SearchConfig setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
        return this;
    }

    public SearchConfig setPreferredSeatClass(SeatClass preferredSeatClass) {
        this.preferredSeatClass = preferredSeatClass;
        return this;
    }

    public void check() throws IllegalArgumentException {
        //TODO: check all of the variables.
    }

}
