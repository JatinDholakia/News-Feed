package work.jatin.newsfeed.mapper;

import work.jatin.newsfeed.dto.PostDto;
import work.jatin.newsfeed.models.Post;

public class PostMapper {


    public static PostDto convertToPostDto(Post post) {
        return new PostDto(post.getId(),
                post.getDescription(),
                post.getCategory(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getLatitude(),
                post.getLongitude());
    }

    public static Post convertToPost(PostDto postDto) {
        return new Post(postDto.getDescription(),
                postDto.getCategory());
    }
}
