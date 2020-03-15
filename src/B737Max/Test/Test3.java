package B737Max.Test;

import B737Max.Components.Airports;
import B737Max.Components.Flight;
import B737Max.Components.ServerInterface;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Test3 {
    public static void main(String[] args) throws Exception {
        ServerInterface service = ServerInterface.getInstance();
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
                Airports.getInstance().selectByCode("IAD"),
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