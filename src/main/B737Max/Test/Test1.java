package B737Max.Test;

import B737Max.Components.ServerAPIAdapter;

import java.io.IOException;

class Test1 {
    public static void main(String[] args) throws Exception{
        ServerAPIAdapter service = ServerAPIAdapter.getInstance();
        String r = "<Flight number=\"4214\" seating=\"FirstClass\"/>";
        String reserve = "<Flights>\n" +
                r+r+r+
                "\n</Flights>\n";
        System.out.println(reserve);
        System.out.println(service.httpGet("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem"+
                "?team=737Max&action=list&list_type=airports" ));
        System.out.println(service.httpGet("http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem"+
                "?team=WorldPlaneInc&action=list&list_type=airplanes" ));

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
