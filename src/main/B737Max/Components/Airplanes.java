package B737Max.Components;

import java.util.HashMap;

/**
 *
 * This class aggregates a number of Airplanes. The aggregate is implemented as an TreeMap
 *
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
 */
public class Airplanes {
    private static Airplanes instance=null;
    private HashMap<String, Airplane> modelMap;

    private Airplanes() {
        modelMap=new HashMap<>();
    }

    /**
     * @return
     */
    public static Airplanes getInstance() {
        if (instance==null) {
            instance = new Airplanes();
        }
        return instance;
    }

    /**
     * set a list of airplanes
     * @param airplanes
     */
    public void setList(Airplane[] airplanes) {
        modelMap.clear();
        for (Airplane p:airplanes) {
            modelMap.put(p.getModel(), p);
        }
    }

    /**
     *
     * @return
     */
    public Airplane[] getList() {
        return modelMap.values().toArray(new Airplane[0]);
    }

    /**
     * using model to get relevant airplane's code
     * @param code
     * @return
     */
    public Airplane selectByModel(String code) {
        return modelMap.get(code);
    }
}
