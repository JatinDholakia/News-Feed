package work.jatin.newsfeed.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(columnList = "postId, createdAt")})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String text;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "user_id")
    private long userId;

    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(name = "post_id")
    private long postId;

    @CreationTimestamp
    @LastModifiedDate
    private LocalDateTime createdAt;

    public Comment(String text, long userId, long postId) {
        this.text = text;
        this.userId = userId;
        this.postId = postId;
    }
}
