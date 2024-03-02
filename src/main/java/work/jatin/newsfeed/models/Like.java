package work.jatin.newsfeed.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(LikeId.class)
public class Like {

    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(name = "post_id")
    @Id
    private long postId;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "user_id")
    @Id
    private long userId;

    @CreationTimestamp
    @LastModifiedDate
    private LocalDateTime createdAt;

    public Like(long postId, long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
