package work.jatin.newsfeed.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import work.jatin.newsfeed.dto.APIResponse;
import work.jatin.newsfeed.dto.PostRequestDto;
import work.jatin.newsfeed.dto.PostResponseDto;
import work.jatin.newsfeed.services.PostService;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created"),
            @ApiResponse(responseCode = "400", description = """
                    Description is null or empty \t
                    Category is missing""", content = @Content),
            @ApiResponse(responseCode = "404", description = "User does not exist with given id", content = @Content),
            @ApiResponse(responseCode = "422", description = "Location cannot be determined from IP address", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
                                                   @RequestHeader("User-Id") long userId,
                                                   @RequestParam String ip) {
        return new APIResponse<>(HttpStatus.CREATED, postService.save(postRequestDto, userId, ip));
    }
}
