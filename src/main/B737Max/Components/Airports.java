package B737Max.Components;

import java.util.TreeMap;

/**
 * This class aggregates a number of Airport. The aggregate is implemented as an TreeMap.
 *
 *
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
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
     *
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
     * set a list of airport including name and code
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
     * using airport code to get the name
     * @param code
     * @return
     */
    public Airport selectByCode(String code) {
        return codeMap.get(code);
    }

    /**
     * using airport name to get the code
     * @param name
     * @return
     */
    public Airport selectByName(String name) {
        return nameMap.get(name);
    }


}
