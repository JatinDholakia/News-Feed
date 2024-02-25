package work.jatin.newsfeed.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.jatin.newsfeed.enums.Category;

import java.util.List;

@Entity
@Data @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ElementCollection
    private List<Category> interests;

    public User(String name, List<Category> interests) {
        this.name = name;
        this.interests = interests;
    }

}
