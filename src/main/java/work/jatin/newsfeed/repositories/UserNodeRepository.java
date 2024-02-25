package work.jatin.newsfeed.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import work.jatin.newsfeed.models.UserNode;

import java.util.List;


@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {

    UserNode findByUserId(long userId);

    List<UserNode> findByFollowingUserId(long userId);
}
