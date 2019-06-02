package hotel.core.exception;

import org.springframework.http.HttpStatus;

public class HotelRuntimeException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public HotelRuntimeException(final String msg) {
        super(msg);
    }

    public HotelRuntimeException(final HttpStatus httpStatus) {
        super(httpStatus.toString());
        this.httpStatus = httpStatus;
    }

    public HotelRuntimeException(final HttpStatus httpStatus, final String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HotelRuntimeException(String msg, final RuntimeException e) {
        super(msg, e);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
