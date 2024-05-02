package internature.hw2.dto.book;

import internature.hw2.entity.Author;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseBookDto {
    private Long id;
    private String title;
    private Author author;
    private List<String> genres;
    private Integer yearPublished;
    private Integer pages;
}
