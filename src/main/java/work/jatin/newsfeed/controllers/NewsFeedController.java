package work.jatin.newsfeed.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import work.jatin.newsfeed.dto.APIResponse;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.services.NewsFeedService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    public NewsFeedController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    @Operation(summary = "Get news feed of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    APIResponse<List<FeedItemDto>> getFeed(@Parameter(description = "Page number of posts starting with 0") @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer pageNo,
                                           @Parameter(description = "Number of posts in one page") @RequestParam(required = false, defaultValue = "10") @Max(100) Integer pageSize,
                                           @RequestHeader("User-Id") long userId) {
        return new APIResponse<>(HttpStatus.OK, newsFeedService.getFeed(pageNo, pageSize, userId));
    }
}
