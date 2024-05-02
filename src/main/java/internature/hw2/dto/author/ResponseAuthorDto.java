package internature.hw2.dto.author;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAuthorDto {
    private Long id;
    private String name;
}
