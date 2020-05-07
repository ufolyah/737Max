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
    private final String manufacturer;
    /** Model of the airplane */
    private final String model;
    /** Total number of firstSeat for the airplane */
    private final int firstSeats;
    /** Total number of CoachSeat for the airplane */
    private final int coachSeats;

    /**
     * Construct an Airplane instance with manufacturer,model,firstSeats,coachSeats
     * @param manufacturer manufacturer of the airplane
     * @param model model of the airplane
     * @param firstSeats Total number of FirstSeat for the airplane
     * @param coachSeats Total number of CoachSeat for the airplane
     */
    public Airplane(String manufacturer, String model, int firstSeats, int coachSeats) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.firstSeats = firstSeats;
        this.coachSeats = coachSeats;
    }

    /**
     * get the Manufacturer of the airport
     * @return manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * get a string of basic airport information
     * @return manufacturer, model, number of firstSeats, number of coachSeats
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
     * get the model of the airplane
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * Total number of FirstSeat for the airplane
     * @return number of FirstSeat
     */
    public int getFirstSeats() {
        return firstSeats;
    }

    /**
     * Total number of CoachSeat for the airplane
     * @return number of CoachSeat
     */
    public int getCoachSeats() {
        return coachSeats;
    }

    /**
     * determine whether two airplanes are the same
     * @param obj an airplane
     * @return whether two airplanes are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Airplane other = (Airplane) obj;
        return manufacturer.equals(other.manufacturer) && model.equals(other.model) && firstSeats == other.firstSeats
                && coachSeats == other.coachSeats;
    }
}
