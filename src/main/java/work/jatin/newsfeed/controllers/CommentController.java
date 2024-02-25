package work.jatin.newsfeed.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import work.jatin.newsfeed.dto.APIResponse;
import work.jatin.newsfeed.dto.CommentDto;
import work.jatin.newsfeed.services.CommentService;
import work.jatin.newsfeed.services.NewsFeedService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final NewsFeedService newsFeedService;

    public CommentController(CommentService commentService,
                             NewsFeedService newsFeedService) {
        this.commentService = commentService;
        this.newsFeedService = newsFeedService;
    }

    @Operation(summary = "Comment on a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added comment"),
            @ApiResponse(responseCode = "400", description = "Comment text is missing or blank", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found with given id \t\n " +
                    "User does not exist with given id", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CommentDto> addComment(@PathVariable long postId,
                                              @Valid @RequestBody CommentDto commentDto,
                                              @RequestHeader("User-Id") long userId) {
        CommentDto response = commentService.addComment(commentDto, postId, userId);
        newsFeedService.updateScore(postId);
        return new APIResponse<>(HttpStatus.CREATED, response);
    }

    // TODO : Pagination
    @Operation(summary = "Get all comments on a post sorted by creation date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Post not found with given id", content = @Content)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<CommentDto>> getAllCommentsOnPost(@PathVariable long postId,
                                                    @RequestHeader("User-Id") long userId) {
        return new APIResponse<>(HttpStatus.OK, commentService.getAllCommentsByPostId(postId));
    }
}
