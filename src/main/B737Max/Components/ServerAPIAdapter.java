package B737Max.Components;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The adaptor for server HTTP APIs
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
 */
public class ServerAPIAdapter {
    private final String teamName="737Max";
    private final String urlBase="http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
    private static ServerAPIAdapter instance=null;
    private static final Lock singletonLock = new ReentrantLock();

    private ServerAPIAdapter() {}

    /**
     * get a server API adapter instance
     * @return a server API adapter
     */
    public static ServerAPIAdapter getInstance() {
        singletonLock.lock();
        if (instance==null) {
            instance = new ServerAPIAdapter();
        }
        singletonLock.unlock();
        return instance;
    }

    /**
     * get list of airports
     * @throws IOException throw an IOException on input error
     */
    public void getAirports() throws IOException {
        String xml = httpGet(urlBase+QueryFactory.getAirports(teamName));
        Airports.getInstance().setList(XMLInterface.parseAirports(xml));
    }

    /**
     * get list of airplanes
     * @throws IOException throw an IOException on input error
     */
    public void getAirplanes() throws IOException {
        String xml = httpGet(urlBase+QueryFactory.getAirplanes(teamName));
        Airplanes.getInstance().setList(XMLInterface.parseAirplanes(xml));
    }

    enum FlightSearchMode {
        DEPARTURE, ARRIVAL
    }

    /**
     * get flights that satisfy the demand of departure airport and departure date
     * @param airport departure airport of the flight
     * @param dateInGMT local departure date of the flight
     * @return list of fights that satisfy the demand of departure airport and departure date
     * @throws IOException throw an IOException on input error
     */
    public Flight[] getDepartureFlights(Airport airport, LocalDate dateInGMT) throws IOException {
        return getFlights(airport, dateInGMT, FlightSearchMode.DEPARTURE);
    }

    /**
     * get fights that satisfy the demand of arrival airport and arrival date
     *
     * @param airport arrival airport of the flight
     * @param dateInGMT local arrival date of the flight
     * @return list of fights that satisfy the demand of arrival airport and arrival date
     * @throws IOException throw an IOException on input error
     */
    public Flight[] getArrivalFlights(Airport airport, LocalDate dateInGMT) throws IOException{
        return getFlights(airport, dateInGMT, FlightSearchMode.ARRIVAL);
    }

    /**
     * get fights that satisfy the demand of departure airport, date and time
     *
     * @param airport departure airport of the flight
     * @param begin the begin of departure time window
     * @param end the end of departure time window
     * @return list of fights that satisfy the demand of departure airport, date and time
     * @throws IOException throw an IOException on input error
     */
    public Flight[] getDepartureFlightsByTimeWindow(Airport airport, ZonedDateTime begin, ZonedDateTime end)
        throws IOException {
        return getFlightsByTimeWindow(airport, begin, end, FlightSearchMode.DEPARTURE);
    }

    /**
     * get fights that satisfy the demand of arrival airport, date and time
     *
     * @param airport arrival airport of the flight
     * @param begin the begin of arrival time window
     * @param end the end of arrival time window
     * @return list of fights that satisfy the demand of arrival airport, date and time
     * @throws IOException throw an IOException on input error
     */
    public Flight[] getArrivalFlightsByTimeWindow(Airport airport, ZonedDateTime begin, ZonedDateTime end)
        throws IOException {
        return getFlightsByTimeWindow(airport, begin, end, FlightSearchMode.ARRIVAL);
    }

    /**
     * get fights that satisfy the airport, the time window of departure or arrival
     *
     * @param airport departure of arrival airport of the flight
     * @param begin the begin of time window
     * @param end the end of time window
     * @param mode search by departure conditions or arrival conditions
     * @return list of fights that satisfy the relevant features
     * @throws IOException throw an IOException on input error
     */
    private Flight[] getFlightsByTimeWindow(Airport airport, ZonedDateTime begin, ZonedDateTime end, FlightSearchMode mode)
            throws IOException {

        if (begin.isAfter(end)) {
            return new Flight[0];
        }

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

    /**
     * get flights that satisfy the airport and local date of departure or arrival
     *
     * @param airport the airport of departure of arrival
     * @param dateInGMT the local date of departure of arrival
     * @param mode search by departure conditions or arrival conditions
     * @return list of flights that satisfy relevant features
     * @throws IOException throw an IOException on input error
     */
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
     * reset the server database
     * @throws IOException throw an IOException on input error
     */
    public void reset() throws IOException {
        httpGet(urlBase+QueryFactory.getReset(teamName));
    }

    /**
     * lock the server database
     * @throws IOException throw an IOException on input error
     */
    public void lock() throws IOException {
        httpPost(urlBase, QueryFactory.postLock(teamName));
    }

    /**
     * unlock the server database
     * @throws IOException throw an IOException on input error
     */
    public void unlock() throws IOException {
        httpPost(urlBase, QueryFactory.postUnlock(teamName));
    }

    /**
     * reserve the trips
     * @param trips trips that to be reserved
     * @throws IOException throw an IOException on input error
     */
    public void reserve(Trip[] trips) throws IOException {
        String q = QueryFactory.postReservation(teamName, trips);
        httpPost(urlBase, q);
    }

    /**
     * perform a http GET request to the query url
     * @param query the query url
     * @return the response text
     * @throws IOException throw an IOException on connection error
     */
    public String httpGet(String query) throws IOException {
        /*
         * Create an HTTP connection to the server for a GET
         * QueryFactory provides the parameter annotations for the HTTP GET query string
         */
        URL url = new URL(query);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(1000);
        connection.setRequestProperty("User-Agent", teamName);
        StringBuilder result = new StringBuilder();

        /*
         * If response code of SUCCESS read the XML string returned
         * line by line to build the full return string
         */
        int responseCode = connection.getResponseCode();
//        System.out.println("Sending 'GET':" + query);
//        System.out.println("Response Code : " + responseCode+"\n");

        if (responseCode/100!=2) {
            throw new IOException(String.valueOf(responseCode));
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("windows-1252")));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }

    /**
     * perform a http Post request to the query url with a body text.
     * @param query the query url
     * @param body the body content.
     * @return the response text.
     * @throws IOException throw an IOException on connection error
     */
    public String httpPost(String query, String body) throws IOException {
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(1000);
        connection.setRequestProperty("User-Agent", teamName);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setDoOutput(true);
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.writeBytes(body);
        writer.flush();
        writer.close();

        int responseCode = connection.getResponseCode();
        System.out.println("Sending 'POST':" + query + "\n" + body);
        System.out.println("Response Code : " + responseCode+"\n");

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
