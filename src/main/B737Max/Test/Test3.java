package B737Max.Test;

import B737Max.Components.Airports;
import B737Max.Components.Flight;
import B737Max.Components.ServerAPIAdapter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

class Test3 {
    public static void main(String[] args) throws Exception {
        ServerAPIAdapter service = ServerAPIAdapter.getInstance();
        service.getAirports();
        service.getAirplanes();
        Flight[] fList = service.getDepartureFlights(
                Airports.getInstance().selectByCode("BOS"),
                LocalDate.parse("2020-05-23")
        );
        for (Flight i:fList) {
            System.out.println(i);
        }

        System.out.println("--------------------");

        fList = service.getArrivalFlights(
                Airports.getInstance().selectByCode("BOS"),
                LocalDate.parse("2020-05-23")
        );
        for (Flight i:fList) {
            System.out.println(i);
        }

        System.out.println("-------------------");

        fList = service.getArrivalFlightsByTimeWindow(
                Airports.getInstance().selectByCode("SFO"),
                ZonedDateTime.parse("2020-05-21T23:34-04:00[America/New_York]"),
                ZonedDateTime.parse("2020-05-23T03:34-04:00[America/New_York]")
        );

        for (Flight i:fList) {
            System.out.println(i);
        }

        System.out.println("-------------------");

        fList = service.getDepartureFlightsByTimeWindow(
                Airports.getInstance().selectByCode("BOS"),
                ZonedDateTime.parse("2020-05-21T11:34-04:00[America/New_York]"),
                ZonedDateTime.parse("2020-05-21T14:34-04:00[America/New_York]")
        );

        for (Flight i:fList) {
            System.out.println(i);
        }

    }
}
