package work.jatin.newsfeed.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import work.jatin.newsfeed.enums.Category;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String description;

    // TODO
    private String image;

    // TODO
    private String video;

    @CreationTimestamp
    @LastModifiedDate
    private LocalDateTime createdAt;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "user_id")
    private long userId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes;

    private long likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    private long commentCount;

    private double latitude;

    private double longitude;

    private Category category;

    public Post(String description, Category category) {
        this.description = description;
        this.category = category;
    }
}
