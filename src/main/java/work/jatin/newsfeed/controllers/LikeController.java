package work.jatin.newsfeed.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import work.jatin.newsfeed.services.LikeService;
import work.jatin.newsfeed.services.NewsFeedService;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    private final NewsFeedService newsFeedService;

    public LikeController(LikeService likeService,
                          NewsFeedService newsFeedService) {
        this.likeService = likeService;
        this.newsFeedService = newsFeedService;
    }

    @Operation(summary = "Like a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like added"),
            @ApiResponse(responseCode = "400", description = "User has already liked the post"),
            @ApiResponse(responseCode = "404", description = "Post not found with given id \t\n" +
                    "User not found with given id")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void likePost(@PathVariable long postId,
                         @RequestHeader("User-Id") long userId) {
        likeService.likePost(postId, userId);
        newsFeedService.updateScore(postId);
    }

    @Operation(summary = "Remove like from a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like removed")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable long postId,
                           @RequestHeader("User-Id") long userId) {
        likeService.removeLike(postId, userId);
        newsFeedService.updateScore(postId);
    }

    // TODO : Get all likes of post
//    @GetMapping("/api/v1/posts/{postId}/likes")
//    public ResponseEntity<List<UserDto>> getAllLikes(@PathVariable long )
}
