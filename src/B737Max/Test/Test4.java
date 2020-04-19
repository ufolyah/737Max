package B737Max.Test;

import B737Max.Components.*;

import java.time.LocalDate;

public class Test4 {
    public static void main(String[] args) throws Exception {
        ServiceBase.load();

        SearchConfig cfg = new SearchConfig()
                .setDepartureAirport(Airports.getInstance().selectByCode("SFO"))
                .setArrivalAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalDate(LocalDate.of(2020, 5, 23));

        Trips ret = ServiceBase.searchFlights(cfg);
        for (Trip t: ret.getTrips()) {
            System.out.println(t);
        }
        System.out.println(ret.size());
    }

}
