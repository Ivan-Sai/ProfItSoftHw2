package internature.hw2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import internature.hw2.Hw2Application;
import internature.hw2.dto.book.ListResponseBookDto;
import internature.hw2.dto.book.PagedResponseDto;
import internature.hw2.dto.book.ResponseBookDto;
import internature.hw2.entity.Book;
import internature.hw2.repository.AuthorRepository;
import internature.hw2.repository.BookRepository;
import internature.hw2.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;


import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Hw2Application.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateBook() throws Exception {
        String title = "Title";
        int yearPublished = 2022;
        List<String> genres = List.of("\"Genre1\"", "\"Genre2\"");
        String authorName = "Author";
        int pages = 100;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ResponseBookDto response = parseResponse(mvcResult, ResponseBookDto.class);
        Long bookId = response.getId();
        assertThat(bookId).isGreaterThanOrEqualTo(1);

        Book book = bookRepository.findById(bookId).orElse(null);
        assertThat(book).isNotNull();
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getYearPublished()).isEqualTo(yearPublished);
        assertThat(book.getGenres()).hasSize(2);

    }

    @Test
    @Transactional
    public void testCreateBookWrongParams() throws Exception {
        String title = "";
        int yearPublished = 2000;
        List<String> genres = List.of();
        String authorName = "";
        int pages = 0;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    public void testGetBooks() throws Exception {
        String title = "Title";
        int yearPublished = 2022;
        List<String> genres = List.of("\"Genre1\"", "\"Genre2\"");
        String authorName = "Author";
        int pages = 100;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ResponseBookDto response = parseResponse(mvcResult, ResponseBookDto.class);
        Long bookId = response.getId();
        assertThat(bookId).isGreaterThanOrEqualTo(1);

        mvcResult = mvc.perform(get("/api/book/%d".formatted(bookId))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        ResponseBookDto book = parseResponse(mvcResult, ResponseBookDto.class);
        assertThat(book.getId()).isEqualTo(bookId);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getYearPublished()).isEqualTo(yearPublished);
        assertThat(book.getGenres()).hasSize(2);

    }

    @Test
    @Transactional
    public void testUpdateBook() throws Exception {
        String title = "Title";
        int yearPublished = 2022;
        List<String> genres = List.of("\"Genre1\"", "\"Genre2\"");
        String authorName = "Author";
        int pages = 100;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ResponseBookDto response = parseResponse(mvcResult, ResponseBookDto.class);
        Long bookId = response.getId();
        assertThat(bookId).isGreaterThanOrEqualTo(1);

        String newTitle = "New Title";
        int newYearPublished = 2023;
        List<String> newGenres = List.of("\"Genre3\"", "\"Genre4\", \"Genre5\"");
        String newAuthorName = "New Author";
        int newPages = 200;

        String newBody = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(newTitle, newYearPublished, newGenres, newAuthorName, newPages);

        mvcResult = mvc.perform(put("/api/book/%d".formatted(bookId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBody)
        )
                .andExpect(status().isNoContent())
                .andReturn();

        Book book = bookRepository.findById(bookId).orElse(null);
        assertThat(book).isNotNull();
        assertThat(book.getTitle()).isEqualTo(newTitle);
        assertThat(book.getYearPublished()).isEqualTo(newYearPublished);
        assertThat(book.getGenres()).hasSize(3);
    }

    @Test
    @Transactional
    public void testDeleteBook() throws Exception {
        String title = "Title";
        int yearPublished = 2022;
        List<String> genres = List.of("\"Genre1\"", "\"Genre2\"");
        String authorName = "Author";
        int pages = 100;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ResponseBookDto response = parseResponse(mvcResult, ResponseBookDto.class);
        Long bookId = response.getId();
        assertThat(bookId).isGreaterThanOrEqualTo(1);

        mvcResult = mvc.perform(delete("/api/book/%d".formatted(bookId))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andReturn();

        Book book = bookRepository.findById(bookId).orElse(null);
        assertThat(book).isNull();
    }

    @Test
    @Transactional
    public void testGetListOfBooks() throws Exception {
        String title = "Title";
        int yearPublished = 2022;
        List<String> genres = List.of("\"Genre1\"", "\"Genre2\"");
        String authorName = "Author";
        int pages = 100;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();


        String filterBody = """
                {
                    "authorName": "%s",
                    "page": %d,
                    "size": %d
                }
                """.formatted(authorName,0,1);

        mvcResult = mvc.perform(post("/api/book/_list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filterBody)
        )
                .andExpect(status().isOk())
                .andReturn();

        PagedResponseDto<ListResponseBookDto> response = parseResponse(mvcResult, PagedResponseDto.class);
        assertThat(response.getList()).hasSize(1);
        assertThat(response.getTotalPages()).isEqualTo(1);



    }


    @Test
    @Transactional
    public void testGetReportOfBooks() throws Exception {
        String title = "Title";
        int yearPublished = 2022;
        List<String> genres = List.of("\"Genre1\"", "\"Genre2\"");
        String authorName = "Author";
        int pages = 100;

        String body = """
                {
                    "title": "%s",
                    "yearPublished": %d,
                    "genres": %s,
                    "authorName": "%s",
                    "pages": %d
                }
                """.formatted(title, yearPublished, genres, authorName, pages);

        MvcResult mvcResult =  mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ResponseBookDto response = parseResponse(mvcResult, ResponseBookDto.class);
        Long bookId = response.getId();
        assertThat(bookId).isGreaterThanOrEqualTo(1);

        String reportBody = """
                {
                    "authorName": "%s",
                    "page": %d,
                    "size": %d
                }
                """.formatted(authorName,0,1);

        mvcResult = mvc.perform(post("/api/book/_report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportBody)
        )
                .andExpect(status().isOk())
                .andReturn();

        byte[] responseBytes = mvcResult.getResponse().getContentAsByteArray();
        assertThat(responseBytes).isNotEmpty();
    }

    @Test
    @Transactional
    public void testUploadBooks() throws Exception {
        String body = """
                [
                  {
                    "title": "The Picture of Dorian Gray",
                    "yearPublished": 1890,
                    "genres": [
                      "Gothic Fiction",
                      "Philosophical Novel"
                    ],
                    "authorName": "Oscar Wilde",
                    "pages": 237
                  }
                ]
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "uploadInvalid.json",
                MediaType.APPLICATION_JSON_VALUE,
                body.getBytes()
        );

        MvcResult mvcResult = mvc.perform(multipart("/api/book/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(bookRepository.findByTitle("The Picture of Dorian Gray")).get().isNotNull();
    }

    private <T> T parseResponse(MvcResult mvcResult, Class<T> c) {
        try {
            String content = mvcResult.getResponse().getContentAsString();
            return objectMapper.readValue(content, c);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing json", e);
        }
    }
}
