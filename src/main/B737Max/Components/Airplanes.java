package B737Max.Components;

import java.util.HashMap;

/**
 *
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
     * @param airplanes
     */
    public void setList(Airplane[] airplanes) {
        modelMap.clear();
        for (Airplane p:airplanes) {
            modelMap.put(p.getModel(), p);
        }
    }

    /**
     * @return
     */
    public Airplane[] getList() {
        return modelMap.values().toArray(new Airplane[0]);
    }

    /**
     * @param code
     * @return
     */
    public Airplane selectByModel(String code) {
        return modelMap.get(code);
    }
}
