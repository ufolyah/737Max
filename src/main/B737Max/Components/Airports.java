package B737Max.Components;

import java.util.TreeMap;

/**
 *
 */
public class Airports {
    private static Airports instance=null;
    private TreeMap<String, Airport> codeMap;
    private TreeMap<String, Airport> nameMap;

    private Airports() {
        codeMap = new TreeMap<>();
        nameMap = new TreeMap<>();
    }

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
        return nameMap.values().toArray(new Airport[0]).clone();
    }

    /**
     *
     * @param list
     */
    public void setList(Airport[] list) {
        nameMap.clear();
        codeMap.clear();
        for (Airport a:list) {
            nameMap.put(a.getName(), a);
            codeMap.put(a.getCode(), a);
        }
    }

    /**
     * @param code
     * @return
     */
    public Airport selectByCode(String code) {
        return codeMap.get(code);
    }

    /**
     * @param name
     * @return
     */
    public Airport selectByName(String name) {
        return nameMap.get(name);
    }


}
