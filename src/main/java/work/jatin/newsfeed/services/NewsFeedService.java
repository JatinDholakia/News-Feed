package work.jatin.newsfeed.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.mapper.PostMapper;
import work.jatin.newsfeed.mapper.UserMapper;
import work.jatin.newsfeed.models.FeedItem;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.FeedItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NewsFeedService {

    private final UserService userService;

    private final FeedItemRepository feedItemRepository;

    public NewsFeedService(UserService userService,
                           FeedItemRepository feedItemRepository) {
        this.userService = userService;
        this.feedItemRepository = feedItemRepository;
    }

    public void addPost(Post post, long userId) {
        List<UserNode> followers = userService.getFollowers(userId);
        followers.forEach(follower -> addToFollowerFeed(post, follower));
    }

    @Async
    public void addToFollowerFeed(Post post, UserNode followerNode) {
        User follower = userService.findById(followerNode.getUserId());
        if (Boolean.FALSE.equals(follower.getInterests().contains(post.getCategory()))) {
            return;
        }
        feedItemRepository.save(new FeedItem(followerNode.getUserId(), post.getId(), post.getCreatedAt()));
    }

    public List<FeedItemDto> getFeed(String ip, Integer pageSize, LocalDateTime endTime, long userId) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<FeedItem> feed = feedItemRepository.findByUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(userId, endTime, pageable);
        List<FeedItemDto> result = new ArrayList<>();
        feed.forEach(feedItem -> {
            Post post = feedItem.getPost();
            double score = RankingService.getScore(post, ip);
            FeedItemDto feedItemDto = new FeedItemDto(UserMapper.convertToUserDto(post.getUser()),
                    PostMapper.convertToPostDto(post), score);
            result.add(feedItemDto);
        });
        result.sort(Comparator.comparing(FeedItemDto::getScore));
        return result;
    }
}
