package B737Max.Components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class QueryFactory {
    /**
     * Return a query string that can be passed to HTTP URL to request list of airports
     *
     * @param teamName is the name of the team to specify the data copy on server
     * @return the query String which can be appended to URL to form HTTP GET request
     */
    public static String getAirports(String teamName) {
        return "?team=" + teamName + "&action=list&list_type=airports";
    }

    public static String getAirplanes(String teamName) {
        return "?team="+ teamName + "&action=list&list_type=airplanes";
    }

    private static String getFlights(String teamName, Airport airport, LocalDate day, String type) {
        String airportCode = airport.getCode();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String dayString = day.format(formatter);
        return String.format("?team=%s&action=list&list_type=%s&airport=%s&day=%s",
                teamName, type, airportCode, dayString);
    }

    /**
     * @param teamName
     * @param airport
     * @param day
     * @return
     */
    public static String getDepartureFlights(String teamName, Airport airport, LocalDate day) {
        return getFlights(teamName, airport, day, "departing");
    }

    /**
     * @param teamName
     * @param airport
     * @param day
     * @return
     */
    public static String getArrivalFlights(String teamName, Airport airport, LocalDate day) {
        return getFlights(teamName, airport, day, "arriving");
    }

    /**
     * @param teamName
     * @return
     */
    public static String getReset(String teamName) {
        return "?team=" + teamName + "&action=resetDB";
    }

    /**
     * Lock the server database so updates can be written
     *
     * @param teamName is the name of the team to acquire the lock
     * @return the String written to HTTP POST to lock server database
     */
    public static String postLock (String teamName) {
        return "team=" + teamName + "&action=lockDB";
    }

    /**
     * Unlock the server database after updates are written
     *
     * @param teamName is the name of the team holding the lock
     * @return the String written to the HTTP POST to unlock server database
     */
    public static String postUnlock (String teamName) {
        return "team=" + teamName + "&action=unlockDB";
    }

    public static String postReservation(String teamName, Trip[] trips) {
        String tripXml = XMLInterface.buildReservations(trips);
        return "team="+teamName+"&action=buyTickets&flightData="+tripXml;
    }

}
