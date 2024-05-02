package internature.hw2.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class FilterRequestBookDto {

    private String title;
    private Integer yearPublished;

    @Size(min = 1)
    private List<String> genres;

    @Length(min = 1)
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Author name should contain only letters and spaces")
    private String authorName;


    @Min(value = 0)
    private Integer page;
    @Min(value = 1)
    private Integer size;
}
