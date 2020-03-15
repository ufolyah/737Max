package B737Max.Components;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerInterface {
    private final String teamName="737Max";
    private final String urlBase="http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
    private static ServerInterface instance=null;

    private ServerInterface() {}

    /**
     * @return
     */
    public static ServerInterface getInstance() {
        if (instance==null) {
            instance = new ServerInterface();
        }
        return instance;
    }

    /**
     *
     */
    public void getAirports() throws IOException {
        String xml = httpGet(urlBase+QueryFactory.getAirports(teamName));
        Airports.getInstance().setList(XMLInterface.parseAirports(xml));
    }

    /**
     * @throws IOException
     */
    public void getAirplanes() throws IOException {
        String xml = httpGet(urlBase+QueryFactory.getAirplanes(teamName));
        Airplanes.getInstance().setList(XMLInterface.parseAirplanes(xml));
    }

    enum FlightSearchMode {
        DEPARTURE, ARRIVAL
    }

    /**
     * @return
     * @throws IOException
     */
    public Trips searchFlights() throws IOException {
        return null;
    }

    public Flight[] getDepartureFlights(Airport airport, LocalDate dateInGMT) throws IOException {
        return getFlights(airport, dateInGMT, FlightSearchMode.DEPARTURE);
    }

    public Flight[] getArrivalFlights(Airport airport, LocalDate dateInGMT) throws IOException{
        return getFlights(airport, dateInGMT, FlightSearchMode.ARRIVAL);
    }

    public Flight[] getDepartureFlightsByTimeWindow(Airport airport, ZonedDateTime begin, ZonedDateTime end)
        throws IOException {
        return getFlightsByTimeWindow(airport, begin, end, FlightSearchMode.DEPARTURE);
    }

    public Flight[] getArrivalFlightsByTimeWindow(Airport airport, ZonedDateTime begin, ZonedDateTime end)
        throws IOException {
        return getFlightsByTimeWindow(airport, begin, end, FlightSearchMode.ARRIVAL);
    }

    private Flight[] getFlightsByTimeWindow(Airport airport, ZonedDateTime begin, ZonedDateTime end, FlightSearchMode mode)
            throws IOException {
        ZonedDateTime beginGMT = begin.withZoneSameInstant(ZoneId.of("GMT"));
        LocalDate beginDate = beginGMT.toLocalDate();

        ZonedDateTime endGMT = end.withZoneSameInstant(ZoneId.of("GMT"));
        LocalDate endDate = endGMT.toLocalDate().plusDays(1);

        ArrayList<Flight> rawResult = new ArrayList<>();
        for (LocalDate d = beginDate; !d.equals(endDate); d=d.plusDays(1)) {
            rawResult.addAll(
                    Arrays.asList(getFlights(airport, d, mode))
            );
        }

        ArrayList<Flight> ans = new ArrayList<>();
        for (Flight f: rawResult) {
            ZonedDateTime targetTime;

            if (mode==FlightSearchMode.DEPARTURE) {
                targetTime = f.getDepartureTime();
            } else {
                targetTime = f.getArrivalTime();
            }

            if (targetTime.isAfter(begin) && targetTime.isBefore(end)) {
                ans.add(f);
            }
        }

        return ans.toArray(new Flight[0]);
    }

    private Flight[] getFlights(Airport airport, LocalDate dateInGMT, FlightSearchMode mode) throws IOException{
        String xml;
        switch (mode) {
            case DEPARTURE:
                xml = httpGet(urlBase+QueryFactory.getDepartureFlights(teamName, airport, dateInGMT));
                break;
            case ARRIVAL:
                xml = httpGet(urlBase+QueryFactory.getArrivalFlights(teamName, airport, dateInGMT));
                break;
            default:
                throw new IOException("Search Mode should be DEPARTURE/ARRIVAL");
        }
        return XMLInterface.parseFlights(xml);
    }

    /**
     * @throws IOException
     */
    public void reset() throws IOException {
        httpGet(urlBase+QueryFactory.getReset(teamName));
    }

    /**
     * @throws IOException
     */
    public void lock() throws IOException {
        httpPost(urlBase, QueryFactory.postLock(teamName));
    }

    /**
     * @throws IOException
     */
    public void unlock() throws IOException {
        httpPost(urlBase, QueryFactory.postUnlock(teamName));
    }

    /**
     * @param trips
     * @throws IOException
     */
    public void reserve(Trip[] trips) throws IOException {
        String q = QueryFactory.postReservation(teamName, trips);
        httpPost(urlBase, q);
    }

    public String httpGet(String query) throws IOException {
        /*
         * Create an HTTP connection to the server for a GET
         * QueryFactory provides the parameter annotations for the HTTP GET query string
         */
        URL url = new URL(query);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", teamName);
        StringBuilder result = new StringBuilder();

        /*
         * If response code of SUCCESS read the XML string returned
         * line by line to build the full return string
         */
        int responseCode = connection.getResponseCode();

        if (responseCode/100!=2) {
            throw new IOException(String.valueOf(responseCode));
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }

    public String httpPost(String query, String body) throws IOException {
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", teamName);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

        connection.setDoOutput(true);
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.writeBytes(body);
        writer.flush();
        writer.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST'");
        System.out.println(("\nResponse Code : " + responseCode));

        if (responseCode/100!=2){
            throw new IOException(String.valueOf(responseCode));
        }

        StringBuilder response = new StringBuilder();


        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }


}
