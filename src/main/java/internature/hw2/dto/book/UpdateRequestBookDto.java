package internature.hw2.dto.book;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateRequestBookDto {

    @Length(min = 1)
    private String title;

    private Integer yearPublished;

    @Size(min = 1)
    private List<String> genres;

    @Length(min = 1)
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Author name should contain only letters and spaces")
    private String authorName;

    @Min(value = 1)
    @Digits(message = "Pages should be a number and not bigger then 10^5", integer = 6, fraction = 0)
    private Integer pages;
}
