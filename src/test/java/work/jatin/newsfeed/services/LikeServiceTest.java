package work.jatin.newsfeed.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import work.jatin.newsfeed.exceptions.DuplicateLikeException;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.models.Like;
import work.jatin.newsfeed.repositories.LikeRepository;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    private LikeService likeService;

    @Test
    void likePost_ValidPostAndUser_SuccessfullyLiked() {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        Mockito.when(postService.existsById(postId)).thenReturn(true);
        Mockito.when(userService.existsById(userId)).thenReturn(true);
        Mockito.when(likeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);

        // Act
        likeService.likePost(postId, userId);

        // Assert
        Mockito.verify(likeRepository, Mockito.times(1)).save(new Like(postId, userId));
        Mockito.verify(postService, Mockito.times(1)).incrementLikeCount(postId);
    }

    @Test
    void likePost_PostNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        Mockito.when(postService.existsById(postId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> likeService.likePost(postId, userId));
        Mockito.verify(likeRepository, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(postService, Mockito.never()).incrementCommentCount(ArgumentMatchers.anyLong());
    }

    @Test
    void likePost_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        Mockito.when(postService.existsById(postId)).thenReturn(true);
        Mockito.when(userService.existsById(userId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> likeService.likePost(postId, userId));
        Mockito.verify(likeRepository, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(postService, Mockito.never()).incrementCommentCount(ArgumentMatchers.anyLong());
    }

    @Test
    void likePost_UserAlreadyLiked_ThrowsDuplicateLikeException() {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        Mockito.when(postService.existsById(postId)).thenReturn(true);
        Mockito.when(userService.existsById(userId)).thenReturn(true);
        Mockito.when(likeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(DuplicateLikeException.class, () -> likeService.likePost(postId, userId));
        Mockito.verify(likeRepository, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(postService, Mockito.never()).incrementCommentCount(ArgumentMatchers.anyLong());
    }

    @Test
    void removeLike_ValidPostAndUser_SuccessfullyRemovedLike() {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        // Act
        likeService.removeLike(postId, userId);

        // Assert
        Mockito.verify(likeRepository, Mockito.times(1)).deleteByUserIdAndPostId(userId, postId);
        Mockito.verify(postService, Mockito.times(1)).decrementLikeCount(postId);
    }
}

