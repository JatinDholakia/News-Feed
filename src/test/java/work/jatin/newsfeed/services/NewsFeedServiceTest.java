package work.jatin.newsfeed.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.enums.Category;
import work.jatin.newsfeed.models.FeedItem;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.FeedItemRepository;
import work.jatin.newsfeed.repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;


@ExtendWith(MockitoExtension.class)
class NewsFeedServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private FeedItemRepository feedItemRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    private NewsFeedService newsFeedService;

    @Test
    void addToFollowerFeed_UserHasInterest_SaveToFeedItemRepository() {
        // Arrange
        Post post = new Post();
        post.setId(1L);
        post.setCategory(Category.TECH);
        post.setCreatedAt(LocalDateTime.now().minusSeconds(10));

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
        int pageNo = 0;
        int pageSize = 10;
        long userId = 1L;

        List<FeedItem> feedItems = new ArrayList<>();
        FeedItem feedItem = new FeedItem(2L, 2L, 1.0);
        Post post = new Post("post 2", Category.TECH);
        User user = new User("user1", List.of(Category.TECH), 12.9716, 77.5946);
        user.setId(userId);
        post.setId(2L);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now().minusSeconds(10));
        feedItem.setPostId(post.getId());
        feedItems.add(feedItem);
        Sort.TypedSort<FeedItem> typedSort = Sort.sort(FeedItem.class);
        Sort sort = typedSort.by(FeedItem::getScore).descending();
        Mockito.when(feedItemRepository.findByUserId(userId, PageRequest.of(0, pageSize, sort)))
                .thenReturn(new PageImpl<>(feedItems));
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // Act
        List<FeedItemDto> result = newsFeedService.getFeed(pageNo, pageSize, userId);

        // Assert
        Assertions.assertEquals(feedItems.size(), result.size());
    }

    @Test
    void updateScore_ValidPostId_FeedItemsSaved() {
        // Arrange
        Post post = new Post();
        post.setId(1L);
        post.setCategory(Category.TECH);
        post.setCreatedAt(LocalDateTime.now().minusSeconds(10));
        FeedItem feedItem = new FeedItem(2L, post.getId(), 1.0);

        User follower = new User();
        follower.setInterests(List.of(Category.TECH));
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(feedItemRepository.findByPostId(post.getId())).thenReturn(List.of(feedItem));
        Mockito.when(userService.findById(feedItem.getUserId())).thenReturn(follower);

        // Act
        newsFeedService.updateScore(post.getId());

        // Assert
        Mockito.verify(userService, Mockito.times(1)).findById(feedItem.getUserId());
        Mockito.verify(feedItemRepository, Mockito.times(1)).saveAll(anyList());
    }
}
