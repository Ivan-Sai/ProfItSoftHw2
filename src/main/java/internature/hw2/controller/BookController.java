package internature.hw2.controller;

import internature.hw2.dto.book.*;
import internature.hw2.entity.Book;
import internature.hw2.exception.ExcelWriteException;
import internature.hw2.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

//    @Value("${kafka.topic.email}")
//    private String emailTopic;

//    private final KafkaTemplate<String,Object> kafkaTemplate;



    @PostMapping()
    public ResponseEntity<ResponseBookDto> createBook(@Valid @RequestBody CreateRequestBookDto createBookDto) {
        ResponseBookDto responseBookDto = bookService.createBook(createBookDto);
//        kafkaTemplate.send(emailTopic, responseBookDto.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(createBookDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseBookDto> getBook(@PathVariable long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBookDto> updateBook(@PathVariable long id, @Valid @RequestBody UpdateRequestBookDto updateBookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, updateBookDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("_list")
    public ResponseEntity<PagedResponseDto<ListResponseBookDto>> getListOfBooks(@Valid @RequestBody FilterRequestBookDto filterBookDto) {
        return ResponseEntity.ok(bookService.getListOfBooks(filterBookDto));
    }

    @PostMapping("_report")
    public ResponseEntity<byte[]> getReportOfBooks(@Valid @RequestBody ReportRequestBookDto reportRequestBookDto) {
        List<Book> books = bookService.getBookByFilter(reportRequestBookDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=books_report.xlsx");
        try {
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bookService.exportBooksToExcel(books));
        } catch (IOException e) {
            throw new ExcelWriteException();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity uploadBooks(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.uploadBooks(file));
    }
}
