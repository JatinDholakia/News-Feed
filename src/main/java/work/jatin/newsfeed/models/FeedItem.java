package work.jatin.newsfeed.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor
@Entity
// TODO : move to redis
public class FeedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "user_id")
    private long userId;

    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    @Column(name = "post_id")
    private long postId;

    private LocalDateTime createdAt;

    public FeedItem(long userId, long postId, LocalDateTime createdAt) {
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }
}
