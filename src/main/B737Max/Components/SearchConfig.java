package B737Max.Components;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Contains all of the parameters needed for one flight search for one-way.
 * if the arrival/departure date is null, the corresponding time window should not used, and can be any value.
 * builder design pattern used.
 */
public class SearchConfig {

    public LocalDate departureDate=null, arrivalDate=null;
    public LocalTime departureWindowStart=null, departureWindowEnd=null, arrivalWindowStart=null, arrivalWindowEnd=null;
    public Airport departureAirport=null, arrivalAirport=null;
    public SeatClass preferredSeatClass = SeatClass.COACH;

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
        if (preferredSeatClass!=null) {
            this.preferredSeatClass = preferredSeatClass;
        }
        return this;
    }

    /**Check if the config is valid for a flight search and add some default values.
     * If departure/arrival date is specified, set the default time window if it is null.
     * If the arrival/departure date is null, the corresponding time window is not used, and can be any value after the check.
     * criteria for checking:
     *  1. airports should be filled.
     *  2. departure or arrival date should be filled.
     *  3. the time window does not end before start.
     * @throws IllegalArgumentException happens if any parameter error found.
     */
    public void check() throws IllegalArgumentException {
        //TODO: check all of the variables.
        if (departureDate==null && arrivalDate==null) {
            throw new IllegalArgumentException("departure/arrival dates should not be both empty.");
        }

        if (departureAirport == null || arrivalAirport == null) {
            throw new IllegalArgumentException("Airport should not be empty.");
        }

        if (departureWindowStart == null) {
            departureWindowStart = LocalTime.MIN;
        }

        if (departureWindowEnd == null) {
            departureWindowEnd = LocalTime.MAX;
        }

        if (arrivalWindowStart == null) {
            arrivalWindowStart = LocalTime.MIN;
        }

        if (arrivalWindowEnd == null) {
            arrivalWindowEnd = LocalTime.MAX;
        }

        if (departureDate!=null && departureWindowStart.isAfter(departureWindowEnd)) {
            throw new IllegalArgumentException("departure window invalid.");
        }

        if (arrivalDate!=null && arrivalWindowStart.isAfter(arrivalWindowEnd)) {
            throw new IllegalArgumentException("arrival window invalid");
        }

        if (arrivalDate != null && departureDate != null) {
            if (arrivalDate.isBefore(departureDate)) {
                throw new IllegalArgumentException("arrival date is earlier than departure date.");
            }

            if (arrivalDate.equals(departureDate)) {
                if (departureWindowStart.isAfter(arrivalWindowEnd)) {
                    throw new IllegalArgumentException("arrival window ends before departure window starts.");
                }
            }
        }

    }

}
