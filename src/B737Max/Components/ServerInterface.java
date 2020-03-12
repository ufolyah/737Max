package B737Max.Components;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
        if (Airports.getInstance().getList().length!=0) {
            return;
        }
        String xml = httpGet(urlBase+QueryFactory.getAirports(teamName));
        Airports.getInstance().setList(XMLInterface.parseAirports(xml));
    }

    /**
     * @throws IOException
     */
    public void getAirplanes() throws IOException {

    }

    /**
     * @return
     * @throws IOException
     */
    public Trips searchFlights() throws IOException {
        return null;
    }

    /**
     * @throws IOException
     */
    public void reset() throws IOException {

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
     * @param flights
     * @throws IOException
     */
    public void reserve(Flight[] flights) throws IOException {

    }

    private String httpGet(String query) throws IOException {
        /*
         * Create an HTTP connection to the server for a GET
         * QueryFactory provides the parameter annotations for the HTTP GET query string
         */
        URL url = new URL(query);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", teamName);
        StringBuffer result = new StringBuffer();

        /*
         * If response code of SUCCESS read the XML string returned
         * line by line to build the full return string
         */
        int responseCode = connection.getResponseCode();
        if (responseCode/100==2) {
            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } else {
            throw new IOException("httpGet:"+query+" failed with "+ responseCode);
        }
        return result.toString();
    }

    private String httpPost(String query, String body) throws IOException {
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", teamName);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        connection.setDoOutput(true);
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.writeBytes(body);
        writer.flush();
        writer.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST'");
        System.out.println(("\nResponse Code : " + responseCode));

        StringBuffer response = new StringBuffer();
        if (responseCode/100==2) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
        } else {
            throw new IOException("httpPost: failed with "+ responseCode);
        }

        return response.toString();
    }


}
