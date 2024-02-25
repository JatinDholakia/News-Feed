package work.jatin.newsfeed.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.exceptions.NewsFeedExceptionHandler;
import work.jatin.newsfeed.services.NewsFeedService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NewsFeedControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NewsFeedService newsFeedService;

    @InjectMocks
    private NewsFeedController newsFeedController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(newsFeedController)
                .setControllerAdvice(new NewsFeedExceptionHandler())
                .build();
    }

    @Test
    void getFeed_ValidRequest_ReturnsOk() throws Exception {
        // Arrange
        String ip = "192.168.1.1";
        int pageSize = 10;
        LocalDateTime endTime = LocalDateTime.now();
        long userId = 1L;

        List<FeedItemDto> feedItems = List.of(new FeedItemDto());
        when(newsFeedService.getFeed(ip, pageSize, endTime, userId)).thenReturn(feedItems);

        // Act & Assert
        mockMvc.perform(get("/api/v1/feed")
                        .param("ip", ip)
                        .param("pageSize", String.valueOf(pageSize))
                        .param("endTime", endTime.toString())
                        .header("User-Id", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        // Verify
        verify(newsFeedService, times(1)).getFeed(ip, pageSize, endTime, userId);
    }
}