package internature.hw2.dto.book;

import internature.hw2.entity.Author;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class ResponseBookDto {
    private Long id;
    private String title;
    private Author author;
    private List<String> genres;
    private Integer yearPublished;
    private Integer pages;
}
