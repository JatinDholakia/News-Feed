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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Category> interests;

    private double latitude;

    private double longitude;

    public User(String name, List<Category> interests, double latitude, double longitude) {
        this.name = name;
        this.interests = interests;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
