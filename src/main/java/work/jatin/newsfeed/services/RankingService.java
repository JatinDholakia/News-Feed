package work.jatin.newsfeed.services;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.dto.GeoIP;
import work.jatin.newsfeed.exceptions.LocationNotFoundException;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.utils.LocationUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
// TODO : Improve ranking algo
public class RankingService {

    public static double getScore(Post post, String ip) {
        double likeScore = post.getLikeCount() == 0 ? 1 : post.getLikeCount();
        double commentScore = post.getCommentCount() == 0 ? 1 : post.getCommentCount();
        try {
            GeoIP geoIP = LocationUtils.getLocation(ip);
            double distance = LocationUtils.calculateDistance(geoIP.getLatitude(),
                    geoIP.getLongitude(),
                    post.getLatitude(),
                    post.getLongitude());
            distance = distance == 0 ? 1 : distance;
            long time = ChronoUnit.SECONDS.between(LocalDateTime.now(), post.getCreatedAt());
            return ((likeScore * commentScore) / (distance * time));
        } catch (IOException | GeoIp2Exception ex) {
            log.error("savePost : cannot find latitude and longitude from ip {}. Exception {}", ip, ex.getMessage());
            throw new LocationNotFoundException("Location cannot be determined from ip = " + ip);
        }
    }
}
