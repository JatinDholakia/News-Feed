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
import work.jatin.newsfeed.exceptions.NewsFeedExceptionHandler;
import work.jatin.newsfeed.services.LikeService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(likeController)
                .setControllerAdvice(new NewsFeedExceptionHandler())
                .build();
    }

    @Test
    void likePost_ValidPostId_ReturnsOk() throws Exception {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        // Act & Assert
        mockMvc.perform(post("/api/v1/posts/{postId}/likes", postId)
                        .header("User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify
        verify(likeService, times(1)).likePost(postId, userId);
    }

    @Test
    void removeLike_ValidPostId_ReturnsOk() throws Exception {
        // Arrange
        long postId = 1L;
        long userId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/v1/posts/{postId}/likes", postId)
                        .header("User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify
        verify(likeService, times(1)).removeLike(postId, userId);
    }
}
