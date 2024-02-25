package work.jatin.newsfeed.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.jatin.newsfeed.exceptions.DuplicateLikeException;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.models.Like;
import work.jatin.newsfeed.repositories.LikeRepository;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    public LikeService(LikeRepository likeRepository,
                       UserService userService,
                       PostService postService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public void likePost(long postId, long userId) {
        if (Boolean.FALSE.equals(postService.existsById(postId))) {
            throw new ResourceNotFoundException("Post not found with id = " + postId);
        }
        if (Boolean.FALSE.equals(userService.existsById(userId))) {
            throw new ResourceNotFoundException("User not found with id = " + userId);
        }
        if (Boolean.TRUE.equals(likeRepository.existsByUserIdAndPostId(userId, postId))) {
            throw new DuplicateLikeException("User has already liked the post");
        }
        likeRepository.save(new Like(postId, userId));
        postService.incrementLikeCount(postId);
    }

    @Transactional
    public void removeLike(long postId, long userId) {
        likeRepository.deleteByUserIdAndPostId(userId, postId);
        postService.decrementLikeCount(postId);
    }
}
