package internature.hw2.dto.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListResponseBookDto {
    private Long id;
    private String title;
    private String authorName;
    private Integer yearPublished;
}
