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
    private String code;

    /** Full name of the airport */
    private String name;

    /** TimeZone ID of the airport based on the location*/
    private ZoneId timeZone;

    /** Latitude of airport in decimal format */
    private double latitude;

    /** Longitude of airport in decimal format */
    private double longitude;

    /**
     *
     * Sets all params in Class Airport
     *
     * @param code
     * @param name
     * @param latitude
     * @param longitude
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
     * @return Basic information of the airport
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
     * @return airport code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return airport name
     */
    public String getName() {
        return name;
    }

    /**
     * @return timezone id
     */
    public ZoneId getTimeZone() {
        return timeZone;
    }

    /**
     * @return latitude of airport
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return longitude of airport
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     *
     * @param obj
     * @return
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
