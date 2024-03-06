package work.jatin.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorMessage {
    private int status;
    private Date timestamp;
    private List<String> messages;
    private String description;

    public ErrorMessage(int status, List<String> messages, String description) {
        this.status = status;
        this.timestamp = new Date();
        this.messages = messages;
        this.description = description;
    }
}
