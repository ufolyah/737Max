package B737Max.Components;

/**
 *
 * This class holds values pertaining to a single Airplane. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives.
 *
 *
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
 */
public class Airplane {
    /** Manufacturer of the airplane */
    private String manufacturer;
    /** Model of the airplane */
    private String model;
    /** Total number of firstSeat for the airplane */
    private int firstSeats;
    /** Total number of CoachSeat for the airplane */
    private int coachSeats;

    /**
     * @param manufacturer
     * @param model
     * @param firstSeats
     * @param coachSeats
     */
    public Airplane(String manufacturer, String model, int firstSeats, int coachSeats) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.firstSeats = firstSeats;
        this.coachSeats = coachSeats;
    }

    /**
     * @return
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * get a string of basic airport information
     * @return
     */
    @Override
    public String toString() {
        return "Airplane{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", firstSeats=" + firstSeats +
                ", coachSeats=" + coachSeats +
                '}';
    }

    /**
     * @return
     */
    public String getModel() {
        return model;
    }

    /**
     * @return
     */
    public int getFirstSeats() {
        return firstSeats;
    }

    /**
     * @return
     */
    public int getCoachSeats() {
        return coachSeats;
    }

}
