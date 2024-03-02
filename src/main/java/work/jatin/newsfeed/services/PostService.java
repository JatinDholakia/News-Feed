package work.jatin.newsfeed.services;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import work.jatin.newsfeed.dto.GeoIP;
import work.jatin.newsfeed.dto.PostRequestDto;
import work.jatin.newsfeed.dto.PostResponseDto;
import work.jatin.newsfeed.exceptions.LocationNotFoundException;
import work.jatin.newsfeed.exceptions.ResourceNotFoundException;
import work.jatin.newsfeed.mapper.PostMapper;
import work.jatin.newsfeed.models.Post;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.repositories.PostRepository;
import work.jatin.newsfeed.utils.LocationUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final NewsFeedService newsFeedService;
    private final AwsS3Service amazonS3Service;

    public PostService(PostRepository postRepository,
                       UserService userService,
                       NewsFeedService newsFeedService,
                       AwsS3Service amazonS3Service) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.newsFeedService = newsFeedService;
        this.amazonS3Service = amazonS3Service;
    }

    public PostResponseDto save(PostRequestDto postRequestDto, long userId, String ipAddress) {
        Post post = PostMapper.convertToPost(postRequestDto);

        // set user
        if (Boolean.FALSE.equals(userService.existsById(userId))) {
            throw new ResourceNotFoundException("User not found with id = " + userId);
        }
        post.setUserId(userId);

        // populate lat & long from ip address
        try {
            GeoIP requestLocation = LocationUtils.getLocation(ipAddress);
            post.setLatitude(requestLocation.getLatitude());
            post.setLongitude(requestLocation.getLongitude());
        } catch (IOException | GeoIp2Exception ex) {
            log.error("savePost : cannot find latitude and longitude from ip. userId = {}. Exception {}", userId, ex.getMessage());
            throw new LocationNotFoundException("Location cannot be determined from ip address = " + ipAddress);
        }

        // Generate presigned url for upload if fileName present
        String uploadPresignedUrl = null;
        if (postRequestDto.getFileName() != null) {
            String key = UUID.randomUUID() + "." + FilenameUtils.getExtension(postRequestDto.getFileName());
            uploadPresignedUrl = amazonS3Service.createPresignedUrl(key);
            post.setS3Key(key);
        }

        // save to db
        Post savedPost = postRepository.save(post);

        // fan-out to followers' news feed (Async)
        List<UserNode> followers = userService.getFollowers(userId);
        followers.forEach(follower -> newsFeedService.addToFollowerFeed(post, follower));
        PostResponseDto postResponseDto = PostMapper.convertToPostResponseDto(savedPost);
        postResponseDto.setPresignedUrl(uploadPresignedUrl);
        return postResponseDto;
    }

    public Post findById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id = " + postId));
    }

    public boolean existsById(long postId) {
        return postRepository.existsById(postId);
    }

    public void incrementLikeCount(long postId) {
        postRepository.incrementLikeCount(postId);
    }

    public void decrementLikeCount(long postId) {
        postRepository.decrementLikeCount(postId);
    }

    public void incrementCommentCount(long postId) {
        postRepository.incrementCommentCount(postId);
    }
}
