package B737Max.Components;

import com.skedgo.converter.TimezoneMapper;

import java.time.ZoneId;

/**
 *
 *  This class holds values pertaining to a single Airport. Class member attributes
 *  are the same as defined by the CS509 server API and store values after conversion from
 *  XML received from the server to Java primitives.
 *
 *  @author xudufy
 *  @version 2.0 2020-05-03
 *  @since 2020-03-01
 *
 */
public class Airport {
    /**
     * Airport attributes as defined by the CS509 server interface XML
     */

    /** Three character code of the airport */
    private final String code;

    /** Full name of the airport */
    private final String name;

    /** TimeZone ID of the airport based on the location*/
    private final ZoneId timeZone;

    /** Latitude of airport in decimal format */
    private final double latitude;

    /** Longitude of airport in decimal format */
    private final double longitude;

    /**
     * Construct an Airport instance with code, name, latitude, longitude
     *
     * @param code Three character code of the airport
     * @param name Full name of the airport
     * @param latitude latitude of airport in decimal format
     * @param longitude Longitude of airport in decimal format
     */
    public Airport(String code, String name, double latitude, double longitude) {
        this.code = code;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZone = ZoneId.of(
                TimezoneMapper.latLngToTimezoneString(latitude, longitude)
        );
    }

    /**
     * set a string of airport information
     * @return Basic information of the airport including code,name, timezone ID, latitude, longitude
     */
    @Override
    public String toString() {
        return "Airport{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", timeZone=" + timeZone +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    /**
     * get the code of airport
     * @return airport code
     */
    public String getCode() {
        return code;
    }

    /**
     * get full name of airport
     * @return airport name
     */
    public String getName() {
        return name;
    }

    /**
     * get timezone id of airport
     * @return timezone id
     */
    public ZoneId getTimeZone() {
        return timeZone;
    }

    /**
     * get latitude of airport
     * @return latitude of airport
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * get longitude of airport
     * @return longitude of airport
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * determine whether two airports are equal
     * @param obj an airport
     * @return whether two airports are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Airport other = (Airport) obj;
        return code.equals(other.code) && name.equals(other.name) && longitude == other.longitude
                && latitude == other.latitude;
    }
}
