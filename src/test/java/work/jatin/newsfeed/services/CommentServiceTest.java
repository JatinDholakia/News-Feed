package work.jatin.newsfeed.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import work.jatin.newsfeed.dto.CommentDto;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.models.Comment;
import work.jatin.newsfeed.repositories.CommentRepository;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addComment_ValidCommentDto_ReturnsCommentDto() {
        // Arrange
        long postId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        Mockito.when(postService.existsById(postId)).thenReturn(true);
        Mockito.when(userService.existsById(userId)).thenReturn(true);

        Comment comment = new Comment(commentDto.getText(), userId, postId);
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        // Act
        CommentDto savedCommentDto = commentService.addComment(commentDto, postId, userId);

        // Assert
        Assertions.assertNotNull(savedCommentDto);
        Assertions.assertEquals(commentDto.getText(), savedCommentDto.getText());
    }

    @Test
    void addComment_PostNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long postId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        Mockito.when(postService.existsById(postId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.addComment(commentDto, postId, userId));
    }

    @Test
    void addComment_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long postId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        Mockito.when(postService.existsById(postId)).thenReturn(true);
        Mockito.when(userService.existsById(userId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.addComment(commentDto, postId, userId));
    }

    @Test
    void getAllCommentsByPostId_ValidPostId_ReturnsListOfCommentDto() {
        // Arrange
        long postId = 1L;
        Comment comment1 = new Comment("Comment 1", 1L, postId);
        Comment comment2 = new Comment("Comment 2", 2L, postId);

        Mockito.when(postService.existsById(postId)).thenReturn(true);
        Mockito.when(commentRepository.findByPostIdOrderByCreatedAtDesc(postId))
                .thenReturn(Arrays.asList(comment1, comment2));

        // Act
        List<CommentDto> commentDtoList = commentService.getAllCommentsByPostId(postId);

        // Assert
        Assertions.assertEquals(2, commentDtoList.size());
        Assertions.assertEquals("Comment 1", commentDtoList.get(0).getText());
        Assertions.assertEquals("Comment 2", commentDtoList.get(1).getText());
    }

    @Test
    void getAllCommentsByPostId_PostNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long postId = 1L;

        Mockito.when(postService.existsById(postId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.getAllCommentsByPostId(postId));
    }
}
