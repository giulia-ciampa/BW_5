package team6.BW_5.exceptions;

public class WrongQueryArgumentException extends RuntimeException {
    public WrongQueryArgumentException(String message) {
        super(message);
    }
}
