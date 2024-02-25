package work.jatin.newsfeed;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import work.jatin.newsfeed.models.User;
import work.jatin.newsfeed.models.UserNode;
import work.jatin.newsfeed.enums.Category;
import work.jatin.newsfeed.repositories.UserNodeRepository;
import work.jatin.newsfeed.repositories.UserRepository;
import work.jatin.newsfeed.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@Slf4j
public class NewsFeedApplication {

    @Autowired
    UserNodeRepository userNodeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(NewsFeedApplication.class, args);
    }

    @PostConstruct
    void demo() {

        userNodeRepository.deleteAll();
        userRepository.deleteAll();

        User greg = userService.save(new User("greg", List.of(Category.TECH), 12.9716, 77.5946));
        User roy = userService.save(new User("roy", List.of(Category.TECH), 12.9716, 77.5946));
        User craig = userService.save(new User("craig", List.of(Category.TECH), 12.9716, 77.5946));

        UserNode gregNode = userNodeRepository.findByUserId(greg.getId());
        UserNode royNode = userNodeRepository.findByUserId(roy.getId());
        UserNode craigNode = userNodeRepository.findByUserId(craig.getId());

        userService.follow(gregNode, royNode);
        userService.follow(gregNode, craigNode);
        userService.follow(royNode, craigNode);

        log.info("Lookup each UserNode by name...");
        List<UserNode> gregFollowers = userNodeRepository.findByFollowingUserId(gregNode.getUserId());
        log.info("gregFollowers = {}", gregFollowers.stream().map(UserNode::getUserId).collect(Collectors.toList()));
        List<UserNode> royFollowers = userNodeRepository.findByFollowingUserId(royNode.getUserId());
        log.info("royFollowers = {}", royFollowers.stream().map(UserNode::getUserId).collect(Collectors.toList()));
        List<UserNode> craigFollowers = userNodeRepository.findByFollowingUserId(craigNode.getUserId());
        log.info("craigFollowers = {}", craigFollowers.stream().map(UserNode::getUserId).collect(Collectors.toList()));

        List<UserNode> teammates = userNodeRepository.findByFollowingUserId(gregNode.getUserId());
        log.info("The following have Greg as a teammate...");
        teammates.forEach(UserNode -> log.info("\t" + UserNode.getUserId()));
    }

}
