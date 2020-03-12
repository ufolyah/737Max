package B737Max.Test;

import B737Max.Components.ServerInterface;

import java.io.IOException;
import java.net.URLEncoder;

public class Test1 {
    public static void main(String[] args) throws Exception{
        ServerInterface service = ServerInterface.getInstance();
        String r = "<Flight number=\"4266\" seating=\"FirstClass\"/>";
        String reserve = "<Flights>" +
                r+
                "</Flights>";
        System.out.println(service.httpGet("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem"+
                "?team=737Max&action=list&list_type=airports" ));
        System.out.println(service.httpGet("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem"+
                "?team=737Max&action=list&list_type=airplanes" ));

        System.out.println(service.httpPost("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem",
                "team=737Max&action=lockDB" ));
        try{
            System.out.println(service.httpPost("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem",
                    "team=737Max&action=buyTickets&flightData="+reserve));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(service.httpPost("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem",
                "team=737Max&action=unlockDB"));
        System.out.println(service.httpGet("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem"+
                "?team=737Max&action=resetDB"));

    }
}
