package internature.hw2.service;


import internature.hw2.dto.author.CreateAuthorDto;
import internature.hw2.dto.author.ResponseAuthorDto;
import internature.hw2.entity.Author;

import java.util.List;

public interface AuthorService {
    Author getAuthorFromName(String authorName);

    ResponseAuthorDto createAuthor(CreateAuthorDto createAuthorDto);

    List<ResponseAuthorDto> getAllAuthors();

    void updateAuthor(CreateAuthorDto updateAuthorDto, Long id);

    void deleteAuthor(Long id);
}
