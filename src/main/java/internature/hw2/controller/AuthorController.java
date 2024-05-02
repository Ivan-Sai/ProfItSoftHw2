package internature.hw2.controller;

import internature.hw2.dto.author.CreateAuthorDto;
import internature.hw2.dto.author.ResponseAuthorDto;
import internature.hw2.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/author")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping()
    public ResponseEntity<ResponseAuthorDto> createAuthor(@Valid @RequestBody CreateAuthorDto createAuthorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(createAuthorDto));
    }

    @GetMapping
    public ResponseEntity<List<ResponseAuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAuthor(@Valid @RequestBody CreateAuthorDto updateAuthorDto, @PathVariable Long id) {
        authorService.updateAuthor(updateAuthorDto, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
