package work.jatin.newsfeed.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GeoIP {
    private String ipAddress;
    private double latitude;
    private double longitude;
}
