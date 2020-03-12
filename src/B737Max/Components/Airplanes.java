package B737Max.Components;

public class Airplanes {
    private static Airplanes instance=null;

    private Airplanes() {}

    /**
     * @return
     */
    public static Airplanes getInstance() {
        if (instance==null) {
            instance = new Airplanes();
        }
        return instance;
    }
}
