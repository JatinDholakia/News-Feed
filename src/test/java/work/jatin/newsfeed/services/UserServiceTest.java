package work.jatin.newsfeed.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.UserNodeRepository;
import work.jatin.newsfeed.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserNodeRepository userNodeRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_ExistingUserId_ReturnsUser() {
        // Arrange
        long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void findById_NonexistentUserId_ThrowsResourceNotFoundException() {
        // Arrange
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void getFollowers_ValidUserId_ReturnsUserNodeList() {
        // Arrange
        long userId = 1L;
        List<UserNode> followers = List.of(new UserNode(), new UserNode());
        when(userNodeRepository.findByFollowingUserId(userId)).thenReturn(followers);

        // Act
        List<UserNode> foundFollowers = userService.getFollowers(userId);

        // Assert
        assertNotNull(foundFollowers);
        assertEquals(followers.size(), foundFollowers.size());
    }

    @Test
    void save_ValidUser_SuccessfullySaved() {
        // Arrange
        User user = new User();
        User savedUser = new User();
        when(userRepository.save(user)).thenReturn(savedUser);

        // Act
        User result = userService.save(user);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser, result);
        verify(userNodeRepository, times(1)).save(any(UserNode.class));
    }

    @Test
    void follow_ValidUsers_SuccessfullyFollowed() {
        // Arrange
        UserNode user = new UserNode();
        UserNode savedUser = new UserNode();
        UserNode userToFollow = new UserNode();
        savedUser.setFollowing(Set.of(userToFollow));
        when(userNodeRepository.save(user)).thenReturn(savedUser);

        // Act
        UserNode result = userService.follow(user, userToFollow);

        // Assert
        assertNotNull(result);
        assertTrue(result.getFollowing().contains(userToFollow));
        verify(userNodeRepository, times(1)).save(result);
    }

    @Test
    void existsById_ExistingUserId_ReturnsTrue() {
        // Arrange
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean result = userService.existsById(userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_NonexistentUserId_ReturnsFalse() {
        // Arrange
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        boolean result = userService.existsById(userId);

        // Assert
        assertFalse(result);
    }
}
