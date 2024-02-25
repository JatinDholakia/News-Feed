package work.jatin.newsfeed.mapper;

import work.jatin.newsfeed.dto.UserDto;
import work.jatin.newsfeed.models.User;

public class UserMapper {

    public static UserDto convertToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getInterests());
    }
}
