package B737Max.Test;

import B737Max.Components.Airport;
import B737Max.Components.Airports;
import B737Max.Components.ServerInterface;

public class Test2 {
    public static void main(String[] args) throws Exception {
        ServerInterface service = ServerInterface.getInstance();
        Airports airports = Airports.getInstance();
        service.getAirports();
        Airport[] airs = airports.getList();
        for (Airport air: airs) {
            System.out.println(air);
        }
        System.out.println(airports.selectByCode("IAD"));
        System.out.println(airports.selectByCode("good"));
        System.out.println(airports.selectByName("Bradley International"));
        System.out.println(airports.selectByName("a"));
    }
}
