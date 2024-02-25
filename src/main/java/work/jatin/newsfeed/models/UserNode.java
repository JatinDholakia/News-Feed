package work.jatin.newsfeed.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Data @NoArgsConstructor
public class UserNode {

    @Id
    private long userId;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private Set<UserNode> following;

    public UserNode(long userId) {
        this.userId = userId;
    }

    public void follow(UserNode userNode) {
        if (following == null) {
            following = new HashSet<>();
        }
        following.add(userNode);
    }
}
