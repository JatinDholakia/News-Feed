package work.jatin.newsfeed.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.dto.FeedItemDto;
import work.jatin.newsfeed.dto.PostResponseDto;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.mapper.PostMapper;
import work.jatin.newsfeed.mapper.UserMapper;
import work.jatin.newsfeed.models.FeedItem;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.FeedItemRepository;
import work.jatin.newsfeed.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsFeedService {

    private final UserService userService;

    private final FeedItemRepository feedItemRepository;

    private final PostRepository postRepository;

    private final AwsS3Service amazonS3Service;

    public NewsFeedService(UserService userService,
                           FeedItemRepository feedItemRepository,
                           PostRepository postRepository,
                           AwsS3Service amazonS3Service) {
        this.userService = userService;
        this.feedItemRepository = feedItemRepository;
        this.postRepository = postRepository;
        this.amazonS3Service = amazonS3Service;
    }

    @Async
    public void addToFollowerFeed(Post post, UserNode followerNode) {
        User follower = userService.findById(followerNode.getUserId());
        if (Boolean.FALSE.equals(follower.getInterests().contains(post.getCategory()))) {
            return;
        }
        feedItemRepository.save(new FeedItem(followerNode.getUserId(), post.getId(),
                RankingService.getScore(post, follower.getLatitude(), follower.getLongitude()), post.getCreatedAt()));
    }

    @Async
    public void updateScore(long postId) {
        Post post = postRepository.findById(postId).get();
        List<FeedItem> feed = feedItemRepository.findByPostId(postId);
        List<FeedItem> newFeed = new ArrayList<>();
        feed.forEach(feedItem -> {
            User follower = userService.findById(feedItem.getUserId());
            feedItem.setScore(RankingService.getScore(post, follower.getLatitude(), follower.getLongitude()));
            newFeed.add(feedItem);
        });
        feedItemRepository.saveAll(newFeed);
    }

    public List<FeedItemDto> getFeed(int pageNo, int pageSize, long userId) {
        Sort.TypedSort<FeedItem> typedSort = Sort.sort(FeedItem.class);
        Sort sort = typedSort.by(FeedItem::getScore).ascending()
                .and(typedSort.by(FeedItem::getCreatedAt).descending());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<FeedItem> feed = feedItemRepository.findByUserId(userId, pageable);
        List<FeedItemDto> result = new ArrayList<>();
        feed.forEach(feedItem -> {
            Post post = postRepository.findById(feedItem.getPostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found with id = " + feedItem.getPostId()));
            PostResponseDto postResponseDto = PostMapper.convertToPostResponseDto(post);
            if (post.getS3Key() != null) {
                postResponseDto.setPresignedUrl(amazonS3Service.createPresignedGetUrl(post.getS3Key()));
            }
            FeedItemDto feedItemDto = new FeedItemDto(UserMapper.convertToUserDto(post.getUser()), postResponseDto);
            result.add(feedItemDto);
        });
        return result;
    }
}
