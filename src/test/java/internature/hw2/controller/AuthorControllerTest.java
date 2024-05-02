package internature.hw2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import internature.hw2.Hw2Application;
import internature.hw2.dto.author.CreateAuthorDto;
import internature.hw2.dto.author.ResponseAuthorDto;
import internature.hw2.entity.Author;
import internature.hw2.repository.AuthorRepository;
import internature.hw2.repository.BookRepository;
import internature.hw2.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Hw2Application.class)
@AutoConfigureMockMvc
public class AuthorControllerTest {

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
    public void testCreateAuthor() throws Exception {

        String body = """
            {
                "name": "Author"
            }
            """;


        MvcResult result = mvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        ResponseAuthorDto authorDto = parseResponse(result, ResponseAuthorDto.class);
        assertThat(authorDto.getId()).isGreaterThanOrEqualTo(1);
        assertThat(authorDto.getName()).isEqualTo("Author");
    }

    @Test
    @Transactional
    public void testGetAllAuthors() throws Exception {
        String body = """
            {
                "name": "Author"
            }
            """;


        MvcResult result = mvc.perform(post("/api/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult result2 = mvc.perform(get("/api/author"))
                .andExpect(status().isOk())
                .andReturn();

        List list = parseResponse(result2, List.class);
        assertThat(list).hasSize(1);


    }

    @Test
    @Transactional
    public void testUpdateAuthor() throws Exception {
        String body = """
            {
                "name": "Author"
            }
            """;


        MvcResult result = mvc.perform(post("/api/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        ResponseAuthorDto authorDto = parseResponse(result, ResponseAuthorDto.class);
        assertThat(authorDto.getId()).isGreaterThanOrEqualTo(1);

        String body2 = """
            {
                "name": "Author2"
            }
            """;

        MvcResult result2 = mvc.perform(put("/api/author/" + authorDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body2))
                .andExpect(status().isNoContent())
                .andReturn();

        Author author = authorRepository.findById(authorDto.getId()).get();
        assertThat(author.getName()).isEqualTo("Author2");
    }


    @Test
    @Transactional
    public void testDeleteAuthor() throws Exception {
        String body = """
            {
                "name": "Author"
            }
            """;


        MvcResult result = mvc.perform(post("/api/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        ResponseAuthorDto authorDto = parseResponse(result, ResponseAuthorDto.class);
        assertThat(authorDto.getId()).isGreaterThanOrEqualTo(1);

        MvcResult result2 = mvc.perform(delete("/api/author/" + authorDto.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(authorRepository.findById(authorDto.getId())).isEmpty();
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
