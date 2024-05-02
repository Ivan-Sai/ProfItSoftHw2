package internature.hw2.service;

import internature.hw2.dto.book.*;
import internature.hw2.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BookService {
    ResponseBookDto createBook(CreateRequestBookDto createBookDto);

    ResponseBookDto getBook(long id);

    void updateBook(long id, UpdateRequestBookDto updateBookDto);

    void deleteBook(long id);

    PagedResponseDto<ListResponseBookDto> getListOfBooks(FilterRequestBookDto filterBookDto);

    List<Book> getBookByFilter(ReportRequestBookDto reportRequestBookDto);

    byte[] exportBooksToExcel(List<Book> books) throws IOException;

    Map<String, Integer> uploadBooks(MultipartFile file);

    List<Book> getBooksByAuthorId(Long id);

    void deleteBooksAuthor(Long id);
}
