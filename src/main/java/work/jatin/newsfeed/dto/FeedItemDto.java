package work.jatin.newsfeed.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class FeedItemDto {
    private UserDto user;

    private PostDto post;

    @JsonIgnore
    private double score;
}
