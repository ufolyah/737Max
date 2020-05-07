package B737Max.Test;

import B737Max.Components.*;

import java.time.LocalDate;

class Test5 {
    public static void main(String[] args) throws Exception {
        ServiceBase.load();

        SearchConfig cfg = new SearchConfig()
                .setDepartureAirport(Airports.getInstance().selectByCode("BOS"))
                .setArrivalAirport(Airports.getInstance().selectByCode("IAD"))
                .setArrivalDate(LocalDate.of(2020, 5, 23));

        Trips ret = ServiceBase.searchFlights(cfg);

        System.out.println("\n------------- filter test begin --------------");
        ret.filterBy("price", (Trip k) -> k.getPrice().doubleValue() < 20 );
        for (Trip t: ret.getTrips()) {
            System.out.println(t);
        }
        ret.removeFilter("price");
        System.out.println("\n ---------- original trips -------------");
        for (Trip t: ret.getTrips()) {
            System.out.println(t);
        }
    }

}
