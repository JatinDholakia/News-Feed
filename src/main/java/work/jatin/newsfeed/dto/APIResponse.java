package work.jatin.newsfeed.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class APIResponse<T> {
    private int status;
    private Date timestamp;
    private T data;

    public APIResponse(HttpStatus status, @Nullable T data) {
        this.status = status.value();
        this.timestamp = new Date();
        this.data = data;
    }
}
