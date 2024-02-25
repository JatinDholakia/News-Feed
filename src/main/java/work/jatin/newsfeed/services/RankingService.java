package work.jatin.newsfeed.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.utils.LocationUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
// TODO : Improve ranking algo
public class RankingService {

    public static double getScore(Post post, double userLatitude, double userLongitude) {
        double likeScore = post.getLikeCount() == 0 ? 1 : post.getLikeCount();
        double commentScore = post.getCommentCount() == 0 ? 1 : post.getCommentCount();
        double distance = LocationUtils.calculateDistance(userLatitude,
                userLongitude,
                post.getLatitude(),
                post.getLongitude());
        distance = distance == 0 ? 1 : distance;
        long time = ChronoUnit.SECONDS.between(post.getCreatedAt(), LocalDateTime.now());
        time = time == 0 ? 1 : time;
        log.info("getScore : likeScore {}, commentScore {}, distance {}, time {}", likeScore, commentScore, distance, time);
        return ((likeScore * commentScore) / (distance * time));
    }
}
