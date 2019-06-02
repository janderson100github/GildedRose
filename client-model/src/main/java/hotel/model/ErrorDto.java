package hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDto {

    @JsonProperty("message")
    private String message;

    public ErrorDto(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
