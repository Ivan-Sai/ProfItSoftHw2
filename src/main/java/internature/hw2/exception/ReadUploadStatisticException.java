package internature.hw2.exception;

public class ReadUploadStatisticException extends RuntimeException {
    public ReadUploadStatisticException() {
        super("Some error uploading statistic file");
    }
}
