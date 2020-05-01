package B737Max.Test;

import B737Max.Components.*;

import java.time.LocalDate;
import java.util.Arrays;

public class Test4 {
    public static void main(String[] args) throws Exception {
        ServiceBase.load();

        SearchConfig cfg = new SearchConfig()
                .setDepartureAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalDate(LocalDate.of(2020, 5, 23));

        Trips ret = ServiceBase.searchFlights(cfg);
        Trip[] cant = ret.getTrips();
        Trip[] imba = ret.getTrips();
        for (Trip t: cant) {
            System.out.println(t);
        }
        System.out.println(ret.size());

        System.out.println("\n---------- Reservation Test Begin ---------------");

        Trip[] t = Arrays.copyOfRange(imba, 0, 1);
        System.out.println(t[0]);
        System.out.println("\n");
        ServiceBase.reserve(t);
        Trip[] imba2 = ServiceBase.searchFlights(cfg).getTrips();
        t = Arrays.copyOfRange(imba2, 0, 1);
        System.out.println(t[0]);

    }

}
