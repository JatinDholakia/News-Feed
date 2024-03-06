package work.jatin.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import work.jatin.newsfeed.enums.Category;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "Description is required")
    @Size(max = 280, message = "Description cannot exceed 280 characters")
    private String description;

    private String fileName;

    @NotNull(message = "Category is required")
    private Category category;
}
