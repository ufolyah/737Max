package B737Max.Test;

import B737Max.Components.*;

public class Test2 {
    public static void main(String[] args) throws Exception {
        ServerAPIAdapter service = ServerAPIAdapter.getInstance();
        Airports airports = Airports.getInstance();
        service.getAirports();
        Airport[] airs = airports.getList();
        for (Airport air: airs) {
            System.out.println(air);
        }
        System.out.println(airports.selectByCode("LAS"));
        System.out.println(airports.selectByCode("good"));
        System.out.println(airports.selectByName("Bradley International"));
        System.out.println(airports.selectByName("a"));

        System.out.println("----------------------");

        service.getAirplanes();
        Airplane[] planes = Airplanes.getInstance().getList();
        for (Airplane a:planes) {
            System.out.println(a);
        }
    }
}
