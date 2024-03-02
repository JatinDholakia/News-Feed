package work.jatin.newsfeed.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import work.jatin.newsfeed.enums.Category;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class PostResponseDto {

    private long id;

    private String description;

    private Category category;

    private LocalDateTime createdAt;

    private long likeCount;

    private long commentCount;

    private String presignedUrl;

    public PostResponseDto(long id, String description, Category category, LocalDateTime createdAt, long likeCount, long commentCount) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
