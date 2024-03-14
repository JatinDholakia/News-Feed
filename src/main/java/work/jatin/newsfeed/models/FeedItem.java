package work.jatin.newsfeed.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data @NoArgsConstructor @AllArgsConstructor
@RedisHash("feed_item")
public class FeedItem {

    @Id
    private String id;

    @Indexed
    private long userId;

    @Indexed
    private long postId;

    private double score;


    public FeedItem(long userId, long postId, double score) {
        this.userId = userId;
        this.postId = postId;
        this.score = score;
    }
}
