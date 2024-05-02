package internature.hw2.dto.book;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedResponseDto<T> {

    private List<T> list;
    private int totalPages;
}
