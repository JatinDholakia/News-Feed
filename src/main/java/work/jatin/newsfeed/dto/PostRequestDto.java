package work.jatin.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import work.jatin.newsfeed.enums.Category;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "description is required")
    private String description;

    private String fileName;

    @NotNull(message = "category is required")
    private Category category;
}
