package work.jatin.newsfeed.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.enums.Category;
import work.jatin.newsfeed.models.FeedItem;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.FeedItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class NewsFeedServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private FeedItemRepository feedItemRepository;

    @InjectMocks
    private NewsFeedService newsFeedService;

    @Test
    void addPost_ValidPost_AddsToFollowersFeed() {
        // Arrange
        Post post = new Post();
        post.setId(1L);
        post.setCategory(Category.TECH);
        long userId = 1L;

        UserNode follower1Node = new UserNode(2L);
        UserNode follower2Node = new UserNode(3L);
        List<UserNode> followers = List.of(follower1Node, follower2Node);

        User follower1 = new User();
        follower1.setId(2L);
        follower1.setInterests(List.of(Category.TECH, Category.NEWS));
        User follower2 = new User();
        follower2.setId(3L);
        follower2.setInterests(List.of(Category.NEWS, Category.SPORTS));

        Mockito.when(userService.getFollowers(userId)).thenReturn(followers);
        Mockito.when(userService.findById(follower1.getId())).thenReturn(follower1);
        Mockito.when(userService.findById(follower2.getId())).thenReturn(follower2);

        // Act
        newsFeedService.addPost(post, userId);

        // Assert
        Mockito.verify(userService, Mockito.times(1)).getFollowers(userId);
        Mockito.verify(feedItemRepository, Mockito.times(1)).save(ArgumentMatchers.any(FeedItem.class));
    }

    @Test
    void addToFollowerFeed_UserHasInterest_SaveToFeedItemRepository() {
        // Arrange
        Post post = new Post();
        post.setId(1L);
        post.setCategory(Category.TECH);

        UserNode followerNode = new UserNode(2L);
        User follower = new User();
        follower.setInterests(List.of(Category.TECH));

        Mockito.when(userService.findById(followerNode.getUserId())).thenReturn(follower);

        // Act
        newsFeedService.addToFollowerFeed(post, followerNode);

        // Assert
        Mockito.verify(userService, Mockito.times(1)).findById(followerNode.getUserId());
        Mockito.verify(feedItemRepository, Mockito.times(1)).save(ArgumentMatchers.any(FeedItem.class));
    }

    @Test
    void addToFollowerFeed_UserHasNoInterest_DoesNotSaveToFeedItemRepository() {
        // Arrange
        Post post = new Post();
        post.setId(1L);
        post.setCategory(Category.SPORTS);

        UserNode followerNode = new UserNode(2L);
        User follower = new User();
        follower.setInterests(List.of(Category.TECH));

        Mockito.when(userService.findById(followerNode.getUserId())).thenReturn(follower);

        // Act
        newsFeedService.addToFollowerFeed(post, followerNode);

        // Assert
        Mockito.verify(userService, Mockito.times(1)).findById(followerNode.getUserId());
        Mockito.verify(feedItemRepository, Mockito.never()).save(ArgumentMatchers.any(FeedItem.class));
    }

    @Test
    void getFeed_ValidParameters_ReturnsFeedItemDtoList() {
        // Arrange
        String ip = "49.37.161.205";
        int pageSize = 10;
        LocalDateTime endTime = LocalDateTime.now();
        long userId = 1L;

        List<FeedItem> feedItems = new ArrayList<>();
        FeedItem feedItem = new FeedItem(2L, 2L, LocalDateTime.now().minusSeconds(20));
        Post post = new Post("post 2", Category.TECH);
        User user = new User("user1", List.of(Category.TECH));
        user.setId(userId);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now().minusSeconds(10));
        feedItem.setPost(post);
        feedItems.add(feedItem);
        Mockito.when(feedItemRepository.findByUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(userId, endTime, PageRequest.of(0, pageSize)))
                .thenReturn(feedItems);

        // Act
        List<FeedItemDto> result = newsFeedService.getFeed(ip, pageSize, endTime, userId);

        // Assert
        Assertions.assertEquals(feedItems.size(), result.size());
    }
}
