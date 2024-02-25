package work.jatin.newsfeed.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import work.jatin.newsfeed.models.Like;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {

    boolean existsByUserIdAndPostId(long userId, long postId);

    void deleteByUserIdAndPostId(long userId, long postId);

}
