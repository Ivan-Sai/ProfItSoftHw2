package internature.hw2.service.impl;

import internature.hw2.dto.author.CreateAuthorDto;
import internature.hw2.dto.author.ResponseAuthorDto;
import internature.hw2.entity.Author;
import internature.hw2.entity.Book;
import internature.hw2.exception.AuthorAlreadyExistsException;
import internature.hw2.exception.AuthorNotFoundException;
import internature.hw2.repository.AuthorRepository;
import internature.hw2.service.AuthorService;
import internature.hw2.service.BookService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    @Lazy
    private final BookService bookService;

    @Override
    public Author getAuthorFromName(String authorName) {
        return authorRepository.findByName(WordUtils.capitalizeFully(authorName.toLowerCase())).orElseGet(() -> {
            Author newAuthor = new Author();
            newAuthor.setName(WordUtils.capitalizeFully(authorName));
            return authorRepository.save(newAuthor);
        });
    }

    @Override
    public ResponseAuthorDto createAuthor(CreateAuthorDto createAuthorDto) {
        if (authorRepository.existsByName(WordUtils.capitalizeFully(createAuthorDto.getName().toLowerCase()))){
            throw new AuthorAlreadyExistsException(createAuthorDto.getName());
        }
        Author author = new Author();
        author.setName(WordUtils.capitalizeFully(createAuthorDto.getName().toLowerCase()));
        authorRepository.save(author);
        return mapAuthorToResponseAuthorDto(author);
    }

    @Override
    public List<ResponseAuthorDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(this::mapAuthorToResponseAuthorDto)
                .toList();
    }

    @Override
    public void updateAuthor(CreateAuthorDto updateAuthorDto, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        if (authorRepository.existsByName(WordUtils.capitalizeFully(updateAuthorDto.getName().toLowerCase()))){
            throw new AuthorAlreadyExistsException(updateAuthorDto.getName());
        }
        author.setName(WordUtils.capitalizeFully(updateAuthorDto.getName().toLowerCase()));
        authorRepository.save(author);

    }

    @Override
    public void deleteAuthor(Long id) {
        bookService.deleteBooksAuthor(id);
        authorRepository.deleteById(id);
    }

    private ResponseAuthorDto mapAuthorToResponseAuthorDto(Author author) {
        ResponseAuthorDto responseAuthorDto = new ResponseAuthorDto();
        responseAuthorDto.setId(author.getId());
        responseAuthorDto.setName(author.getName());
        return responseAuthorDto;
    }
}
