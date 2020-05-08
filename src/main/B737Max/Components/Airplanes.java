package B737Max.Components;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private final HashMap<String, Airplane> modelMap;
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rLock = rwl.readLock();
    private final Lock wLock = rwl.writeLock();
    private static final Lock singletonLock = new ReentrantLock();

    private Airplanes() {
        modelMap=new HashMap<>();
    }

    /**
     * Return an Airplanes instance
     * @return an Airplane
     */
    public static Airplanes getInstance() {
        singletonLock.lock();
        if (instance==null) {
            instance = new Airplanes();
        }
        singletonLock.unlock();
        return instance;
    }

    /**
     * build a hashmap based on model, so we can search the airplanes by model.
     * @param airplanes list of airplanes
     */
    public void setList(Airplane[] airplanes) {
        wLock.lock();
        modelMap.clear();
        for (Airplane p:airplanes) {
            modelMap.put(p.getModel(), p);
        }
        wLock.unlock();
    }

    /**
     * get the arraylist of airplanes
     * @return the arraylist of airplanes
     */
    public Airplane[] getList() {
        rLock.lock();
        try {
            return modelMap.values().toArray(new Airplane[0]);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * using model to get corresponding airplanes
     * @param code model of the airplane
     * @return the relevant airplane
     */
    public Airplane selectByModel(String code) {
        rLock.lock();
        try {
            return modelMap.get(code);
        } finally {
            rLock.unlock();
        }
    }
}
