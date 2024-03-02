package work.jatin.newsfeed.mapper;

import work.jatin.newsfeed.dto.PostRequestDto;
import work.jatin.newsfeed.dto.PostResponseDto;
import work.jatin.newsfeed.models.Post;

public class PostMapper {


    public static PostResponseDto convertToPostResponseDto(Post post) {
        return new PostResponseDto(post.getId(),
                post.getDescription(),
                post.getCategory(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount());
    }

    public static Post convertToPost(PostRequestDto postRequestDto) {
        return new Post(postRequestDto.getDescription(),
                postRequestDto.getCategory());
    }
}
