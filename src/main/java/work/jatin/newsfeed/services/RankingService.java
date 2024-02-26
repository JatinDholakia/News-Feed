package work.jatin.newsfeed.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.utils.LocationUtils;

@Service
@Slf4j
public class RankingService {

    public static double getScore(Post post, double userLatitude, double userLongitude) {
        double likeScore = post.getLikeCount() == 0 ? 1 : post.getLikeCount();
        double commentScore = post.getCommentCount() == 0 ? 1 : post.getCommentCount();
        double distance = LocationUtils.calculateDistance(userLatitude,
                userLongitude,
                post.getLatitude(),
                post.getLongitude());
        distance = distance == 0 ? 1 : distance;
        log.info("getScore : likeScore {}, commentScore {}, distance {}", likeScore, commentScore, distance);
        return ((likeScore * commentScore) / (distance));
    }
}
