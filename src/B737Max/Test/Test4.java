package B737Max.Test;

import B737Max.Components.*;

import java.time.LocalDate;

public class Test4 {
    public static void main(String[] args) throws Exception {
        ServerAPIAdapter.getInstance().getAirplanes();
        ServerAPIAdapter.getInstance().getAirports();

        SearchConfig cfg = new SearchConfig()
                .setDepartureAirport(Airports.getInstance().selectByCode("BOS"))
                .setArrivalAirport(Airports.getInstance().selectByCode("SFO"))
                .setArrivalDate(LocalDate.of(2020, 5, 23));

        Trips ret = SearchService.searchTrips(cfg);
        for (Trip t: ret.getTrips()) {
            System.out.println(t);
        }
        System.out.println(ret.size());
    }

}
