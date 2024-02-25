package work.jatin.newsfeed.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Component;
import work.jatin.newsfeed.dto.GeoIP;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class LocationUtils {

    private final static DatabaseReader locationDatabaseReader;

    public static final int EARTH_RADIUS = 6371;

    static {
        File database = new File("src/main/resources/GeoLite2-City.mmdb");
        try {
            locationDatabaseReader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculate distance between two points in latitude and longitude. Uses Haversine method as its base.
     * @param startLat - latitude of starting point
     * @param startLong - longitude of starting point
     * @param endLat - latitude of ending point
     * @param endLong - longitude of ending point
     * @return distance between coordinates
     */
    public static double calculateDistance(double startLat, double startLong, double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public static GeoIP getLocation(String ip)
            throws IOException, GeoIp2Exception {

        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = locationDatabaseReader.city(ipAddress);

        double latitude = response.getLocation().getLatitude();
        double longitude = response.getLocation().getLongitude();
        return new GeoIP(ip, latitude, longitude);
    }
}
