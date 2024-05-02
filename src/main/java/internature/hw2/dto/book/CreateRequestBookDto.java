package internature.hw2.dto.book;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateRequestBookDto {

    @NotBlank
    private String title;

    @NotNull
    private Integer yearPublished;

    @NotEmpty
    private List<String> genres;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Author name should contain only letters and spaces")
    private String authorName;

    @NotNull
    @Min(value = 1)
    @Digits(message = "Pages should be a number and not bigger then 10^5", integer = 6, fraction = 0)
    private Integer pages;
}
