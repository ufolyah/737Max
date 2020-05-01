package B737Max.Components;

import com.skedgo.converter.TimezoneMapper;

import java.time.ZoneId;

/**
 *
 */
public class Airport {
    private String code;
    private String name;
    private ZoneId timeZone;
    private double latitude;
    private double longitude;

    /**
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
     * @return
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
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public ZoneId getTimeZone() {
        return timeZone;
    }

    /**
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

}
