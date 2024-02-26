package work.jatin.newsfeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import work.jatin.newsfeed.enums.Category;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PostDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "category is required")
    private Category category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long likeCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long commentCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double latitude;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double longitude;
}
