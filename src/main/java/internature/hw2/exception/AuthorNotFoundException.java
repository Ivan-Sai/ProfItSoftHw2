package internature.hw2.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(long id) {
        super(String.format("Author with id %d not found", id));
    }
}
