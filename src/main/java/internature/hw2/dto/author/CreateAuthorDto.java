package internature.hw2.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAuthorDto {

    @NotBlank
    String name;
}
