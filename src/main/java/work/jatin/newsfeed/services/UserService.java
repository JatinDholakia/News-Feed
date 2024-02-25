package work.jatin.newsfeed.services;

import org.springframework.stereotype.Service;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.UserNodeRepository;
import work.jatin.newsfeed.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserNodeRepository userNodeRepository;

    public UserService(UserRepository userRepository,
                       UserNodeRepository userNodeRepository) {
        this.userRepository = userRepository;
        this.userNodeRepository =userNodeRepository;
    }

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + userId));
    }

    public List<UserNode> getFollowers(long userId) {
        return userNodeRepository.findByFollowingUserId(userId);
    }

    public User save(User user) {
        User savedUser = userRepository.save(user);
        UserNode userNode = new UserNode(savedUser.getId());
        userNodeRepository.save(userNode);
        return savedUser;
    }

    public UserNode follow(UserNode user, UserNode userToFollow) {
        user.follow(userToFollow);
        return userNodeRepository.save(user);
    }

    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }

}
