package B737Max.Components;

public class Airports {
    private static Airports instance=null;

    private Airports() {}

    /**
     * @return
     */
    public static Airports getInstance() {
        if (instance==null) {
            instance = new Airports();
        }
        return instance;
    }

    /**
     * @return
     */
    public Airport[] getList() {
        return new Airport[0];
    }

    /**
     *
     */
    public void setList(Airport[] list) {
        return;
    }



}
