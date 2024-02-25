package work.jatin.newsfeed.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.dto.CommentDto;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.mapper.CommentMapper;
import work.jatin.newsfeed.models.Comment;
import work.jatin.newsfeed.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository,
                          PostService postService,
                          UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public CommentDto addComment(CommentDto commentDto, long postId, long userId) {
        if (Boolean.FALSE.equals(postService.existsById(postId))) {
            throw new ResourceNotFoundException("Post not found with id = " + postId);
        }
        if (Boolean.FALSE.equals(userService.existsById(userId))) {
            throw new ResourceNotFoundException("User not found with id = " + userId);
        }
        Comment comment = new Comment(commentDto.getText(), userId, postId);
        Comment savedComment = commentRepository.save(comment);
        postService.incrementCommentCount(postId);
        return CommentMapper.convertToCommentDto(savedComment);
    }

    // TODO : Pagination and sorting
    public List<CommentDto> getAllCommentsByPostId(long postId) {
        if (Boolean.FALSE.equals(postService.existsById(postId))) {
            throw new ResourceNotFoundException("Post not found with id = " + postId);
        }
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream().map(CommentMapper::convertToCommentDto)
                .collect(Collectors.toList());
    }
}
