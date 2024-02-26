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
import work.jatin.newsfeed.dto.CommentDto;
import work.jatin.newsfeed.exceptions.NewsFeedExceptionHandler;
import work.jatin.newsfeed.services.CommentService;
import work.jatin.newsfeed.services.NewsFeedService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    NewsFeedService newsFeedService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new NewsFeedExceptionHandler())
                .build();
    }

    @Test
    void addComment_ValidCommentDto_ReturnsCreated() throws Exception {
        // Arrange
        long postId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        when(commentService.addComment(any(CommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/posts/{postId}/comments", postId)
                        .header("User-Id", userId)
                        .content(asJsonString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.text").value(commentDto.getText()));

    }

    @Test
    void getAllComments_ValidPostId_ReturnsComments() throws Exception {
        // Arrange
        long postId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        when(commentService.getAllCommentsByPostId(postId)).thenReturn(Collections.singletonList(commentDto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/posts/{postId}/comments", postId)
                        .header("User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].text").value(commentDto.getText()));

    }

    // Helper method to convert object to JSON string
    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
