package internature.hw2.dto.book;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BookMessageDto {
    private String title;
    private String author;
}
