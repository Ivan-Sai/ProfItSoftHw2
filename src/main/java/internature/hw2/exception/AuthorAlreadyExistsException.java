package internature.hw2.exception;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String name) {
        super(String.format("Author with name %s already exists", name));
    }
}
