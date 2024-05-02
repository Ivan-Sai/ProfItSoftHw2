package internature.hw2.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(long id) {
        super(String.format("Book with id %d not found", id));
    }
}
