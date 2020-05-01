package B737Max.Components;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Layover {
    public final Airport airport;
    public final ZonedDateTime startTime, endTime;
    public final Duration duration;

    @Override
    public String toString() {
        return "Layover{" +
                "airport=" + airport.getCode() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }

    public Layover(Airport airport, ZonedDateTime startTime, ZonedDateTime endTime) {
        this.airport = airport;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
    }

}
