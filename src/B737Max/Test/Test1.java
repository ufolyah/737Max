package B737Max.Test;

import B737Max.Components.ServerInterface;

public class Test1 {
    public static void main(String[] args) throws Exception{
        ServerInterface.getInstance().getAirports();
        ServerInterface.getInstance().lock();
        ServerInterface.getInstance().unlock();
        return;
    }
}
