package work.jatin.newsfeed.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import work.jatin.newsfeed.models.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAtDesc(long postId);
}
