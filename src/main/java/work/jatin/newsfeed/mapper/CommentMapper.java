package work.jatin.newsfeed.mapper;

import work.jatin.newsfeed.dto.CommentDto;
import work.jatin.newsfeed.models.Comment;

public class CommentMapper {

    public static CommentDto convertToCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
