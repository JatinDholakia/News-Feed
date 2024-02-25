package work.jatin.newsfeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import work.jatin.newsfeed.enums.Category;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PostDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotNull(message = "description is required")
    private String description;

    @NotNull(message = "category is required")
    private Category category;
}
