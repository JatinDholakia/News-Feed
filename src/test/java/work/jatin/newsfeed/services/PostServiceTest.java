package work.jatin.newsfeed.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import work.jatin.newsfeed.dto.PostRequestDto;
import work.jatin.newsfeed.dto.PostResponseDto;
import work.jatin.newsfeed.enums.Category;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.mapper.PostMapper;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock

    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private NewsFeedService newsFeedService;

    @Mock
    AwsS3Service awsS3Service;

    @InjectMocks
    private PostService postService;

    @Test
    void save_ValidPostRequestDto_SuccessfullySaved() {
        // Arrange
        long userId = 1L;
        String ipAddress = "49.37.161.205";
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setDescription("Test post");
        postRequestDto.setCategory(Category.NEWS);

        User user = new User();
        user.setId(userId);
        UserNode follower1 = new UserNode();
        UserNode follower2 = new UserNode();
        when(userService.existsById(userId)).thenReturn(true);
        when(userService.getFollowers(userId)).thenReturn(List.of(follower1, follower2));

        Post savedPost = new Post();
        savedPost.setDescription("Test post");
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        PostResponseDto postResponseDto = postService.save(postRequestDto, userId, ipAddress);

        // Assert
        assertNotNull(postResponseDto);
        assertEquals(postResponseDto.getDescription(), postRequestDto.getDescription());
        verify(newsFeedService, times(2)).addToFollowerFeed(any(Post.class), any(UserNode.class));
    }

    @Test
    void save_PostRequestWithFileName_SuccessfullySaved() {
        // Arrange
        long userId = 1L;
        String ipAddress = "49.37.161.205";
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setDescription("Test post");
        postRequestDto.setCategory(Category.NEWS);
        postRequestDto.setFileName("image.png");

        User user = new User();
        user.setId(userId);
        when(userService.existsById(userId)).thenReturn(true);

        String presignedUrl = "presigned url";
        when(awsS3Service.createPresignedUrl(anyString())).thenReturn(presignedUrl);

        Post savedPost = PostMapper.convertToPost(postRequestDto);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        UserNode follower1 = new UserNode();
        UserNode follower2 = new UserNode();
        when(userService.getFollowers(userId)).thenReturn(List.of(follower1, follower2));


        // Act
        PostResponseDto postResponseDto = postService.save(postRequestDto, userId, ipAddress);

        // Assert
        assertNotNull(postResponseDto);
        assertEquals(postResponseDto.getDescription(), postRequestDto.getDescription());
        assertEquals(postResponseDto.getCategory(), postRequestDto.getCategory());
        assertEquals(postResponseDto.getPresignedUrl(), presignedUrl);
        verify(newsFeedService, times(2)).addToFollowerFeed(any(Post.class), any(UserNode.class));
    }

    @Test
    void save_InvalidUserId_ThrowsResourceNotFoundException() {
        // Arrange
        long userId = 1L;
        String ipAddress = "49.37.161.205";
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setDescription("Test post");

        when(userService.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.save(postRequestDto, userId, ipAddress));
        verifyNoInteractions(postRepository);
        verifyNoInteractions(newsFeedService);
    }

    @Test
    void findById_ExistingPostId_ReturnsPost() {
        // Arrange
        long postId = 1L;
        Post post = new Post();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        Post foundPost = postService.findById(postId);

        // Assert
        assertNotNull(foundPost);
        assertEquals(post, foundPost);
    }

    @Test
    void findById_NonexistentPostId_ThrowsResourceNotFoundException() {
        // Arrange
        long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.findById(postId));
    }

    @Test
    void existsById_ExistingPostId_ReturnsTrue() {
        // Arrange
        long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);

        // Act
        boolean result = postService.existsById(postId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_NonexistentPostId_ReturnsFalse() {
        // Arrange
        long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);

        // Act
        boolean result = postService.existsById(postId);

        // Assert
        assertFalse(result);
    }

    @Test
    void incrementLikeCount_ValidPostId_IncrementsLikeCount() {
        // Arrange
        long postId = 1L;

        // Act
        postService.incrementLikeCount(postId);

        // Assert
        verify(postRepository, times(1)).incrementLikeCount(postId);
    }

    @Test
    void decrementLikeCount_ValidPostId_DecrementsLikeCount() {
        // Arrange
        long postId = 1L;

        // Act
        postService.decrementLikeCount(postId);

        // Assert
        verify(postRepository, times(1)).decrementLikeCount(postId);
    }

    @Test
    void incrementCommentCount_ValidPostId_IncrementsCommentCount() {
        // Arrange
        long postId = 1L;

        // Act
        postService.incrementCommentCount(postId);

        // Assert
        verify(postRepository, times(1)).incrementCommentCount(postId);
    }

}
