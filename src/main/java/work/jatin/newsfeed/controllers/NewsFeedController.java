package work.jatin.newsfeed.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import work.jatin.newsfeed.dto.APIResponse;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.services.NewsFeedService;

import java.time.LocalDateTime;
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
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Location cannot be determined from ip", content = @Content)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    APIResponse<List<FeedItemDto>> getFeed(@Parameter(description = "IP address") @RequestParam String ip,
                                           @Parameter(description = "Number of posts") @RequestParam(required = false, defaultValue = "10") @Max(100) Integer pageSize,
                                           @Parameter(description = "Time before which news feed should be fetched") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                           @RequestHeader("User-Id") long userId) {
        return new APIResponse<>(HttpStatus.OK, newsFeedService.getFeed(ip, pageSize, endTime, userId));
    }
}
