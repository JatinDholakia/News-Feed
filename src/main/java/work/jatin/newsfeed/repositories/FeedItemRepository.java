package work.jatin.newsfeed.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import work.jatin.newsfeed.models.FeedItem;

import java.util.List;

@Repository
public interface FeedItemRepository extends CrudRepository<FeedItem, Long> {

    Page<FeedItem> findByUserId(long userId, Pageable pageable);

    List<FeedItem> findByPostId(long postId);
}
