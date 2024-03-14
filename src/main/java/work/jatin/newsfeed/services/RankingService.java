package work.jatin.newsfeed.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.utils.LocationUtils;

import java.time.ZoneOffset;

@Service
@Slf4j
public class RankingService {

    private static final long EPOCH_START = 1134028003;
    private static final double DECAY_FACTOR = 45000;

    public static double getScore(Post post, double userLatitude, double userLongitude) {
        long likes = post.getLikeCount();
        long comments = post.getCommentCount();
        double distance = LocationUtils.calculateDistance(userLatitude,
                userLongitude,
                post.getLatitude(),
                post.getLongitude());
        distance = distance == 0 ? 1 : distance;
        long timeDiff = post.getCreatedAt().toEpochSecond(ZoneOffset.UTC) - EPOCH_START;
        log.info("getScore : likes {}, comments {}, distance {}, timeDiff {}", likes, comments, distance, timeDiff);
        return Math.log10(Math.max(post.getLikeCount() + post.getCommentCount(), 1))
                - Math.log10(distance)
                + timeDiff / DECAY_FACTOR;
    }
}
