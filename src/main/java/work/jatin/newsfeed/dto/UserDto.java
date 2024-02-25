package work.jatin.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import work.jatin.newsfeed.enums.Category;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserDto {
    private long id;

    private String name;

    private List<Category> interests;
}
