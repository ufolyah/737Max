package B737Max.Components;

public class Airplane {
    private String manufacturer;
    private String model;
    private int firstSeats;
    private int coachSeats;

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
