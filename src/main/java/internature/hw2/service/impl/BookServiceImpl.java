package internature.hw2.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import internature.hw2.dto.book.*;
import internature.hw2.entity.Book;
import internature.hw2.entity.Genre;
import internature.hw2.exception.BookNotFoundException;
import internature.hw2.exception.ReadUploadStatisticException;
import internature.hw2.repository.BookRepository;
import internature.hw2.service.AuthorService;
import internature.hw2.service.BookService;
import internature.hw2.service.GenreService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing books.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreService genreService;

    private AuthorService authorService;
    @Autowired
    public void setAuthorService(@Lazy AuthorService authorService) {
        this.authorService = authorService;
    }

    private final ObjectMapper objectMapper;


    @Override
    public ResponseBookDto createBook(CreateRequestBookDto createBookDto) {
        Book book = new Book();
        book.setTitle(createBookDto.getTitle());
        book.setYearPublished(createBookDto.getYearPublished());
        book.setGenres(genreService.getGenresFromNameList(createBookDto.getGenres()));
        book.setAuthor(authorService.getAuthorFromName(createBookDto.getAuthorName()));
        book.setPages(createBookDto.getPages());
        bookRepository.save(book);
        return buildResponseBookDto(book);
    }

    @Override
    public ResponseBookDto getBook(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return buildResponseBookDto(book);
    }

    @Override
    public void updateBook(long id, UpdateRequestBookDto updateBookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (updateBookDto.getTitle() != null) {
            book.setTitle(updateBookDto.getTitle());
        }
        if (updateBookDto.getYearPublished() != null) {
            book.setYearPublished(updateBookDto.getYearPublished());
        }
        if (updateBookDto.getGenres() != null) {
            book.setGenres(genreService.getGenresFromNameList(updateBookDto.getGenres()));
        }
        if (updateBookDto.getAuthorName() != null) {
            book.setAuthor(authorService.getAuthorFromName(updateBookDto.getAuthorName()));
        }
        if (updateBookDto.getPages() != null) {
            book.setPages(updateBookDto.getPages());
        }
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public PagedResponseDto<ListResponseBookDto> getListOfBooks(FilterRequestBookDto filterBookDto) {
        Pageable pageable = PageRequest.of(filterBookDto.getPage(),filterBookDto.getSize());
        Page<Book> bookPage = bookRepository.findAll(toPredicate(filterBookDto.getTitle(),filterBookDto.getGenres(),filterBookDto.getAuthorName(),filterBookDto.getYearPublished()),pageable);
        return getPagedBookResponse(bookPage);
    }

    @Override
    public List<Book> getBookByFilter(ReportRequestBookDto reportRequestBookDto) {
        return bookRepository.findAll(toPredicate(reportRequestBookDto.getTitle(),reportRequestBookDto.getGenres(),reportRequestBookDto.getAuthorName(),reportRequestBookDto.getYearPublished()));
    }

    @Override
    public byte[] exportBooksToExcel(List<Book> books) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet("Books");

            sheet.setColumnWidth(0, 15 * 256);
            sheet.setColumnWidth(1, 15 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Title");
            headerRow.createCell(1).setCellValue("Author");
            headerRow.createCell(2).setCellValue("Year Published");

            int rowNum = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getTitle());
                row.createCell(1).setCellValue(book.getAuthor().getName());
                row.createCell(2).setCellValue(book.getYearPublished());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public Map<String, Integer> uploadBooks(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<CreateRequestBookDto> booksDto = objectMapper.readValue(fileBytes, new TypeReference<List<CreateRequestBookDto>>() {});
            Map<String,Integer> uploadStatistics = new LinkedHashMap<>();
            uploadStatistics.put("Invalid books",0);
            List<Book> books = new ArrayList<>();
            for (CreateRequestBookDto bookDto : booksDto) {
                convertFromUpload(bookDto,uploadStatistics,books);
            }
            bookRepository.saveAll(books);
            uploadStatistics.put("Total uploaded books",books.size());
            return uploadStatistics;
        } catch (IOException e) {
            throw new ReadUploadStatisticException();
        }
    }

    @Override
    public List<Book> getBooksByAuthorId(Long id) {
        return bookRepository.findAllByAuthorId(id);
    }

    @Override
    public void deleteBooksAuthor(Long id) {
        List<Book> books = bookRepository.findAllByAuthorId(id);
        for (Book book : books) {
            book.setAuthor(null);
        }
        bookRepository.saveAll(books);
    }

    private void convertFromUpload(CreateRequestBookDto createRequestBookDto, Map<String, Integer> uploadStatistics, List<Book> books) {
        Book book = new Book();
        boolean isOk = true;
        if (createRequestBookDto.getTitle() != null) {
            book.setTitle(createRequestBookDto.getTitle());
        }
        else {
            isOk = false;
            uploadStatistics.put("No title",uploadStatistics.getOrDefault("No title",0) + 1);
        }
        if (createRequestBookDto.getYearPublished() != null) {
            book.setYearPublished(createRequestBookDto.getYearPublished());
        }
        else {
            isOk = false;
            uploadStatistics.put("No year published",uploadStatistics.getOrDefault("No year published",0) + 1);
        }
        if (createRequestBookDto.getGenres() != null) {
            book.setGenres(genreService.getGenresFromNameList(createRequestBookDto.getGenres()));
        }
        else {
            isOk = false;
            uploadStatistics.put("No genres",uploadStatistics.getOrDefault("No genres",0) + 1);
        }
        if (createRequestBookDto.getAuthorName() != null) {
            book.setAuthor(authorService.getAuthorFromName(createRequestBookDto.getAuthorName()));
        }
        else {
            isOk = false;
            uploadStatistics.put("No author name",uploadStatistics.getOrDefault("No author name",0) + 1);
        }
        if (createRequestBookDto.getPages() != null) {
            book.setPages(createRequestBookDto.getPages());
        }
        else {
            isOk = false;
            uploadStatistics.put("No pages",uploadStatistics.getOrDefault("No pages",0) + 1);
        }
        if (isOk) {
            books.add(book);
        }
        else {
            uploadStatistics.put("Invalid books",uploadStatistics.getOrDefault("Invalid books",0) + 1);
        }
    }

    private PagedResponseDto<ListResponseBookDto> getPagedBookResponse(Page<Book> page){
        return PagedResponseDto.<ListResponseBookDto>builder()
                .list(page.getContent()
                        .stream()
                        .map(this::buildListResponseBookDto)
                        .collect(Collectors.toList()))
                .totalPages(page.getTotalPages())
                .build();
    }

    private ListResponseBookDto buildListResponseBookDto(Book book) {
        return ListResponseBookDto.builder()
                .title(book.getTitle())
                .authorName(book.getAuthor().getName())
                .yearPublished(book.getYearPublished())
                .build();
    }

    private Specification<Book> toPredicate(String title, List<String> genres, String authorName, Integer yearPublished) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null){
                predicates.add(cb.like(root.get("title"),title));
            }
            if (genres != null && !genres.isEmpty()) {
                predicates.add(root.get("genres").get("name").in(genres));
            }
            if (authorName != null) {
                predicates.add(cb.like(root.get("author").get("name"),authorName));
            }
            if (yearPublished != null) {
                predicates.add(cb.equal(root.get("yearPublished"), yearPublished));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private ResponseBookDto buildResponseBookDto(Book book) {
        return ResponseBookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genres(book.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .toList())
                .yearPublished(book.getYearPublished())
                .pages(book.getPages())
                .build();
    }
}
