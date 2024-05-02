package internature.hw2.exception;

public class ExcelWriteException extends RuntimeException {
    public ExcelWriteException() {
        super("Some error during excel report generation");
    }
}
