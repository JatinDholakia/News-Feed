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
import work.jatin.newsfeed.dto.PostRequestDto;
import work.jatin.newsfeed.dto.PostResponseDto;
import work.jatin.newsfeed.enums.Category;
import work.jatin.newsfeed.exceptions.NewsFeedExceptionHandler;
import work.jatin.newsfeed.services.PostService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new NewsFeedExceptionHandler())
                .build();
    }

    @Test
    void createPost_ValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        String description = "Post Description";
        Category category = Category.NEWS;
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setDescription(description);
        postRequestDto.setCategory(category);
        long userId = 1L;
        String ip = "192.168.1.1";

        PostResponseDto postResponseDto = new PostResponseDto(1L, description, category, LocalDateTime.now(), 0, 0);

        when(postService.save(any(), anyLong(), anyString())).thenReturn(postResponseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/posts")
                        .header("User-Id", String.valueOf(userId))
                        .param("ip", ip)
                        .content(asJsonString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.description").value(postRequestDto.getDescription()))
                .andExpect(jsonPath("$.data.category").value(postRequestDto.getCategory().toString()));

        // Verify
        verify(postService, times(1)).save(any(PostRequestDto.class), anyLong(), anyString());
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