package work.jatin.newsfeed.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import work.jatin.newsfeed.models.FeedItem;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedItemRepository extends PagingAndSortingRepository<FeedItem, Long>, CrudRepository<FeedItem, Long> {
    List<FeedItem> findByUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(long userId, LocalDateTime endTime, Pageable pageable);
}
